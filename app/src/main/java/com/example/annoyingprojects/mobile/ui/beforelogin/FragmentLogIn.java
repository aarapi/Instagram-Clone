package com.example.annoyingprojects.mobile.ui.beforelogin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.data.PostModel;
import com.example.annoyingprojects.data.Posts;
import com.example.annoyingprojects.data.StoryModel;
import com.example.annoyingprojects.data.UserModel;
import com.example.annoyingprojects.mobile.basemodels.BaseFragment;
import com.example.annoyingprojects.utilities.CheckSetup;
import com.example.annoyingprojects.utilities.ClassType;
import com.example.annoyingprojects.utilities.FragmentUtil;
import com.example.annoyingprojects.utilities.RequestFunction;
import com.example.connectionframework.requestframework.languageData.SavedInformation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FragmentLogIn extends BaseFragment implements View.OnClickListener{
    private ImageView iv_language;
    private EditText email, password;
    private TextView forgotPass, signUp, tvForgotPass, tv_signin, tv_error;
    private RelativeLayout btnSignIn;
    private Fragment fragmentLogIn;
    private AlertDialog alertDialog;
    private ProgressBar progressBar;


    public static FragmentLogIn newInstance(Bundle args){
        FragmentLogIn fragmentLogIn = new FragmentLogIn();
        fragmentLogIn.setArguments(args);
        return fragmentLogIn;
    }
    @Override
    public void initViews() {

        iv_language = containerView.findViewById(R.id.iv_language);
        email = containerView.findViewById(R.id.atvEmailLog);
        password = containerView.findViewById(R.id.atvPasswordLog);
        forgotPass = containerView.findViewById(R.id.tvForgotPass);
        signUp = containerView.findViewById(R.id.tv_sign_up);
        btnSignIn = containerView.findViewById(R.id.btnSignIn);
        tv_signin = containerView.findViewById(R.id.tv_signin);
        tv_error = containerView.findViewById(R.id.tv_error);

        progressBar = containerView.findViewById(R.id.progressbar);
        tvForgotPass = containerView.findViewById(R.id.tvForgotPass);
    }

    @Override
    public void bindEvents() {
        forgotPass.setOnClickListener(this);
        signUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        iv_language.setOnClickListener(this);
    }

    @Override
    public void setViews() {

//        tvForgotPass.setText(SavedInformation.getInstance().getResource(ResourceKey.ForgotPasswordText));

    }


    @Override
    public void onClick(View v) {
        if (v == btnSignIn) {
            String inEmail = email.getText().toString();
            String inPassword = password.getText().toString();

            if (validateInput(inEmail, inPassword)) {
                UserModel userModel = new UserModel();
                userModel.username = inEmail;
                userModel.password = inPassword;

                btnSignIn.setClickable(false);
                progressBar.setVisibility(View.VISIBLE);
                tv_signin.setVisibility(View.GONE);
                tv_error.setVisibility(View.GONE);

                signUser(userModel);
            }
        } else if (v == signUp) {
            FragmentUtil.switchContent(R.id.container_frame, FragmentUtil.SIGN_UP_FRAGMENT, activity, null);
        } else if (v == forgotPass) {

        }else if (v == iv_language){
            alertDialog = getLanguageDialog();
            alertDialog.show();
        }

    }


    public void signUser(UserModel userModel) {
        sendRequest(RequestFunction.loginValidate(activity.getActivityId(), userModel));
    }


    public boolean validateInput(String inemail, String inpassword) {

        if (inemail.isEmpty()) {
            email.setError("Email field is empty.");
            return false;
        }
        if (inpassword.isEmpty()) {
            password.setError("Password is empty.");
            return false;
        }

        return true;
    }

    private AlertDialog getLanguageDialog() {

        final String[] languages = {"sq","en"};
        int selectedIndex = -1;

        for (int i = 0; i<languages.length; i++){


            boolean flag = languages[i].equals(SavedInformation.getInstance().getPreferenceData(getContext(), "languageData"));

            if (flag)
            {
                selectedIndex = i;
                break;
            }
        }


        final int whichBefore = selectedIndex;
        return new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AppTheme))
                .setSingleChoiceItems(languages, selectedIndex,
                        (dialog, which) -> {
                            if (whichBefore != which) {
                                List<Object> data = new ArrayList<>();
                                SavedInformation.getInstance().setPreferenceData(getContext(),languages[which], "languageData");
                                activity.sendRequest(RequestFunction.getLanguageData(activity.getActivityId(),
                                        SavedInformation.getInstance().
                                                getPreferenceData(getContext(), "languageData")));

                                alertDialog.dismiss();
                              }
                        })
                .setCancelable(true)
                .create();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login_layout;
    }

    public AlertDialog getAlertDialog(){
        return alertDialog;
    }

    @Override
    public void onDataReceive(int action, List<Object> data) {
        if (action == CheckSetup.ServerActions.INSTA_COMMERCE_LOG_IN){
            UserModel userModel = new ClassType<UserModel>() {
            }.fromJson(data.get(0));
            SavedInformation.getInstance().setPreferenceData(getContext(), userModel, "user");

            Gson gson = new Gson();
            Type postsType = new TypeToken<ArrayList<Posts>>() {}.getType();
            Type storiesType = new TypeToken<ArrayList<StoryModel>>() {
            }.getType();

            ArrayList<Posts> posts = gson.fromJson(gson.toJson(data.get(1)),postsType);
            ArrayList<StoryModel> stories = gson.fromJson(gson.toJson(data.get(2)), storiesType);

            ArrayList<PostModel> postModels = new ArrayList<>();
            int postsSize = posts.size();
            for (int i = 0; i < postsSize; i++) {
                PostModel postModel = posts.get(i).getPostModel();
                postModel.setLinkUserImg(posts.get(i).getLinkUserImg());
                postModel.setLinkImages(posts.get(i).getLinkImages());

                postModels.add(postModel);

            }


            int storySize = stories.size();
            for (int i =0; i<storySize; i++){
                stories.get(i).ID = i+"";
            }

            List<Object> homeData = new ArrayList<>();
            homeData.add(stories);
            homeData.add(postModels);

            startActivity(CheckSetup.Activities.HOME_ACTIVITY, homeData);
        }
    }

    @Override
    public void onErrorDataReceive(int action, List<Object> data) {
       String errorResponse =  (String) data.get(0);

        ((LoginActivity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnSignIn.setClickable(true);
                progressBar.setVisibility(View.GONE);
                tv_signin.setVisibility(View.VISIBLE);
                tv_error.setText(errorResponse);
                tv_error.setVisibility(View.VISIBLE);
            }
        });


    }
}

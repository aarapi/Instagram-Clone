package com.instacommerce.annoyingprojects.mobile.ui.beforelogin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.instacommerce.annoyingprojects.R;
import com.instacommerce.annoyingprojects.data.FilterModel;
import com.instacommerce.annoyingprojects.data.PostModel;
import com.instacommerce.annoyingprojects.data.Posts;
import com.instacommerce.annoyingprojects.data.StoryModel;
import com.instacommerce.annoyingprojects.data.UserModel;
import com.instacommerce.annoyingprojects.mobile.basemodels.BaseFragment;
import com.instacommerce.annoyingprojects.repository.LocalServer;
import com.instacommerce.annoyingprojects.utilities.CheckSetup;
import com.instacommerce.annoyingprojects.utilities.ClassType;
import com.instacommerce.annoyingprojects.utilities.FragmentUtil;
import com.instacommerce.annoyingprojects.utilities.RequestFunction;
import com.instacommerce.connectionframework.requestframework.languageData.SavedInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.octopepper.mediapickerinstagram.commons.models.CategoryModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import gujc.directtalk9.common.Util9;

public class FragmentLogIn extends BaseFragment implements View.OnClickListener{
    private ImageView iv_language;
    private EditText email, password;
    private TextView forgotPass, signUp, tvForgotPass, tv_signin, tv_error;
    private RelativeLayout btnSignIn;
    private Fragment fragmentLogIn;
    private AlertDialog alertDialog;
    private ProgressBar progressBar;

    SharedPreferences sharedPreferences;


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

        sharedPreferences = getActivity().getSharedPreferences("gujc", Activity.MODE_PRIVATE);
        String id = sharedPreferences.getString("user_id", "");
//        if (!"".equals(id)) {
//            user_id.setText(id);
//        }
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
            RequestFunction.username = LocalServer.getInstance(getContext()).getUser().username;

            Gson gson = new Gson();
            Type postsType = new TypeToken<ArrayList<Posts>>() {}.getType();
            Type storiesType = new TypeToken<ArrayList<StoryModel>>() {
            }.getType();
            Type usersType = new TypeToken<ArrayList<UserModel>>() {
            }.getType();
            Type categoryType = new TypeToken<ArrayList<CategoryModel>>() {
            }.getType();

            ArrayList<Posts> posts = gson.fromJson(gson.toJson(data.get(1)),postsType);
            ArrayList<StoryModel> stories = gson.fromJson(gson.toJson(data.get(2)), storiesType);
            ArrayList<CategoryModel> categoryModels = gson.fromJson(gson.toJson(data.get(4)), categoryType);
            String userToken = (String) data.get(5);
            RequestFunction.userToken = userToken;

            LocalServer.newInstance().setUserList(gson.fromJson(gson.toJson(data.get(3)), usersType));
            LocalServer.newInstance().setCategoryModels(categoryModels);

            FilterModel.newInstance().country = userModel.country;
            FilterModel.newInstance().city = "";
            FilterModel.newInstance().allCategories = true;

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

            email.setText("");
            password.setText("");

            FirebaseAuth.getInstance().signInWithEmailAndPassword(userModel.email, userModel.password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        sharedPreferences.edit().putString("user_id", userModel.email).commit();
                    } else {
                        Util9.showMessage(getContext(), task.getException().getMessage());
                    }
                }
            });

            SavedInformation.getInstance().setPreferenceData(getContext(), userToken, "USER_TOKEN");

            startActivity(CheckSetup.Activities.HOME_ACTIVITY, homeData);
            enableLoginBtn();
        }
    }

    @Override
    public void onErrorDataReceive(int action, List<Object> data) {
       String errorResponse =  (String) data.get(0);

        ((LoginActivity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_error.setText(errorResponse);
                tv_error.setVisibility(View.VISIBLE);
                noInternetConnection();
            }
        });
    }

    @Override
    public void noInternetConnection() {
        enableLoginBtn();
    }

    private void enableLoginBtn(){
        btnSignIn.setClickable(true);
        progressBar.setVisibility(View.GONE);
        tv_signin.setVisibility(View.VISIBLE);
    }
}

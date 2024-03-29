package com.instacommerce.annoyingprojects.mobile.ui.beforelogin;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
import com.instacommerce.annoyingprojects.utilities.RequestFunction;
import com.instacommerce.connectionframework.requestframework.constants.MessagingFrameworkConstant;
import com.instacommerce.connectionframework.requestframework.languageData.SavedInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.octopepper.mediapickerinstagram.commons.models.CategoryModel;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import gujc.directtalk9.common.Util9;

public class FragmentLoginWithToken extends BaseFragment implements View.OnClickListener {

    private SharedPreferences sharedPreferences;

    private ProgressBar progressbar;
    private RelativeLayout rl_retry;
    private Button btn_retry;

    private ImageView iv_offline;
    private TextView tv_instacommerce;




    @Override
    public void initViews() {
        sharedPreferences = getActivity().getSharedPreferences("gujc", Activity.MODE_PRIVATE);

        progressbar = containerView.findViewById(R.id.progressbar);
        rl_retry = containerView.findViewById(R.id.rl_retry);
        btn_retry = containerView.findViewById(R.id.btn_retry);

        iv_offline = containerView.findViewById(R.id.iv_offline);
        tv_instacommerce = containerView.findViewById(R.id.tv_instacommerce);
    }

    @Override
    public void bindEvents() {
        btn_retry.setOnClickListener(this);
    }


    @Override
    public void setViews() {
        String userToken = SavedInformation.getInstance().getPreferenceData(getContext(), "USER_TOKEN");
        signUser(userToken);
    }


    @Override
    public void onClick(View v) {
        if (v == btn_retry){
            rl_retry.setVisibility(View.GONE);
            iv_offline.setVisibility(View.GONE);

            progressbar.setVisibility(View.VISIBLE);
            tv_instacommerce.setVisibility(View.VISIBLE);


            String userToken = SavedInformation.getInstance().getPreferenceData(getContext(), "USER_TOKEN");
            signUser(userToken);
        }
    }


    public void signUser(String userToken) {
        sendRequest(RequestFunction.loginWithToken(0, userToken));
        RequestFunction.userToken = userToken;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_login_with_token_layout;
    }

    @Override
    public void onDataReceive(int action, List<Object> data) {
        if (action == CheckSetup.ServerActions.INSTA_COMMERCE_LOG_IN_WITH_TOKEN) {
            UserModel userModel = new ClassType<UserModel>() {
            }.fromJson(data.get(0));


            SavedInformation.getInstance().setPreferenceData(activity.getBaseContext(), userModel, "user");
            RequestFunction.username = LocalServer.getInstance(getContext()).getUser().username;

            Gson gson = new Gson();
            Type postsType = new TypeToken<ArrayList<Posts>>() {
            }.getType();
            Type storiesType = new TypeToken<ArrayList<StoryModel>>() {
            }.getType();
            Type usersType = new TypeToken<ArrayList<UserModel>>() {
            }.getType();
            Type categoryType = new TypeToken<ArrayList<CategoryModel>>() {
            }.getType();

            ArrayList<Posts> posts = gson.fromJson(gson.toJson(data.get(1)), postsType);
            ArrayList<StoryModel> stories = gson.fromJson(gson.toJson(data.get(2)), storiesType);
            ArrayList<CategoryModel> categoryModels = gson.fromJson(gson.toJson(data.get(4)), categoryType);
            LocalServer.newInstance().setSearchedPosts(gson.fromJson(gson.toJson(((ArrayList) data.get(6)).get(0)),postsType));

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
            for (int i = 0; i < storySize; i++) {
                stories.get(i).ID = i + "";
            }

            List<Object> homeData = new ArrayList<>();
            homeData.add(stories);
            homeData.add(postModels);
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

            startActivity(CheckSetup.Activities.HOME_ACTIVITY, homeData);
        }
    }

    @Override
    public void onErrorDataReceive(int action, List<Object> data, int status) {
        if (action == CheckSetup.ServerActions.INSTA_COMMERCE_LOG_IN_WITH_TOKEN) {
            if (status == MessagingFrameworkConstant.STATUS_CODES.Warning) {
                Toast toast = Toast.makeText(activity, (String) data.get(0), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                switchFragment(R.id.container_frame, new FragmentLogIn());

            }else if (status == 700){
                errorOnServer();
            }else {
                Toast toast = Toast.makeText(activity, "User couldn't login", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                switchFragment(R.id.container_frame, new FragmentLogIn());
            }
        }else {
            errorOnServer();
        }
    }

    private void errorOnServer(){
        rl_retry.setVisibility(View.VISIBLE);
        progressbar.setVisibility(View.INVISIBLE);

        tv_instacommerce.setVisibility(View.GONE);
        iv_offline.setVisibility(View.INVISIBLE);

        ((TextView) containerView.findViewById(R.id.tv_offline)).setText("An error occured!");
        ((TextView) containerView.findViewById(R.id.tv_find_connection)).setText("Please try again");
    }

    @Override
    public void noInternetConnection() {
        iv_offline.setVisibility(View.VISIBLE);
        rl_retry.setVisibility(View.VISIBLE);


        progressbar.setVisibility(View.INVISIBLE);
        tv_instacommerce.setVisibility(View.GONE);

    }
}

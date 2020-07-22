package com.instacommerce.annoyingprojects.mobile.ui.beforelogin;

import android.app.Activity;
import android.content.Intent;

import com.instacommerce.annoyingprojects.R;
import com.instacommerce.annoyingprojects.data.PostModel;
import com.instacommerce.annoyingprojects.data.Posts;
import com.instacommerce.annoyingprojects.data.StoryModel;
import com.instacommerce.annoyingprojects.data.UserModel;
import com.instacommerce.annoyingprojects.mobile.basemodels.BaseActivity;
import com.instacommerce.annoyingprojects.repository.LocalServer;
import com.instacommerce.annoyingprojects.utilities.CheckSetup;
import com.instacommerce.annoyingprojects.utilities.ClassType;
import com.instacommerce.annoyingprojects.utilities.RequestFunction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SplashScreenActivity extends BaseActivity {


    @Override
    public void initViews() {
        if (LocalServer.getInstance(getApplicationContext()).getUser() == null){
            Intent intent = new Intent(getApplicationContext(),
                    LoginActivity.class);
            startActivity(intent);
            finish();
        }else {
            UserModel userModel = (UserModel) LocalServer.getInstance(getApplicationContext()).getUser();
            sendRequest(RequestFunction.loginValidate(activity.getActivityId(), userModel));
        }
    }

    @Override
    public void bindEvents() {

    }

    @Override
    public void setViews() {

    }

    @Override
    public int getLayoutContent() {
        return R.layout.activity_splash_screen;
    }

    @Override
    public Activity getActivity() {
        return null;
    }

    @Override
    public int getActivityId() {
        return 0;
    }


    @Override
    public void onDataReceive(int action, List<Object> data) {
        if (action == CheckSetup.ServerActions.INSTA_COMMERCE_LOG_IN) {
            UserModel userModel = new ClassType<UserModel>() {
            }.fromJson(data.get(0));
            RequestFunction.username = LocalServer.getInstance(getApplicationContext()).getUser().username;

            Gson gson = new Gson();
            Type postsType = new TypeToken<ArrayList<Posts>>() {
            }.getType();
            Type storiesType = new TypeToken<ArrayList<StoryModel>>() {
            }.getType();
            Type usersType = new TypeToken<ArrayList<UserModel>>() {
            }.getType();

            ArrayList<Posts> posts = gson.fromJson(gson.toJson(data.get(1)), postsType);
            ArrayList<StoryModel> stories = gson.fromJson(gson.toJson(data.get(2)), storiesType);
            LocalServer.newInstance().setUserList(gson.fromJson(gson.toJson(data.get(3)), usersType));

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


            startActivity(CheckSetup.Activities.HOME_ACTIVITY, homeData);
        }
    }
}

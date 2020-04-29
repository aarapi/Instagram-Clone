package com.example.annoyingprojects.mobile.ui.afterlogin.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.data.PostModel;
import com.example.annoyingprojects.data.Posts;
import com.example.annoyingprojects.data.StoryInfo;
import com.example.annoyingprojects.data.User;
import com.example.annoyingprojects.mobile.basemodels.BaseActivity;
import com.example.annoyingprojects.mobile.ui.afterlogin.userprofile.SettingFragment;
import com.example.annoyingprojects.mobile.ui.afterlogin.userprofile.UserProfileFragment;
import com.example.annoyingprojects.repository.LocalServer;
import com.example.annoyingprojects.utilities.CheckSetup;
import com.example.annoyingprojects.utilities.ClassType;
import com.example.annoyingprojects.utilities.FragmentUtil;
import com.example.annoyingprojects.utilities.RequestFunction;
import com.example.connectionframework.requestframework.languageData.SavedInformation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.octopepper.mediapickerinstagram.MainActivity;
import com.octopepper.mediapickerinstagram.commons.models.Post;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import yalantis.com.sidemenu.util.ViewAnimator;

import static com.example.annoyingprojects.mobile.ui.afterlogin.userprofile.UserProfileFragment.USER_PROFILE_DATA;
import static com.example.annoyingprojects.utilities.Util.setUserImageRes;

public class HomeActivity extends BaseActivity implements ViewAnimator.ViewAnimatorListener, SettingFragment.ItemClickListener{
    private ImageView iv_search_button, cv_user_img, iv_home_button, iv_add_post;
    private RelativeLayout rl_user_img;
    private HomeFragment homeFragment;
    private UserProfileFragment userProfileFragment;
    public static int scrollTime = 1;
    private User user;
    private HashMap<ImageView, ImageView> bottomMenus = new HashMap<>();
    private HashMap<ImageView, Integer> bottomMenusClicked = new HashMap<>();
    private HashMap<ImageView, Integer> bottomMenusNotClicked = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleArgs;
        bundleArgs = new Bundle();

        user = LocalServer.getInstance(getApplicationContext()).getUser();

        bundleArgs.putSerializable(HomeFragment.HOME_FRAMGENT_DATA_LIST, (Serializable) (List<Object>) getActivity().getIntent().getSerializableExtra("data"));
        homeFragment = HomeFragment.newInstance(bundleArgs);
        userProfileFragment = new UserProfileFragment();

        FragmentUtil.switchFragmentWithAnimation(R.id.fl_fragment_container,
                homeFragment,
                this,
                FragmentUtil.HOME_FRAGMENT, null);

    }
    @Override
    public void initViews() {
        iv_search_button = findViewById(R.id.iv_search_button);
        cv_user_img = findViewById(R.id.cv_user_img);
        iv_home_button = findViewById(R.id.iv_home_button);
        iv_add_post = findViewById(R.id.iv_add_post);
        rl_user_img = findViewById(R.id.rl_user_img);
    }

    @Override
    public void bindEvents() {
        iv_search_button.setOnClickListener(this);
        cv_user_img.setOnClickListener(this);
        iv_home_button.setOnClickListener(this);
        iv_add_post.setOnClickListener(this);
    }

    @Override
    public void setViews() {
        iv_home_button.setImageResource(R.drawable.ic_home_clicked);
        setUserImageRes(getApplicationContext(),user.userImage, cv_user_img);


        bottomMenus.put(iv_search_button, iv_search_button);
        bottomMenus.put(iv_home_button, iv_home_button);
        bottomMenus.put(cv_user_img, cv_user_img);

        bottomMenusClicked.put(iv_search_button, R.drawable.ic_search_clicked);
        bottomMenusClicked.put(iv_home_button, R.drawable.ic_home_clicked);
        bottomMenusClicked.put(cv_user_img, R.drawable.cardview_circle_background);

        bottomMenusNotClicked.put(iv_search_button, R.drawable.ic_search);
        bottomMenusNotClicked.put(iv_home_button, R.drawable.ic_home_run);

    }

    @Override
    public int getLayoutContent() {
        return R.layout.activity_home_layout;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public int getActivityId() {
        return CheckSetup.Activities.HOME_ACTIVITY;
    }

    @Override
    public void onClick(View v) {
        if (v == iv_search_button){
        }else if (v == iv_home_button){
            FragmentUtil.switchContent(R.id.fl_fragment_container,
                    FragmentUtil.HOME_FRAGMENT,
                    this,
                    FragmentUtil.AnimationType.SLIDE_RIGHT);
        }else if (v == cv_user_img){
            FragmentUtil.switchContent(R.id.fl_fragment_container,
                    FragmentUtil.USER_PROFILE_FRAGMENT,
                    this,
                   FragmentUtil.AnimationType.SLIDE_LEFT);

        }else if (v == iv_add_post){
            Intent intent = new Intent(this, MainActivity.class);
            startActivityForResult(intent, 1213);
        }

        setCheckedMenu((ImageView) v);
    }

    @Override
    public void onDataReceive(int action, List<Object> data) {
        if (data.size() == 0)
            return;
    }

    @Override
    public void onItemClick(String item) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1213 && resultCode == Activity.RESULT_OK) {
            List<String> postData = (List<String>) data.getSerializableExtra("result");

            sendRequest(RequestFunction.createNewPost(getActivityId(), postData));
        }
    }

    private void setCheckedMenu(ImageView imageView){
        Set<ImageView> keySet = bottomMenus.keySet();

        for (ImageView key: keySet){
            if(bottomMenus.get(key) == imageView){
                if (imageView == cv_user_img){
                    rl_user_img.setBackgroundResource(bottomMenusClicked.get(key));
                }else {
                    bottomMenus.get(key).setImageResource(bottomMenusClicked.get(key));
                }
            }else{
                if (bottomMenus.get(key) == cv_user_img){
                    rl_user_img.setBackground(null);
                }else {
                    bottomMenus.get(key).setImageResource(bottomMenusNotClicked.get(key));
                }
            }
        }
    }

}
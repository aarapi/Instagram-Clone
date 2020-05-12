package com.example.annoyingprojects.mobile.ui.afterlogin.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.data.PostModel;
import com.example.annoyingprojects.data.UserModel;
import com.example.annoyingprojects.mobile.basemodels.BaseActivity;
import com.example.annoyingprojects.mobile.ui.afterlogin.userprofile.SettingFragment;
import com.example.annoyingprojects.mobile.ui.afterlogin.userprofile.UserProfileFragment;
import com.example.annoyingprojects.repository.LocalServer;
import com.example.annoyingprojects.tasks.BitmapTask;
import com.example.annoyingprojects.utilities.CheckSetup;
import com.example.annoyingprojects.utilities.FragmentUtil;
import com.octopepper.mediapickerinstagram.MainActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import static com.example.annoyingprojects.utilities.Util.setUserImageRes;

public class HomeActivity extends BaseActivity implements SettingFragment.ItemClickListener {
    public static int scrollTime = 1;
    private UserModel userModel;

    private ImageView iv_search_button;
    private CircleImageView cv_user_img;
    private ImageView iv_home_button;
    private ImageView iv_add_post;
    private ImageView iv_likes;


    private RelativeLayout rl_user_img;
    private HomeFragment homeFragment;
    private UserProfileFragment userProfileFragment;

    private BitmapTask bitmapTask;

    private HashMap<ImageView, ImageView> bottomMenus = new HashMap<>();
    private HashMap<ImageView, Integer> bottomMenusClicked = new HashMap<>();
    private HashMap<ImageView, Integer> bottomMenusNotClicked = new HashMap<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleArgs;
        bundleArgs = new Bundle();

        userModel = LocalServer.getInstance(getApplicationContext()).getUser();

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
        iv_likes = findViewById(R.id.iv_likes);
    }

    @Override
    public void bindEvents() {
        iv_search_button.setOnClickListener(this);
        cv_user_img.setOnClickListener(this);
        iv_home_button.setOnClickListener(this);
        iv_add_post.setOnClickListener(this);
        iv_likes.setOnClickListener(this);
    }

    @Override
    public void setViews() {
        iv_home_button.setImageResource(R.drawable.ic_home_clicked);
        setUserImageRes(getApplicationContext(), userModel.userImage, (ImageView) cv_user_img);


        bottomMenus.put(iv_search_button, iv_search_button);
        bottomMenus.put(iv_home_button, iv_home_button);
        bottomMenus.put(cv_user_img, cv_user_img);
        bottomMenus.put(iv_likes, iv_likes);

        bottomMenusClicked.put(iv_search_button, R.drawable.ic_search_clicked);
        bottomMenusClicked.put(iv_home_button, R.drawable.ic_home_clicked);
        bottomMenusClicked.put(cv_user_img, R.drawable.cardview_circle_background);
        bottomMenusClicked.put(iv_likes, R.drawable.ic_heart_clicked_home);

        bottomMenusNotClicked.put(iv_search_button, R.drawable.ic_search);
        bottomMenusNotClicked.put(iv_home_button, R.drawable.ic_home_run);
        bottomMenusNotClicked.put(iv_likes, R.drawable.ic_heart);

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
            FragmentUtil.switchContent(R.id.fl_fragment_container,
                    FragmentUtil.SEARCH_FRAGMENT,
                    this,
                    null);
        }else if (v == iv_home_button){
            FragmentUtil.switchContent(R.id.fl_fragment_container,
                    FragmentUtil.HOME_FRAGMENT,
                    this,
                    null);
        }else if (v == cv_user_img){
            if (rl_user_img.getBackground() == null) {
                userProfileFragment = new UserProfileFragment();
                FragmentUtil.switchFragmentWithAnimation(R.id.fl_fragment_container,
                        userProfileFragment,
                        this,
                        FragmentUtil.USER_PROFILE_FRAGMENT,
                        null);
            }

        }else if (v == iv_add_post){
            Intent intent = new Intent(this, MainActivity.class);
            startActivityForResult(intent, CheckSetup.Activities.ADD_NEW_POST_ACTIVITY);
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

        if (requestCode == CheckSetup.Activities.ADD_NEW_POST_ACTIVITY && resultCode == Activity.RESULT_OK) {
            List<Object> newPost = (ArrayList) data.getSerializableExtra("result");

            homeFragment.getRl_upload_post().setVisibility(View.VISIBLE);
            bitmapTask = new BitmapTask(homeFragment, newPost);
            bitmapTask.execute();

        } else if (requestCode == CheckSetup.Activities.SINGLE_POST_ACTIVITY && resultCode == Activity.RESULT_OK) {
            List<PostModel> postModels = (List<PostModel>) data.getSerializableExtra("postData");
            userProfileFragment.getTv_posts_value().setText(postModels.size() + "");
            userProfileFragment.updateUserPosts(postModels);

            return;
        }
        setHomeIcon();
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

    public void setHomeIcon() {
        setCheckedMenu(iv_home_button);
    }

    public UserProfileFragment getUserProfileFragment() {
        return userProfileFragment;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public BitmapTask getBitmapTask() {
        return bitmapTask;
    }
}
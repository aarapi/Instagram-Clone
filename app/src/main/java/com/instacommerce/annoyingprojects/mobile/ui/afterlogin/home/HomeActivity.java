package com.instacommerce.annoyingprojects.mobile.ui.afterlogin.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.instacommerce.annoyingprojects.R;
import com.instacommerce.annoyingprojects.data.PostModel;
import com.instacommerce.annoyingprojects.data.UserModel;
import com.instacommerce.annoyingprojects.mobile.basemodels.BaseActivity;
import com.instacommerce.annoyingprojects.mobile.ui.afterlogin.userprofile.SettingFragment;
import com.instacommerce.annoyingprojects.mobile.ui.afterlogin.userprofile.UserProfileFragment;
import com.instacommerce.annoyingprojects.repository.LocalServer;
import com.instacommerce.annoyingprojects.tasks.BitmapTask;
import com.instacommerce.annoyingprojects.utilities.CheckSetup;
import com.instacommerce.annoyingprojects.utilities.FragmentUtil;
import com.octopepper.mediapickerinstagram.PostPickerActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.instacommerce.annoyingprojects.utilities.Util.setUserImageResPicasso;

public class HomeActivity extends BaseActivity implements SettingFragment.ItemClickListener {
    public static int scrollTime = 1;
    private UserModel userModel;

    private ImageView iv_search_button;
    private CircleImageView cv_user_img;
    private ImageView iv_home_button;
    private ImageView iv_add_post;
    private ImageView iv_likes;


    private RelativeLayout rl_user_img;
    private RelativeLayout rl_home_button;
    private RelativeLayout rl_search_button;
    private RelativeLayout rl_add_post;
    private RelativeLayout rl_user;

    private LinearLayout ll_bottom_menu;
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

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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
        ll_bottom_menu = findViewById(R.id.ll_bottom_menu);

        rl_home_button = findViewById(R.id.rl_home_button);
        rl_search_button = findViewById(R.id.rl_search_button);
        rl_add_post = findViewById(R.id.rl_add_post);
        rl_user = findViewById(R.id.rl_user);
    }

    @Override
    public void bindEvents() {
        rl_search_button.setOnClickListener(this);
        rl_user.setOnClickListener(this);
        rl_home_button.setOnClickListener(this);
        rl_add_post.setOnClickListener(this);
        iv_likes.setOnClickListener(this);
    }

    @Override
    public void setViews() {
        iv_home_button.setImageResource(R.drawable.ic_home_clicked);
        setUserImageResPicasso(userModel.userImage, cv_user_img);


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

        LocalServer.isLogedIn = true;

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
        ImageView imageView = null;
        if (v == rl_search_button) {
            FragmentUtil.switchContent(R.id.fl_fragment_container,
                    FragmentUtil.SEARCH_FRAGMENT,
                    this,
                    null);
            imageView = iv_search_button;
        } else if (v == rl_home_button) {
            FragmentUtil.switchContent(R.id.fl_fragment_container,
                    FragmentUtil.HOME_FRAGMENT,
                    this,
                    null);
            imageView = iv_home_button;
        } else if (v == rl_user) {
            if (rl_user_img.getBackground() == null) {
                userProfileFragment = new UserProfileFragment();
                FragmentUtil.switchFragmentWithAnimation(R.id.fl_fragment_container,
                        userProfileFragment,
                        this,
                        FragmentUtil.USER_PROFILE_FRAGMENT,
                        null);
            }
            imageView = cv_user_img;

        } else if (v == rl_add_post) {
            Intent intent = new Intent(this, PostPickerActivity.class);
            intent.putExtra("CATEGORIES", (Serializable) LocalServer.newInstance().getCategoryModels());
            startActivityForResult(intent, CheckSetup.Activities.ADD_NEW_POST_ACTIVITY);
            imageView = iv_add_post;
        }

        setCheckedMenu(imageView);
    }

    @Override
    public void onBackPressed() {
        FragmentUtil
                .switchContent(R.id.fl_fragment_container,
                        FragmentUtil.HOME_FRAGMENT,
                        this,
                        null);
        setHomeIcon();
    }

    @Override
    public void onDataReceive(int action, List<Object> data) {
        if (data.size() == 0)
            return;

        if (action == CheckSetup.ServerActions.INSTA_COMMERCE_HOME_DATA) {
            homeFragment.setLoading(true);
            homeFragment.onDataReceive(action, data);
        }
    }

    @Override
    public void onErrorDataReceive(int action, List<Object> data) {
        if (action == CheckSetup.ServerActions.INSTA_COMMERCE_HOME_DATA) {
            homeFragment.setLoading(true);
            homeFragment.onErrorDataReceive(action, data);
        }
    }

    @Override
    public void onItemClick(String item) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CheckSetup.Activities.ADD_NEW_POST_ACTIVITY) {
            setHomeIcon();
            if (resultCode == Activity.RESULT_OK) {
                List<Object> newPost = (ArrayList) data.getSerializableExtra("result");

                homeFragment.getRl_upload_post().setVisibility(View.VISIBLE);
                bitmapTask = new BitmapTask(homeFragment, newPost);
                bitmapTask.execute();
            }

        } else if (requestCode == CheckSetup.Activities.SINGLE_POST_ACTIVITY && resultCode == Activity.RESULT_OK) {
            List<PostModel> postModels = (List<PostModel>) data.getSerializableExtra("postData");
            userProfileFragment.getTv_posts_value().setText(postModels.size() + "");
            userProfileFragment.updateUserPosts(postModels);

            return;
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
    public CircleImageView getCv_user_img(){
        return cv_user_img;
    }

    public HomeFragment getHomeFragment() {
        return homeFragment;
    }

    public LinearLayout getLl_bottom_menu() {
        return ll_bottom_menu;
    }
}
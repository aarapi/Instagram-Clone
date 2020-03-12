package com.example.annoyingprojects.mobile.ui.afterlogin.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.mobile.basemodels.BaseActivity;
import com.example.annoyingprojects.mobile.ui.afterlogin.userprofile.SettingFragment;
import com.example.annoyingprojects.mobile.ui.afterlogin.userprofile.UserProfileFragment;
import com.example.annoyingprojects.utilities.CheckSetup;
import com.example.annoyingprojects.utilities.RequestFunction;

import java.io.Serializable;
import java.util.List;

import yalantis.com.sidemenu.util.ViewAnimator;

import static com.example.annoyingprojects.mobile.ui.afterlogin.userprofile.UserProfileFragment.USER_PROFILE_DATA;
import static com.example.annoyingprojects.utilities.Util.setUserImageRes;

public class HomeActivity extends BaseActivity implements ViewAnimator.ViewAnimatorListener, SettingFragment.ItemClickListener{
    private ImageView iv_search_button, cv_user_img, iv_home_button;
    private HomeFragment homeFragment;
    private UserProfileFragment userProfileFragment;
    private RelativeLayout rl_user_img;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sendRequest(RequestFunction.getPostData(getActivityId()));
        addToRegister(CheckSetup.ServerActions.ANNOYING_PROJECTS_HOME_DATA);
        addToRegister(CheckSetup.ServerActions.ANNOYING_PROJECTS_USER_PROFILE);
    }
    @Override
    public void initViews() {
        iv_search_button = findViewById(R.id.iv_search_button);
        cv_user_img = findViewById(R.id.cv_user_img);
        iv_home_button = findViewById(R.id.iv_home_button);
        rl_user_img = findViewById(R.id.rl_user_img);
    }

    @Override
    public void bindEvents() {
        iv_search_button.setOnClickListener(this);
        cv_user_img.setOnClickListener(this);
        iv_home_button.setOnClickListener(this);

    }

    @Override
    public void setViews() {
        iv_home_button.setImageResource(R.drawable.ic_home_clicked);
        setUserImageRes(getApplicationContext(),"https://techcrunch.com/wp-content/uploads/2016/07/gettyimages-80751598-e1484590456411.jpg?w=730&crop=1", cv_user_img);

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
            iv_search_button.setImageResource(R.drawable.ic_search_clicked);
            iv_home_button.setImageResource(R.drawable.ic_home_run);
        }else if (v == iv_home_button){
            sendRequest(RequestFunction.getPostData(getActivityId()));
            iv_home_button.setImageResource(R.drawable.ic_home_clicked);
            iv_search_button.setImageResource(R.drawable.ic_search);
        }else if (v == cv_user_img){
            rl_user_img.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circular_border_shape));
            iv_home_button.setImageResource(R.drawable.ic_home_run);
            iv_search_button.setImageResource(R.drawable.ic_search);
            sendRequest(RequestFunction.getUserProfileData(getActivityId()));
        }
    }

    @Override
    public void onDataReceive(int action, List<Object> data) {
        if (data.size() == 0)
            return;
        Bundle bundleArgs;

        if (action == CheckSetup.ServerActions.ANNOYING_PROJECTS_HOME_DATA){
             bundleArgs = new Bundle();
             bundleArgs.putSerializable(HomeFragment.HOME_FRAMGENT_DATA_LIST, (Serializable) data);
            homeFragment = HomeFragment.newInstance(bundleArgs);
            fragmentManager.beginTransaction()
                    .replace(R.id.fl_fragment_container, homeFragment)
                    .addToBackStack(null)
                    .commit();
        }else if (action == CheckSetup.ServerActions.ANNOYING_PROJECTS_USER_PROFILE){
            bundleArgs = new Bundle();
            bundleArgs.putSerializable(USER_PROFILE_DATA, (Serializable) data);
            userProfileFragment = UserProfileFragment.newInstance(bundleArgs);
            fragmentManager.beginTransaction()
                    .replace(R.id.fl_fragment_container, userProfileFragment)
                    .addToBackStack(null)
                    .commit();

        }
    }


    @Override
    public void onItemClick(String item) {

    }
}
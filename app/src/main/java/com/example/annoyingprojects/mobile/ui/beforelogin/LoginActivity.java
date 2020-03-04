package com.example.annoyingprojects.mobile.ui.beforelogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.mobile.basemodels.BaseActivity;
import com.example.annoyingprojects.utilities.CheckSetup;

import java.util.List;

import static com.example.annoyingprojects.utilities.CheckSetup.initializeApplicationActivity;

public class LoginActivity extends BaseActivity {
    private Fragment fragmentSplash, fragmentSignIn, fragmentSignUp, fragmentDashboard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeApplicationActivity();
        addToRegister(CheckSetup.ServerActions.ANNOYING_PROJECTS_LOG_IN);


        fragmentSplash = new FramgentSplashScreen();
        fragmentSignIn = new FragmentLogIn();
        fragmentSignUp = new FragmentSignUp();
        fragmentManager.beginTransaction()
                .add(android.R.id.content, fragmentSplash).commit();

    }

    @Override
    public void initViews() {

    }

    @Override
    public void bindEvents() {

    }

    @Override
    public void setViews() {

    }

    @Override
    public int getLayoutContent() {
        return R.layout.dashboard_activity;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public int getActivityId() {
        return CheckSetup.Activities.LOG_IN_ACTIVITY;
    }

    @Override
    public void onDataReceive(int action, List<Object> data) {
        startActivity(CheckSetup.Activities.DASHBOARD_ACTIVITY);
    }

    public Fragment getFragmentSplash() {
        return fragmentSplash;
    }

    public Fragment getFragmentSignIn() {
        return fragmentSignIn;
    }

    public Fragment getFragmentSignUp() {
        return fragmentSignUp;
    }
}

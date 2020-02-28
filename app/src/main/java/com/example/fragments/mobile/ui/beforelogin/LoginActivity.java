package com.example.fragments.mobile.ui.beforelogin;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fragments.R;
import com.example.fragments.mobile.basemodels.BaseActivity;
import com.example.fragments.utilities.CheckSetup;

public class LoginActivity extends BaseActivity {
    private Fragment fragmentSplash, fragmentSignIn, fragmentSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addToRegister(CheckSetup.ServerActions.ANNOYING_PROJECTS_LOG_IN);

    }

    @Override
    public void initViews() {
        fragmentSplash = new FramgentSplashScreen();
        fragmentSignIn = new FragmentLogIn();
        fragmentSignUp = new FragmentSignUp();
        fragmentManager.beginTransaction()
                .add(android.R.id.content, fragmentSplash).commit();
    }

    @Override
    public void bindEvents() {

    }

    @Override
    public void setViews() {

    }

    @Override
    public int getLayoutContent() {
        return R.layout.main_activity;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public int getActivityId() {
        return 0;
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

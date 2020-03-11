package com.example.annoyingprojects.mobile.ui.afterlogin.personaldata;

import android.app.Activity;


import com.example.annoyingprojects.R;
import com.example.annoyingprojects.mobile.basemodels.BaseActivity;
import com.example.annoyingprojects.utilities.CheckSetup;


public class PersonalDataActivity extends BaseActivity {

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
        return R.layout.personal_data_activity;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public int getActivityId() {
        return CheckSetup.Activities.PERSONAL_DATA_ACTIVITY;
    }
}

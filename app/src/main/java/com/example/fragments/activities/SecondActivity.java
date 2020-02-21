package com.example.fragments.activities;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.fragments.basemodels.BaseActivity;
import com.example.fragments.R;
import com.example.fragments.utilities.CheckSetup;
import com.example.fragments.utilities.RequestFunction;

import java.util.List;

public class SecondActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sendRequest(RequestFunction.getTest(getActivityId()));
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
        return R.layout.second_activity;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public int getActivityId() {
        return CheckSetup.LocalActions.ANNOYING_PROJECTS_SECOND_ACTIVITY;
    }

    @Override
    public void onDataReceive(int action, List<Object> data) {
        super.onDataReceive(action, data);
    }
}

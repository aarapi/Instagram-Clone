package com.example.annoyingprojects.mobile.ui.afterlogin.personaldata;

import android.app.Activity;
import android.webkit.WebView;


import com.example.annoyingprojects.R;
import com.example.annoyingprojects.mobile.basemodels.BaseActivity;
import com.example.annoyingprojects.utilities.CheckSetup;


public class CourseExampleActivity extends BaseActivity {

    private WebView wv_course_example;
    @Override
    public void initViews() {
        wv_course_example = findViewById(R.id.wv_course_example);
    }

    @Override
    public void bindEvents() {

    }

    @Override
    public void setViews() {
        wv_course_example.loadUrl("https://zabanexam.com/toefl/");

    }

    @Override
    public int getLayoutContent() {
        return R.layout.activity_courseexample_layout;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public int getActivityId() {
        return CheckSetup.Activities.COURSE_EXAMPLE_ACTIVITY;
    }
}

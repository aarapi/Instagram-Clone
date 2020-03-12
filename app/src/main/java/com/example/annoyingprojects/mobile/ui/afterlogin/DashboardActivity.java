package com.example.annoyingprojects.mobile.ui.afterlogin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.mobile.basemodels.BaseActivity;
import com.example.annoyingprojects.utilities.CheckSetup;

import java.util.List;

import yalantis.com.sidemenu.util.ViewAnimator;

public class DashboardActivity extends BaseActivity implements ViewAnimator.ViewAnimatorListener, View.OnClickListener {

    private ImageView user_photo;
    private TextView user_name, user_id;


    private CardView cv_personal_data, cv_course_schedules, cv_study_result, cv_attendance, cv_course_booking, cv_course_plan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        sendRequest(RequestFunction.getDashboardData(getActivityId()));
        addToRegister(CheckSetup.ServerActions.ANNOYING_PROJECTS_DASHBOARD_ACTIVITY);


    }

    @Override
    public void initViews() {

        cv_attendance = findViewById(R.id.cv_attendance);
        cv_course_booking = findViewById(R.id.cv_course_booking);
        cv_course_plan = findViewById(R.id.cv_course_plan);
        cv_course_schedules = findViewById(R.id.cv_course_schedules);
        cv_personal_data = findViewById(R.id.cv_personal_data);
        cv_study_result = findViewById(R.id.cv_study_result);

        user_id = findViewById(R.id.user_id);
        user_name = findViewById(R.id.user_name);
        user_photo = findViewById(R.id.user_photo);


    }

    @Override
    public void bindEvents() {


        cv_study_result.setOnClickListener(this);
        cv_course_booking.setOnClickListener(this);
        cv_course_plan.setOnClickListener(this);
        cv_course_schedules.setOnClickListener(this);
        cv_personal_data.setOnClickListener(this);
        cv_study_result.setOnClickListener(this);
        cv_attendance.setOnClickListener(this);
    }

    @Override
    public void setViews() {

    }

    @Override
    public int getLayoutContent() {
        return R.layout.activity_dashboard_layout;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public int getActivityId() {
        return CheckSetup.Activities.DASHBOARD_ACTIVITY;
    }

    @Override
    public void onDataReceive(int action, List<Object> data) {
        super.onDataReceive(action, data);
    }


}

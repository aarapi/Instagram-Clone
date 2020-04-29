package com.example.annoyingprojects.mobile.ui.afterlogin;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.mobile.basemodels.BaseActivity;
import com.example.annoyingprojects.utilities.CheckSetup;

import java.util.List;

import yalantis.com.sidemenu.util.ViewAnimator;


public class DashboardActivity extends BaseActivity implements ViewAnimator.ViewAnimatorListener, View.OnClickListener {

    private ImageView user_photo;
    private TextView user_name, user_id;


    private CardView cv_zaban_test, cv_reading, cv_listening, cv_speaking, cv_writing, cv_essay_sample;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        sendRequest(RequestFunction.getDashboardData(getActivityId()));

    }

    @Override
    public void initViews() {

        cv_speaking = findViewById(R.id.cv_speaking);
        cv_writing = findViewById(R.id.cv_writing);
        cv_essay_sample = findViewById(R.id.cv_essay_sample);
        cv_reading = findViewById(R.id.cv_reading);
        cv_zaban_test = findViewById(R.id.cv_zaban_test);
        cv_listening = findViewById(R.id.cv_listening);

        user_id = findViewById(R.id.user_id);
        user_name = findViewById(R.id.user_name);
        user_photo = findViewById(R.id.user_photo);


    }

    @Override
    public void bindEvents() {
        cv_listening.setOnClickListener(this);
        cv_writing.setOnClickListener(this);
        cv_essay_sample.setOnClickListener(this);
        cv_reading.setOnClickListener(this);
        cv_zaban_test.setOnClickListener(this);
        cv_listening.setOnClickListener(this);
        cv_speaking.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        String url = null;
        if(v == cv_zaban_test){
            url = "https://zabanexam.com/toefl/";
        }else if(v == cv_reading){
            url = "https://www.youtube.com/watch?v=gQkQryk7-Qg&list=PLypKRMg8Q-k6BMsbsGncnMJJmw6mg3ib4";
        }
        else if (v == cv_listening){
            url = "https://www.youtube.com/watch?v=TSAZKU1zVGM&list=PLypKRMg8Q-k4QO-cdsCihPSFcw7YLkT8p";
        }
        else if(v == cv_speaking){
            url = "https://www.youtube.com/watch?v=JkIcekKgACE&list=PLypKRMg8Q-k62D9YUVkloW_3Eg0x0XDTv";
        }
        else if (v == cv_writing){
            url = "https://www.youtube.com/watch?v=WRpLBMg2QfU&list=PLypKRMg8Q-k5YPOkIzYedjMCpqtp_GPzi";
        }else if (v == cv_essay_sample){
            url = "https://www.toeflresources.com/sample-toefl-essays/";
        }
        openCustomTabs(url);
    }


    private void openCustomTabs(String url){
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.textcardColor));
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(
                getResources(), R.drawable.ic_back));
        builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
        builder.enableUrlBarHiding();
        builder.setShowTitle(false);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
}

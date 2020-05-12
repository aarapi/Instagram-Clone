package com.example.annoyingprojects.mobile.basemodels;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.appconfiguration.ApplicationActivity;
import com.example.annoyingprojects.utilities.CheckSetup;
import com.example.connectionframework.requestframework.receiver.RequestReceived;
import com.example.connectionframework.requestframework.sender.Request;
import com.example.connectionframework.requestframework.sender.SenderBridge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import yalantis.com.sidemenu.interfaces.Resourceble;
import yalantis.com.sidemenu.interfaces.ScreenShotable;
import yalantis.com.sidemenu.model.SlideMenuItem;
import yalantis.com.sidemenu.util.ViewAnimator;

public abstract class BaseActivity extends AppCompatActivity implements
        View.OnClickListener {
    public FragmentManager fragmentManager;
    private SenderBridge senderBridge;
    protected BaseActivity activity;
    private ApplicationActivity applicationActivity;
    boolean isCreated = true;

    private static final long MOVE_DEFAULT_TIME = 700;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this;
        fragmentManager = getSupportFragmentManager();
        setContentView(getLayoutContent());

        RequestReceived requestReceived = new RequestReceived() {
            @Override
            public void onRequestReceived(int p_action, List<Object> data) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onDataReceive(p_action, data);
                    }
                });

            }
            @Override
            public void onErrorReceived(int p_action, List<Object> data) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onErrorDataReceive(p_action, data);
                    }
                });

            }
        };
        senderBridge = new SenderBridge(requestReceived);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isCreated) {
            initViews();
            setViews();
            bindEvents();

            isCreated = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    public void sendRequest(Request request) {

        if (senderBridge != null)
            senderBridge.sendMessage(request);
    }

    public void onDataReceive(int action, List<Object> data) {}
    public void onErrorDataReceive(int action, List<Object> data) {}


    protected void switchFragment(int fragmentContainerId, Fragment nextFragment) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(fragmentContainerId, nextFragment);
        fragmentTransaction
                .commit();
    }

    protected void startActivity(int activityId) {
        applicationActivity = CheckSetup.applicationActivityMap.get(activityId);
        if (applicationActivity != null) {
            Intent intent = new Intent(getApplicationContext(), applicationActivity.getActivityClass());
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);

        }
    }
    protected void startActivity(int activityId, List<Object> data) {
        applicationActivity = CheckSetup.applicationActivityMap.get(activityId);
        if (applicationActivity != null) {
            Intent intent = new Intent(getApplicationContext(), applicationActivity.getActivityClass());
            intent.putExtra("data", (Serializable) data);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);

        }
    }



    public abstract void initViews();
    public abstract void bindEvents();
    public abstract void setViews();
    public abstract int getLayoutContent();
    public abstract Activity getActivity();
    public abstract int getActivityId();

    @Override
    public void onClick(View v) {

    }
}


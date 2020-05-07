package com.example.annoyingprojects.mobile.basemodels;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.appconfiguration.ApplicationActivity;
import com.example.annoyingprojects.mobile.ui.afterlogin.home.HomeActivity;
import com.example.annoyingprojects.utilities.CheckSetup;
import com.example.annoyingprojects.utilities.FragmentUtil;
import com.example.connectionframework.requestframework.json.Deserializer;
import com.example.connectionframework.requestframework.json.JsonWrapper;
import com.example.connectionframework.requestframework.receiver.RequestReceived;
import com.example.connectionframework.requestframework.sender.Message;
import com.example.connectionframework.requestframework.sender.Request;
import com.example.connectionframework.requestframework.sender.SenderBridge;

import java.io.Serializable;
import java.util.List;

public abstract class BaseFragment extends Fragment {
    protected View containerView;
    protected BaseActivity activity;
    private ApplicationActivity applicationActivity;
    private SenderBridge senderBridge;
    private static final long MOVE_DEFAULT_TIME = 700;

    public static String ACTION_DATA_RECEIVER_BASE = "com.example";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        containerView = inflater.inflate(getLayoutId(), container, false);

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

       return containerView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = (BaseActivity) getActivity();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        containerView.setFocusableInTouchMode(true);
        containerView.requestFocus();

        containerView.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {


                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {
                    onBackClicked();
                }

                return true;
            }
        });

        initViews();
        setViews();
        bindEvents();
    }



    public abstract void initViews();
    public abstract void bindEvents();
    public abstract void setViews();
    public abstract int getLayoutId();

    protected void startActivity(int activityId) {
        applicationActivity = CheckSetup.applicationActivityMap.get(activityId);
        if (applicationActivity != null) {
            Intent intent = new Intent(activity.getApplicationContext(), applicationActivity.getActivityClass());
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.enter, R.anim.exit);
        }
    }
    protected void startActivity(int activityId, List<Object> data) {
        applicationActivity = CheckSetup.applicationActivityMap.get(activityId);
        if (applicationActivity != null) {
            Intent intent = new Intent(getContext(), applicationActivity.getActivityClass());
            intent.putExtra("data", (Serializable) data);
            startActivity(intent);
            activity.overridePendingTransition(R.anim.enter, R.anim.exit);

        }
    }

    protected void changeFragment(Fragment nextFragment) {

        FragmentTransaction fragmentTransaction = activity.fragmentManager.beginTransaction();

        fragmentTransaction.replace(android.R.id.content, nextFragment);
        fragmentTransaction
                .commit();
    }


    public void sendRequest(Request request) {

        if (senderBridge != null)
            senderBridge.sendMessage(request);
    }

    public void setFragmentView(List<Object> data){

    }

    public void onDataReceive(int action, List<Object> data) {}
    public void onErrorDataReceive(int action, List<Object> data) {}

    public void onBackClicked() {
    }

    ;



}

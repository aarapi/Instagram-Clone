package com.example.annoyingprojects.mobile.basemodels;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.annoyingprojects.appconfiguration.ApplicationActivity;
import com.example.annoyingprojects.utilities.CheckSetup;
import com.example.connectionframework.requestframework.json.JsonWrapper;
import com.example.connectionframework.requestframework.sender.Message;
import com.example.connectionframework.requestframework.sender.Request;
import com.example.connectionframework.requestframework.sender.SenderBridge;

import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {
    public FragmentManager fragmentManager;
    private SenderBridge senderBridge;
    protected BaseActivity activity;
    private ApplicationActivity applicationActivity;

    private static final long MOVE_DEFAULT_TIME = 700;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this;
        fragmentManager = getSupportFragmentManager();
        setContentView(getLayoutContent());

        senderBridge = new SenderBridge(getActivity(), BaseFragment.ACTION_DATA_RECEIVER_BASE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initViews();
        setViews();
        bindEvents();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public abstract void initViews();
    public abstract void bindEvents();
    public abstract void setViews();
    public abstract int getLayoutContent();
    public abstract Activity getActivity();
    public abstract int getActivityId();

    public void sendRequest(Request request) {

        if (senderBridge != null)
            senderBridge.sendMessage(request);
    }

    public void addToRegister(int action){
        DataReceiver receiver = new DataReceiver();
        String _action = BaseFragment.ACTION_DATA_RECEIVER_BASE + action;
        getActivity().registerReceiver(receiver, new IntentFilter(_action));
    }

    class DataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context p_context, Intent p_intent) {
            int _action = p_intent.getIntExtra("action", -1);

            Message message = JsonWrapper.getobject(p_intent.getStringExtra("data"));

            onDataReceive(_action, message.getData());

        }
    }

    public void onDataReceive(int action, List<Object> data) {

    }


    protected void changeFragment(String tagTransition, int viewId, Fragment nextFragment) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        // 2. Shared Elements Transition
        TransitionSet enterTransitionSet = new TransitionSet();
        enterTransitionSet.addTransition(TransitionInflater.from(getApplicationContext()).inflateTransition(android.R.transition.move));
        enterTransitionSet.setDuration(MOVE_DEFAULT_TIME);
        nextFragment.setSharedElementEnterTransition(enterTransitionSet);


        if (tagTransition != null || viewId != -1) {
            View logo = findViewById(viewId);
            fragmentTransaction.addSharedElement(logo, tagTransition);
        }
        fragmentTransaction.replace(android.R.id.content, nextFragment);
        fragmentTransaction
                .commit();
    }

    protected void startActivity(int activityId) {
        applicationActivity = CheckSetup.applicationActivityMap.get(activityId);
        if (applicationActivity != null) {
            Intent intent = new Intent(getApplicationContext(), applicationActivity.getActivityClass());
            startActivity(intent);
        }
    }

}


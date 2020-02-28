package com.example.fragments.mobile.basemodels;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.connectionframework.requestframework.json.Deserializer;
import com.example.connectionframework.requestframework.json.JsonWrapper;
import com.example.connectionframework.requestframework.sender.Message;
import com.example.fragments.R;
import com.example.fragments.mobile.ui.beforelogin.FragmentLogIn;

import java.util.List;

public abstract class BaseFragment extends Fragment {
    protected View containerView;
    private int layoutId;
    protected BaseActivity activity;

    private static final long MOVE_DEFAULT_TIME = 700;

    public static String ACTION_DATA_RECEIVER_BASE = "com.example";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       containerView = inflater.inflate(layoutId, null);
       return containerView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = (BaseActivity) getActivity();
        initViews();
        setViews();
        bindEvents();


    }


    public BaseFragment(int layoutId) {
        this.layoutId = layoutId;
    }

    public abstract void initViews();
    public abstract void bindEvents();
    public abstract void setViews();


    public void addToRegister(int action) {
        BaseFragment.DataReceiver receiver = new DataReceiver();
        String _action = BaseFragment.ACTION_DATA_RECEIVER_BASE + action;
        getActivity().registerReceiver(receiver, new IntentFilter(_action));
    }

    protected void changeFragment(String tagTransition, int viewId, Fragment nextFragment) {

        FragmentTransaction fragmentTransaction = activity.fragmentManager.beginTransaction();


        // 2. Shared Elements Transition
        TransitionSet enterTransitionSet = new TransitionSet();
        enterTransitionSet.addTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        enterTransitionSet.setDuration(MOVE_DEFAULT_TIME);
        nextFragment.setSharedElementEnterTransition(enterTransitionSet);


        if (tagTransition != null || viewId != -1) {
            View logo = containerView.findViewById(viewId);
            fragmentTransaction.addSharedElement(logo, tagTransition);
        }
        fragmentTransaction.replace(android.R.id.content, nextFragment);
        fragmentTransaction
                .commit();
    }

    protected void changeFragment(Fragment nextFragment) {
        changeFragment(null, -1, nextFragment);
    }


    class DataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context p_context, Intent p_intent) {
            int _action = p_intent.getIntExtra("action", -1);

            Deserializer deserializer = new Deserializer();


            Message message = JsonWrapper.getobject(p_intent.getStringExtra("data"));

            onDataReceive(_action, message.getData());

        }
    }

    public void onDataReceive(int action, List<Object> data) {

    }

}

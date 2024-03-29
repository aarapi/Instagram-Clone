package com.instacommerce.annoyingprojects.mobile.basemodels;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.instacommerce.annoyingprojects.R;
import com.instacommerce.annoyingprojects.appconfiguration.ApplicationActivity;
import com.instacommerce.annoyingprojects.utilities.CheckSetup;
import com.instacommerce.connectionframework.requestframework.receiver.RequestReceived;
import com.instacommerce.connectionframework.requestframework.sender.Request;
import com.instacommerce.connectionframework.requestframework.sender.SenderBridge;

import java.io.Serializable;
import java.util.List;

import static com.instacommerce.annoyingprojects.utilities.Util.isNetworkAvailable;

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

            @Override
            public void onErrorReceived(int p_action, List<Object> data, int status) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onErrorDataReceive(p_action, data, status);
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
            activity.overridePendingTransition(R.anim.enter, R.anim.enter_anim);
        }
    }
    protected void startActivity(int activityId, List<Object> data) {
        applicationActivity = CheckSetup.applicationActivityMap.get(activityId);
        if (applicationActivity != null) {
            Intent intent = new Intent(getContext(), applicationActivity.getActivityClass());
            intent.putExtra("data", (Serializable) data);
            startActivity(intent);
            activity.overridePendingTransition(R.anim.enter, R.anim.enter_anim);

        }
    }

    protected void switchFragment(int fragmentContainerId, Fragment nextFragment) {

        FragmentTransaction fragmentTransaction = activity.fragmentManager.beginTransaction();

        fragmentTransaction.replace(fragmentContainerId, nextFragment);
        fragmentTransaction
                .commit();
    }


    public void sendRequest(Request request) {
        if (isNetworkAvailable(getActivity())){
            if (senderBridge != null)
                senderBridge.sendMessage(request);
        }else {
            Toast toast = Toast.makeText(getContext(), "No interenet connection.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            noInternetConnection();
        }

    }

    public void setFragmentView(List<Object> data){

    }

    public void onDataReceive(int action, List<Object> data) {}
    public void onErrorDataReceive(int action, List<Object> data) {}
    public void onErrorDataReceive(int action, List<Object> data, int status) {}
    public void noInternetConnection(){}


    public void onBackClicked() {
    }

    ;



}

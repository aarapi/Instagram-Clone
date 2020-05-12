package com.example.annoyingprojects.mobile.ui.afterlogin.messages;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.data.MessageUsersModel;
import com.example.annoyingprojects.mobile.basemodels.BaseActivity;
import com.example.annoyingprojects.utilities.CheckSetup;
import com.example.annoyingprojects.utilities.FragmentUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MessagesActivity extends BaseActivity implements  MaterialSearchBar.OnSearchActionListener {




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentUtil.switchFragmentWithAnimation(R.id.fr_fragment_container
                , new FragmentMessageUsers()
                , activity
                , FragmentUtil.MESSAGE_USERS_FRAGMENT
                , null);
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
        return R.layout.activity_messages_layout;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public int getActivityId() {
        return CheckSetup.Activities.MESSAGES_ACTIVITY;
    }


    @Override
    public void onSearchStateChanged(boolean enabled) {
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }

    @Override
    public void onDataReceive(int action, List<Object> data) {
        if (action == CheckSetup.ServerActions.INSTA_COMMERCE_MESSAGE_USERS) {
            Gson gson = new Gson();
            Type founderListType = new TypeToken<ArrayList<MessageUsersModel>>() {
            }.getType();

            ArrayList<MessageUsersModel> messageUsersModels = gson.fromJson(gson.toJson(data.get(0)),
                    founderListType);

        } else if (action == CheckSetup.ServerActions.INSTA_COMMERCE_USER_MESSAGES) {

        }
    }
}

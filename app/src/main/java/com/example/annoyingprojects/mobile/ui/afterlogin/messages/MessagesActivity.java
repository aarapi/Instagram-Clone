package com.example.annoyingprojects.mobile.ui.afterlogin.messages;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.adapters.RecyclerViewAdapterMessageUsers;
import com.example.annoyingprojects.data.Posts;
import com.example.annoyingprojects.data.UserMessagesModel;
import com.example.annoyingprojects.mobile.basemodels.BaseActivity;
import com.example.annoyingprojects.utilities.CheckSetup;
import com.example.annoyingprojects.utilities.RequestFunction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MessagesActivity extends BaseActivity implements  MaterialSearchBar.OnSearchActionListener {

    RecyclerViewAdapterMessageUsers recyclerViewAdapterMessageUsers;
    private RecyclerView rv_message_users;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendRequest(RequestFunction.getMessageUsers(getActivityId()));
    }

    @Override
    public void initViews() {
        rv_message_users = findViewById(R.id.rv_message_users);
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
        Gson gson = new Gson();
        Type founderListType = new TypeToken<ArrayList<UserMessagesModel>>() {
        }.getType();

        ArrayList<UserMessagesModel> userMessagesModels = gson.fromJson(gson.toJson(data.get(0)),
                founderListType);

        recyclerViewAdapterMessageUsers = new RecyclerViewAdapterMessageUsers(userMessagesModels);
        rv_message_users.setAdapter(recyclerViewAdapterMessageUsers);

    }
}

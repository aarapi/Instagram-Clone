package com.example.annoyingprojects.mobile.ui.afterlogin.messages;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.adapters.RecyclerViewAdapterMessageUsers;
import com.example.annoyingprojects.data.MessageUsersModel;
import com.example.annoyingprojects.mobile.basemodels.BaseFragment;
import com.example.annoyingprojects.repository.LocalServer;
import com.example.annoyingprojects.utilities.CheckSetup;
import com.example.annoyingprojects.utilities.FragmentUtil;
import com.example.annoyingprojects.utilities.RequestFunction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FragmentMessageUsers extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerViewAdapterMessageUsers recyclerViewAdapterMessageUsers;
    private RecyclerView rv_message_users;
    private SwipeRefreshLayout swipe_refresh;

    private ProgressBar progress;
    private ImageView iv_backbtn;

    @Override
    public void initViews() {
        rv_message_users = containerView.findViewById(R.id.rv_message_users);
        iv_backbtn = containerView.findViewById(R.id.iv_backbtn);

        progress = containerView.findViewById(R.id.progress);
        swipe_refresh = containerView.findViewById(R.id.swipe_refresh);

        if (LocalServer.newInstance().getMessageUsersModels() == null) {
            progress.setVisibility(View.VISIBLE);
            sendRequest(RequestFunction.getMessageUsers(0));
        } else {
            setMessageUsersList();
        }
    }


    @Override
    public void bindEvents() {
        iv_backbtn.setOnClickListener(this);
        swipe_refresh.setOnRefreshListener(this);
    }

    @Override
    public void setViews() {

    }

    @Override
    public void onBackClicked() {
        ((MessagesActivity) getContext()).finish();
    }

    @Override
    public void onDataReceive(int action, List<Object> data) {
        if (action == CheckSetup.ServerActions.INSTA_COMMERCE_MESSAGE_USERS) {
            Gson gson = new Gson();
            Type founderListType = new TypeToken<ArrayList<MessageUsersModel>>() {
            }.getType();

            LocalServer.newInstance().setMessageUsersModels(gson.fromJson(gson.toJson(data.get(0)),
                    founderListType));

            if (swipe_refresh.isRefreshing()) {
                refreshData();
            } else {
                setMessageUsersList();
            }
        }
    }

    @Override
    public void onErrorDataReceive(int action, List<Object> data) {
        if (swipe_refresh.isRefreshing()) {
            swipe_refresh.setRefreshing(false);
        }

        progress.setVisibility(View.GONE);

    }

    private void setMessageUsersList() {
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity().getBaseContext());
        rv_message_users.setLayoutManager(linearLayoutManager);
        rv_message_users.setHasFixedSize(true);

        progress.setVisibility(View.GONE);
        recyclerViewAdapterMessageUsers = new RecyclerViewAdapterMessageUsers(getContext()
                , LocalServer.newInstance().getMessageUsersModels());
        rv_message_users.setAdapter(recyclerViewAdapterMessageUsers);

        recyclerViewAdapterMessageUsers.SetOnItemClickListener(new RecyclerViewAdapterMessageUsers.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MessageUsersModel messageUsersModel = LocalServer.newInstance().getMessageUsersModels().get(position);
                Bundle args = new Bundle();
                args.putSerializable("USER_MESSAGES", (Serializable) messageUsersModel);
                args.putSerializable("IS_FROM_MAIN_ACTIVITY", (Serializable) false);
                FragmentUserMessages fragmentUserMessages = FragmentUserMessages.newInstance(args);

                FragmentUtil.switchFragmentWithAnimation(R.id.fr_fragment_container
                        , fragmentUserMessages
                        , activity
                        , FragmentUtil.USER_MESSAGES_FRAGMENT
                        , null);

            }
        });
    }

    private void refreshData() {
        if (swipe_refresh.isRefreshing()) {
            swipe_refresh.setRefreshing(false);
        }
        recyclerViewAdapterMessageUsers.setMessageUsersModels(LocalServer.newInstance().getMessageUsersModels());
        recyclerViewAdapterMessageUsers.SetOnItemClickListener(new RecyclerViewAdapterMessageUsers.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MessageUsersModel messageUsersModel = LocalServer.newInstance().getMessageUsersModels().get(position);
                Bundle args = new Bundle();
                args.putSerializable("USER_MESSAGES", (Serializable) messageUsersModel);
                args.putSerializable("IS_FROM_MAIN_ACTIVITY", (Serializable) false);
                FragmentUserMessages fragmentUserMessages = FragmentUserMessages.newInstance(args);

                FragmentUtil.switchFragmentWithAnimation(R.id.fr_fragment_container
                        , fragmentUserMessages
                        , activity
                        , FragmentUtil.USER_MESSAGES_FRAGMENT
                        , null);

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_message_users;
    }

    @Override
    public void onClick(View view) {
        if (view == iv_backbtn) {
            onBackClicked();
        }
    }

    @Override
    public void onRefresh() {
        sendRequest(RequestFunction.getMessageUsers(0));
    }
}

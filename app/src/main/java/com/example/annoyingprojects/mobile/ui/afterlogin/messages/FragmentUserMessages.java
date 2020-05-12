package com.example.annoyingprojects.mobile.ui.afterlogin.messages;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.adapters.RecyclerViewAdapterUserMessages;
import com.example.annoyingprojects.data.MessageUsersModel;
import com.example.annoyingprojects.data.UserMessagesModel;
import com.example.annoyingprojects.mobile.basemodels.BaseFragment;
import com.example.annoyingprojects.repository.LocalServer;
import com.example.annoyingprojects.utilities.CheckSetup;
import com.example.annoyingprojects.utilities.FragmentUtil;
import com.example.annoyingprojects.utilities.RequestFunction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stfalcon.chatkit.messages.MessageInput;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentUserMessages extends BaseFragment implements View.OnClickListener {

    private RecyclerViewAdapterUserMessages recyclerViewAdapterUserMessages;
    private RecyclerView rv_messages;
    private MessageInput input;

    private ProgressBar progress;

    private MessageUsersModel messageUsersModel;
    ArrayList<UserMessagesModel> userMessagesModels;

    private boolean isFromMainActivity;

    public static FragmentUserMessages newInstance(Bundle args) {
        FragmentUserMessages fragmentUserMessages = new FragmentUserMessages();
        fragmentUserMessages.setArguments(args);
        return fragmentUserMessages;
    }

    @Override
    public void initViews() {
        rv_messages = containerView.findViewById(R.id.rv_messages);
        input = containerView.findViewById(R.id.input);
        progress = containerView.findViewById(R.id.progress);

        messageUsersModel = (MessageUsersModel) getArguments().getSerializable("USER_MESSAGES");
        isFromMainActivity = (Boolean) getArguments().getSerializable("IS_FROM_MAIN_ACTIVITY");

        progress.setVisibility(View.VISIBLE);
        sendRequest(RequestFunction.getUserMessages(0,
                messageUsersModel.getUsernameTo(), messageUsersModel.getUsernameFrom()));
    }

    @Override
    public void bindEvents() {
        input.getButton().setOnClickListener(this);
    }

    @Override
    public void setViews() {

    }

    @Override
    public void onBackClicked() {

        if (isFromMainActivity) {
            FragmentUtil.switchContent(R.id.fl_fragment_container, FragmentUtil.USER_PROFILE_FRAGMENT, activity, null);
        } else
            FragmentUtil.switchContent(R.id.fr_fragment_container, FragmentUtil.MESSAGE_USERS_FRAGMENT, activity, null);


    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_messages;
    }

    @Override
    public void onDataReceive(int action, List<Object> data) {
        if (action == CheckSetup.ServerActions.INSTA_COMMERCE_USER_MESSAGES) {
            Gson gson = new Gson();
            Type founderListType = new TypeToken<ArrayList<UserMessagesModel>>() {
            }.getType();

            userMessagesModels = gson.fromJson(gson.toJson(data.get(0)),
                    founderListType);

            LinearLayoutManager linearLayoutManager =
                    new LinearLayoutManager(getActivity().getBaseContext(), RecyclerView.VERTICAL, true);

            rv_messages.setLayoutManager(linearLayoutManager);
            rv_messages.setHasFixedSize(true);
            recyclerViewAdapterUserMessages = new RecyclerViewAdapterUserMessages(getContext(), userMessagesModels);
            progress.setVisibility(View.GONE);
            rv_messages.setAdapter(recyclerViewAdapterUserMessages);
        }


    }

    @Override
    public void onClick(View view) {
        UserMessagesModel userMessagesModel = new UserMessagesModel();

        Timestamp date = new Timestamp((new Date()).getTime());

        userMessagesModel.setMessage(input.getInputEditText().getText().toString());
        userMessagesModel.setUsernamFrom(messageUsersModel.getUsernameFrom());
        userMessagesModel.setUsernameTo(messageUsersModel.getUsernameTo());
        userMessagesModel.setMessageTime(date);

        recyclerViewAdapterUserMessages.addNewMessage(userMessagesModel);
        input.getInputEditText().setText("");

        LocalServer.newInstance().setSendNewMessage(true);
        sendRequest(RequestFunction.sendNewMessage(0, userMessagesModel));

    }
}

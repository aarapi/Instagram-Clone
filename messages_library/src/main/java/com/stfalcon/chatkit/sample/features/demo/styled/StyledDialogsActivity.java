package com.stfalcon.chatkit.sample.features.demo.styled;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.sample.R;
import com.stfalcon.chatkit.sample.common.data.fixtures.DialogsFixtures;
import com.stfalcon.chatkit.sample.common.data.fixtures.FixturesData;
import com.stfalcon.chatkit.sample.common.data.model.Dialog;
import com.stfalcon.chatkit.sample.common.data.model.UserMessage;
import com.stfalcon.chatkit.sample.features.demo.DemoDialogsActivity;
import com.stfalcon.chatkit.sample.utils.RequestFunctionMessage;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class StyledDialogsActivity extends DemoDialogsActivity
        implements DateFormatter.Formatter {

    public static void open(Context context) {
        context.startActivity(new Intent(context, StyledDialogsActivity.class));
    }

    private DialogsList dialogsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_styled_dialogs);

        sendRequest(RequestFunctionMessage.getMessageUsers(0));

        dialogsList = (DialogsList) findViewById(R.id.dialogsList);

    }

    @Override
    public void onDialogClick(Dialog dialog) {
        StyledMessagesActivity.open(this);
    }

    @Override
    public String format(Date date) {
        if (DateFormatter.isToday(date)) {
            return DateFormatter.format(date, DateFormatter.Template.TIME);
        } else if (DateFormatter.isYesterday(date)) {
            return getString(R.string.date_header_yesterday);
        } else if (DateFormatter.isCurrentYear(date)) {
            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH);
        } else {
            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
        }
    }

    private void initAdapter() {
        super.dialogsAdapter = new DialogsListAdapter<>(super.imageLoader);
        super.dialogsAdapter.setItems(DialogsFixtures.getDialogs());

        super.dialogsAdapter.setOnDialogClickListener(this);
        super.dialogsAdapter.setOnDialogLongClickListener(this);
        super.dialogsAdapter.setDatesFormatter(this);

        dialogsList.setAdapter(super.dialogsAdapter);
    }

    @Override
    public void onDataReceive(int action, List<Object> data) {
        Gson gson = new Gson();
        Type founderListType = new TypeToken<ArrayList<UserMessage>>(){}.getType();

        ArrayList<UserMessage> userMessages = gson.fromJson(gson.toJson(data.get(0)),
                founderListType);

        FixturesData.users = userMessages;

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initAdapter();
            }
        });

    }

}

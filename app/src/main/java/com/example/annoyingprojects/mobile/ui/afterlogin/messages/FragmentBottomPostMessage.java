package com.example.annoyingprojects.mobile.ui.afterlogin.messages;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.adapters.RecyclerViewAdapterSearch;
import com.example.annoyingprojects.data.UserModel;
import com.example.annoyingprojects.mobile.basemodels.BaseActivity;
import com.example.annoyingprojects.mobile.ui.afterlogin.userprofile.SettingFragment;
import com.example.annoyingprojects.repository.LocalServer;
import com.example.annoyingprojects.utilities.RequestFunction;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

public class FragmentBottomPostMessage extends BottomSheetDialogFragment implements RecyclerViewAdapterSearch.OnItemClickListener {
    public static final String TAG = "ActionBottomDialog";
    private SettingFragment.ItemClickListener mListener;

    private RecyclerViewAdapterSearch recyclerViewAdapterSearch;
    private RecyclerView rv_message_users;
    private View sendMessageView;

    private EditText et_message;
    private RelativeLayout btn_send;

    private ArrayList<UserModel> usersToSendMessage = new ArrayList<>();


    public static FragmentBottomPostMessage newInstance(Bundle args) {
        FragmentBottomPostMessage fragmentBottomPostMessage = new FragmentBottomPostMessage();
        fragmentBottomPostMessage.setArguments(args);
        return fragmentBottomPostMessage;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Intrinsics.checkParameterIsNotNull(inflater, "inflater");
        View view = inflater.inflate(R.layout.fragment_post_message_bottom_layout, container, false);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getContext());

        rv_message_users = view.findViewById(R.id.rv_message_users);
        rv_message_users.setLayoutManager(linearLayoutManager);
        rv_message_users.setHasFixedSize(true);

        recyclerViewAdapterSearch = new RecyclerViewAdapterSearch(getContext(), LocalServer.newInstance().getUserList(), true);
        recyclerViewAdapterSearch.SetOnItemClickListener(this);
        rv_message_users.setAdapter(recyclerViewAdapterSearch);
        recyclerViewAdapterSearch.notifyDataSetChanged();

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog == null) {
            throw new TypeCastException("null cannot be cast to non-null type com.google.android.material.bottomsheet.BottomSheetDialog");
        } else {
            final BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialog;
            bottomSheetDialog.setOnShowListener((DialogInterface.OnShowListener) (new DialogInterface.OnShowListener() {
                public final void onShow(DialogInterface it) {
                    if (it == null) {
                        throw new TypeCastException("null cannot be cast to non-null type com.google.android.material.bottomsheet.BottomSheetDialog");
                    } else {
                        final CoordinatorLayout coordinator = (CoordinatorLayout) ((BottomSheetDialog) it).findViewById(R.id.coordinator);
                        final FrameLayout containerLayout = (FrameLayout) ((BottomSheetDialog) it).findViewById(R.id.container);
                        sendMessageView = bottomSheetDialog.getLayoutInflater().inflate(R.layout.send_message_layout_button, (ViewGroup) null);
                        et_message = sendMessageView.findViewById(R.id.et_message);
                        btn_send = sendMessageView.findViewById(R.id.btn_send);
                        btn_send.setOnClickListener(onClickListener);
                        btn_send.setClickable(false);
                        Intrinsics.checkExpressionValueIsNotNull(sendMessageView, "buttons");
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.gravity = Gravity.BOTTOM;
                        sendMessageView.setLayoutParams((android.view.ViewGroup.LayoutParams) layoutParams);
                        if (containerLayout == null) {
                            Intrinsics.throwNpe();
                        }
                        containerLayout.addView(sendMessageView);
                        sendMessageView.post((Runnable) (new Runnable() {
                            public final void run() {
                                CoordinatorLayout coordinatorLayout = coordinator;
                                if (coordinatorLayout == null) {
                                    Intrinsics.throwNpe();
                                }

                                android.view.ViewGroup.LayoutParams var6 = coordinatorLayout.getLayoutParams();
                                if (var6 == null) {
                                    throw new TypeCastException("null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
                                } else {
                                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) var6;
                                    sendMessageView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                                    View view = sendMessageView;
                                    Intrinsics.checkExpressionValueIsNotNull(view, "buttons");
                                    marginLayoutParams.bottomMargin = view.getMeasuredHeight();
                                    containerLayout.requestLayout();
                                }
                            }
                        }));
                    }
                }
            }));
            return (Dialog) bottomSheetDialog;
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onRadioButtonClicked(CheckBox checkBox, int position, int selectedNr) {
        if (selectedNr == 0) {
            et_message.setVisibility(View.GONE);
            btn_send.setClickable(false);
            btn_send.setBackgroundColor(getContext().getResources().getColor(R.color.white_five));
        } else {
            et_message.setVisibility(View.VISIBLE);
            btn_send.setClickable(true);
            btn_send.setBackground(getContext().getResources().getDrawable(R.drawable.btn_send_post_message));
        }

        UserModel userModel = LocalServer.newInstance().getUserList().get(position);

        if (checkBox.isChecked()) {

            if (!usersToSendMessage.contains(userModel)) {
                usersToSendMessage.add(userModel);
            }
        } else if (!checkBox.isChecked()) {
            usersToSendMessage.remove(userModel);
        }

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == btn_send) {
                int postId = getArguments().getInt("POST_ID");
                dismiss();
                ((BaseActivity) getContext())
                        .sendRequest(RequestFunction
                                .sendPostMessage(0,
                                        usersToSendMessage,
                                        et_message.getText().toString(),
                                        postId));
            }
        }
    };
}

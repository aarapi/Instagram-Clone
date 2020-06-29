package com.example.annoyingprojects.mobile.ui.afterlogin.messages;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.annoyingprojects.data.PostModel;
import com.example.annoyingprojects.data.UserModel;
import com.example.annoyingprojects.mobile.basemodels.BaseActivity;
import com.example.annoyingprojects.mobile.ui.afterlogin.userprofile.SettingFragment;
import com.example.annoyingprojects.repository.LocalServer;
import com.example.annoyingprojects.utilities.RequestFunction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.octopepper.mediapickerinstagram.commons.models.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gujc.directtalk9.chat.ChatActivity;
import gujc.directtalk9.fragment.ChatFragment;
import gujc.directtalk9.model.User;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

public class FragmentBottomPostMessage extends BottomSheetDialogFragment implements RecyclerViewAdapterSearch.OnItemClickListener, TextWatcher {
    public static final String TAG = "ActionBottomDialog";
    private SettingFragment.ItemClickListener mListener;

    private RecyclerViewAdapterSearch recyclerViewAdapterSearch;
    private RecyclerView rv_message_users;
    private View sendMessageView;

    private EditText et_message;
    private EditText edt_search_input;
    private RelativeLayout btn_send;

    private ArrayList<UserModel> usersToSendMessage = new ArrayList<>();
    private ArrayList<UserModel> userSearched = new ArrayList<>();

    private FirebaseFirestore db;
    private String roomId;
    private String myUid;

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

        edt_search_input = view.findViewById(R.id.edt_search_input);
        edt_search_input.addTextChangedListener(this);

        rv_message_users = view.findViewById(R.id.rv_message_users);
        rv_message_users.setLayoutManager(linearLayoutManager);
        rv_message_users.setHasFixedSize(true);

        userSearched = (ArrayList<UserModel>) LocalServer.newInstance().getUserList().clone();
        recyclerViewAdapterSearch = new RecyclerViewAdapterSearch(getContext(), userSearched, true);
        recyclerViewAdapterSearch.SetOnItemClickListener(this);
        rv_message_users.setAdapter(recyclerViewAdapterSearch);
        recyclerViewAdapterSearch.notifyDataSetChanged();

        db = FirebaseFirestore.getInstance();
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

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
                if (et_message.getText().toString().isEmpty()) {
                    et_message.setError("Add a message");
                } else {
                    PostModel postModel = (PostModel) getArguments().getSerializable("POST");
                    dismiss();
                    for (int i = 0; i < usersToSendMessage.size(); i++) {
                        sendPostMessages(usersToSendMessage.get(i), postModel.getLinkImages().get(0));
                    }
                }

            }
        }
    };

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int j, int i1, int i2) {
        String searchedString = charSequence.toString();
        int size = LocalServer.newInstance().getUserList().size();
        userSearched.clear();
        ArrayList<UserModel> temp = LocalServer.newInstance().getUserList();

        for(int i = 0; i<size; i++){
            if (temp.get(i).username.contains(searchedString)){
                userSearched.add(temp.get(i));
                continue;
            }
        }

        recyclerViewAdapterSearch.notifyDataSetChanged();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void sendPostMessages(UserModel userModel, String imageUrl) {
        Query query = db.collection("users").whereEqualTo("usernm", userModel.username);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<User> list = new ArrayList<>();
                if (task.isSuccessful()) {
                    if (task.getResult().getDocuments().size() > 0) {
                        for (DocumentSnapshot document : task.getResult()) {
                            User user = document.toObject(User.class);

                            db.collection("rooms").whereGreaterThanOrEqualTo("users." + myUid, 0).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (!task.isSuccessful()) {
                                                return;
                                            }

                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Map<String, Long> users = (Map<String, Long>) document.get("users");

                                                if (users.size() == 2 & users.get(user.getUid()) != null) {
                                                    roomId = document.getId();
                                                    break;
                                                }
                                            }
                                            constructMessage(myUid, user.getUid(), imageUrl);
                                        }
                                    });

                        }
                    } else {
                    }
                }
            }
        });


    }

    void constructMessage(String myUid, String toUid, String imageUrl) {
        final Map<String, Object> messages = new HashMap<>();
        messages.put("uid", myUid);
        messages.put("msg", et_message.getText().toString());
        messages.put("msgtype", "3");
        messages.put("timestamp", FieldValue.serverTimestamp());
        messages.put("postImg", imageUrl);

        if (roomId == null) {             // create chatting room for two user
            roomId = db.collection("rooms").document().getId();
            CreateChattingRoom(db.collection("rooms").document(roomId), myUid, toUid);
        }

        final DocumentReference docRef = db.collection("rooms").document(roomId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.isSuccessful()) {
                    return;
                }

                WriteBatch batch = db.batch();

                // save last message
                batch.set(docRef, messages, SetOptions.merge());

                // save message
                List<String> readUsers = new ArrayList();
                readUsers.add(myUid);
                messages.put("readUsers", readUsers);//new String[]{myUid} );
                batch.set(docRef.collection("messages").document(), messages);

                // inc unread message count
                DocumentSnapshot document = task.getResult();
                Map<String, Long> users = (Map<String, Long>) document.get("users");

                for (String key : users.keySet()) {
                    if (!myUid.equals(key)) users.put(key, users.get(key) + 1);
                }
                document.getReference().update("users", users);

                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        }
                    }
                });
            }

        });

    }

    public void CreateChattingRoom(final DocumentReference room, String myUid, String toUid) {
        Map<String, Integer> users = new HashMap<>();
        String title = "";

        users.put(myUid, 0);
        users.put(toUid, 0);

        Map<String, Object> data = new HashMap<>();
        data.put("title", null);
        data.put("users", users);

        room.set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                }
            }
        });
    }


}

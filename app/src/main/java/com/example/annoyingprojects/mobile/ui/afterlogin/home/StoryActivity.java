package com.example.annoyingprojects.mobile.ui.afterlogin.home;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.data.StoryModel;
import com.example.annoyingprojects.data.UserModel;
import com.example.annoyingprojects.utilities.Util;
import com.example.connectionframework.requestframework.languageData.ResourceKey;
import com.example.connectionframework.requestframework.languageData.SavedInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import gujc.directtalk9.MainActivity;
import gujc.directtalk9.model.User;
import jp.shts.android.storiesprogressview.StoriesProgressView;

import static com.example.annoyingprojects.adapters.StoryRecyclerViewAdapter.SELECTED_ITEM_INFO;
import static com.example.annoyingprojects.adapters.StoryRecyclerViewAdapter.STORY_LIST_INFO;

public class StoryActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener, View.OnClickListener {

    private static int PROGRESS_COUNT;


    private StoriesProgressView storiesProgressView;
    private RelativeLayout root;

    private ImageView image;
    private CircleImageView cv_user;
    private ImageView iv_send_message;
    private EditText et_send_message;
    private TextView tv_user;

    List<StoryModel> storyModelList;
    private int counter = 0;

    long pressTime = 0L;
    long limit = 500L;
    boolean isKeyboardShowing = false;

    private FirebaseFirestore db;
    private String roomId;
    private String myUid;
    private String msg;

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    storiesProgressView.setVisibility(View.GONE);
                    findViewById(R.id.ll_user).setVisibility(View.GONE);
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    storiesProgressView.setVisibility(View.VISIBLE);
                    findViewById(R.id.ll_user).setVisibility(View.VISIBLE);
                    return limit < now - pressTime;

            }
            return false;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_stories_layout);

        db = FirebaseFirestore.getInstance();
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

// ContentView is the root view of the layout of this activity/fragment
        findViewById(android.R.id.content).getRootView().getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        Rect r = new Rect();
                        findViewById(android.R.id.content).getRootView().getWindowVisibleDisplayFrame(r);
                        int screenHeight = findViewById(android.R.id.content).getRootView().getRootView().getHeight();

                        // r.bottom is the position above soft keypad or device button.
                        // if keypad is shown, the r.bottom is smaller than that before.
                        int keypadHeight = screenHeight - r.bottom;


                        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                            // keyboard is opened
                            if (!isKeyboardShowing) {
                                isKeyboardShowing = true;
                                onKeyboardVisibilityChanged(true);
                            }
                        } else {
                            // keyboard is closed
                            if (isKeyboardShowing) {
                                isKeyboardShowing = false;
                                onKeyboardVisibilityChanged(false);
                            }
                        }
                    }
                });

        setPositionClicked();


        storiesProgressView = findViewById(R.id.stories);
        root = findViewById(R.id.root);

        storiesProgressView.setStoriesCount(PROGRESS_COUNT);
        storiesProgressView.setStoryDuration(3000L);
        storiesProgressView.setStoriesListener(this);
        storiesProgressView.startStories(counter);

        image = findViewById(R.id.image);
        iv_send_message = findViewById(R.id.iv_send_message);
        cv_user = findViewById(R.id.cv_user);
        tv_user = findViewById(R.id.tv_user);

        iv_send_message.setOnClickListener(this);
        et_send_message = findViewById(R.id.et_send_message);

        setImageResource(counter);

        // bind reverse view
        View reverse = findViewById(R.id.reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });
        reverse.setOnTouchListener(onTouchListener);

        // bind skip view
        View skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });
        skip.setOnTouchListener(onTouchListener);
    }

    void onKeyboardVisibilityChanged(boolean opened) {
        if (opened) {
            pressTime = System.currentTimeMillis();
            storiesProgressView.pause();
        } else {
            long now = System.currentTimeMillis();
            storiesProgressView.resume();
        }
    }

    @Override
    public void onNext() {
        setImageResource(++counter);
    }

    @Override
    public void onPrev() {
        if ((counter - 1) < 0) return;
        setImageResource(--counter);
    }

    @Override
    public void onComplete() {
        finish();
    }

    @Override
    protected void onDestroy() {
        // Very important !
        storiesProgressView.destroy();
        super.onDestroy();

    }
    public void setPositionClicked(){
        Bundle bundle = getIntent().getBundleExtra("data");

        ArrayList<StoryModel> list = (ArrayList) bundle.getSerializable(STORY_LIST_INFO);
        PROGRESS_COUNT = list.size();

        if (list != null && list.size() > 0) {
            storyModelList = list;
        }
        StoryModel storyModel = (StoryModel) bundle.getSerializable(SELECTED_ITEM_INFO);

        for (int i = 0; i < storyModelList.size(); i++) {
            if (storyModel.ID.equals(storyModelList.get(i).ID)) {
                counter = i;
                break;
            }
        }
    }
    public void setImageResource(int counter){
        Picasso.get().load(storyModelList.get(counter).getLink())
                .placeholder(R.drawable.placeholder_error_media).error(R.drawable.placeholder_error_media)
                .into(cv_user);
        tv_user.setText(storyModelList.get(counter).username);

        Picasso.get().load(storyModelList.get(counter).getLink())
                .placeholder(R.drawable.placeholder_error_media).error(R.drawable.placeholder_error_media)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        image.setImageBitmap(bitmap);
                        Palette.from(bitmap)
                                .generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        Palette.Swatch textSwatch = palette.getVibrantSwatch();
                                        if (textSwatch == null) {
                                            root.setBackgroundColor(getApplicationContext()
                                                    .getResources().getColor(R.color.black));
                                            return;
                                        }
                                        root.setBackgroundColor(textSwatch.getRgb());
//                                        bodyColorText.setTextColor(textSwatch.getBodyTextColor());
                                    }
                                });
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });


    }


    @Override
    public void onClick(View view) {
        if (view == iv_send_message) {
            UserModel userModel = new UserModel();
            userModel.username = storyModelList.get(counter).username;
            msg = et_send_message.getText().toString();
            Toast toast = Toast.makeText(this, "Sending message to " + userModel.username + " ...", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            sendPostMessages(userModel, storyModelList.get(counter).getLink());
            et_send_message.setText("");
        }
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
        messages.put("msg", msg);
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

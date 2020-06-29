package com.example.annoyingprojects.mobile.ui.afterlogin.home;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.data.StoryModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.shts.android.storiesprogressview.StoriesProgressView;

import static com.example.annoyingprojects.adapters.StoryRecyclerViewAdapter.SELECTED_ITEM_INFO;
import static com.example.annoyingprojects.adapters.StoryRecyclerViewAdapter.STORY_LIST_INFO;

public class StoryActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener {

    private static int PROGRESS_COUNT;


    private StoriesProgressView storiesProgressView;
    private ImageView image;
    private EditText et_send_message;

    List<StoryModel> storyModelList;
    private int counter = 0;

    long pressTime = 0L;
    long limit = 500L;

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
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


        setPositionClicked();


        storiesProgressView = findViewById(R.id.stories);
        storiesProgressView.setStoriesCount(PROGRESS_COUNT);
        storiesProgressView.setStoryDuration(3000L);
        storiesProgressView.setStoriesListener(this);
        storiesProgressView.startStories(counter);

        image = findViewById(R.id.image);
        et_send_message = findViewById(R.id.et_send_message);

        setImageResource(counter);

        et_send_message.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Toast.makeText(StoryActivity.this, "Has", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StoryActivity.this, "Lost", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                .into(image);

    }


}

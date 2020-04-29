package com.example.annoyingprojects.mobile.ui.afterlogin.userprofile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.adapters.ListViewAdapterPost;
import com.example.annoyingprojects.adapters.StoryRecyclerViewAdapter;
import com.example.annoyingprojects.data.PostModel;
import com.example.annoyingprojects.mobile.basemodels.BaseActivity;
import com.example.annoyingprojects.mobile.basemodels.BaseFragment;
import com.example.annoyingprojects.utilities.CheckSetup;

import java.util.List;

public class ActivitySinglePost extends BaseActivity {
    public static String SINGLE_POST_DATA = "SINGLE_POST_DATA";
    private ListView mainListView;
    private ListViewAdapterPost adapter;
    private ImageView iv_backbtn;
    StoryRecyclerViewAdapter mStoryRVAdapter = new StoryRecyclerViewAdapter();



    @Override
    public void initViews() {
        mainListView = findViewById(R.id.mainListView);
        iv_backbtn = findViewById(R.id.iv_backbtn);
    }

    @Override
    public void bindEvents() {
        iv_backbtn.setOnClickListener(this);

    }

    @Override
    public void setViews() {
       List<Object> data = (List<Object>) getIntent().getSerializableExtra(SINGLE_POST_DATA);
       int position = getIntent().getIntExtra("position", 0);
       setActivityView(data, position);
    }

    @Override
    public int getLayoutContent() {
        return R.layout.activity_singlepost_layout;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public int getActivityId() {
        return CheckSetup.Activities.SINGLE_POST_ACTIVITY;
    }

    public void setActivityView(List<Object> data, int position){
        List<PostModel> postModelList = (List<PostModel>) data.get(0);
        adapter = new ListViewAdapterPost(postModelList, getApplicationContext(), fragmentManager, true, mainListView);
        if (mainListView != null) {
            mainListView.setAdapter(adapter);
            mainListView.setSelection(position);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == iv_backbtn){
            finish();
        }
    }
}

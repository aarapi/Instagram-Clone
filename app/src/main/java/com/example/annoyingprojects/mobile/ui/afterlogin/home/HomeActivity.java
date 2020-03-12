package com.example.annoyingprojects.mobile.ui.afterlogin.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.annoyingprojects.R;
import com.example.annoyingprojects.mobile.basemodels.BaseActivity;
import com.example.annoyingprojects.utilities.CheckSetup;

import java.util.ArrayList;
import java.util.List;

import yalantis.com.sidemenu.util.ViewAnimator;

public class HomeActivity extends BaseActivity implements ViewAnimator.ViewAnimatorListener{
     private RecyclerView rv_stories;
    private ListView mainListView;
    private ListViewAdapterPost adapter;
    StoryRecyclerViewAdapter mStoryRVAdapter = new StoryRecyclerViewAdapter();
    private ImageView iv_search_button, cv_user_img, iv_home_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void initViews() {
        rv_stories = findViewById(R.id.rv_stories);
        mainListView = findViewById(R.id.mainListView);
        iv_search_button = findViewById(R.id.iv_search_button);
        cv_user_img = findViewById(R.id.cv_user_img);
        iv_home_button = findViewById(R.id.iv_home_button);
    }

    @Override
    public void bindEvents() {
        iv_search_button.setOnClickListener(this);
        cv_user_img.setOnClickListener(this);
        iv_home_button.setOnClickListener(this);
    }

    @Override
    public void setViews() {
        iv_home_button.setImageResource(R.drawable.ic_home_clicked);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ArrayList<StoryInfo> storyInfos = new ArrayList<>();
        for (int i =0; i<6; i++){
            StoryInfo storyInfo = new StoryInfo();
            storyInfo.ID = i+"";
            storyInfo.Name = "test";
            storyInfo.Title = "test";
            if(i%2 ==0) {
                storyInfo.setLink("https://cdn.urldecoder.org/assets/images/url-fb.png");
            }else
                storyInfo.setLink("https://i-cdn.phonearena.com/images/review/4721-image/Apple-iPhone-11-Review.jpg");

            storyInfos.add(storyInfo);
        }
        mStoryRVAdapter.setList(storyInfos);
        rv_stories.setLayoutManager(layoutManager);
        rv_stories.setAdapter(mStoryRVAdapter);
        mStoryRVAdapter.notifyDataSetChanged();

        List<PostModel> postModels = new ArrayList<>();

            PostModel postModel = new PostModel();
            postModel.setName("dtrump");
            postModel.setLinkImage("https://5.imimg.com/data5/AC/TF/MY-49552276/apple-laptop-500x500.jpg");
            postModel.setLinkUserImg("https://upload.wikimedia.org/wikipedia/commons/thumb/5/56/Donald_Trump_official_portrait.jpg/1200px-Donald_Trump_official_portrait.jpg");
            postModels.add(postModel);

        postModel = new PostModel();
        postModel.setName("aarapi");
        postModel.setLinkImage("https://ae01.alicdn.com/kf/HTB1QwDVXOwIL1JjSZFsq6AXFFXaY.jpg?size=109055&height=664&width=1000&hash=e26844beb055d804a71537690816fd65");
        postModel.setLinkUserImg("https://media-exp1.licdn.com/dms/image/C4D03AQFz1Xa99TZZmw/profile-displayphoto-shrink_200_200/0?e=1586995200&v=beta&t=Wkm1OKAc9TNV4JWpBQ0JLzlxmWwpRikgFFPCkTPkUJk");
        postModels.add(postModel);

        adapter = new ListViewAdapterPost(postModels, getApplicationContext());
        if (mainListView != null)
            mainListView.setAdapter(adapter);

        setUserImageRes("https://techcrunch.com/wp-content/uploads/2016/07/gettyimages-80751598-e1484590456411.jpg?w=730&crop=1", cv_user_img);
    }

    @Override
    public int getLayoutContent() {
        return R.layout.fragment_home;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public int getActivityId() {
        return CheckSetup.Activities.HOME_ACTIVITY;
    }

    @Override
    public void onClick(View v) {
        if (v == iv_search_button){
            iv_search_button.setImageResource(R.drawable.ic_search_clicked);
            iv_home_button.setImageResource(R.drawable.ic_home_run);
        }else if (v == iv_home_button){
            iv_home_button.setImageResource(R.drawable.ic_home_clicked);
            iv_search_button.setImageResource(R.drawable.ic_search);
        }
    }
    public void setUserImageRes(String url, ImageView view){
        Glide.with(getApplicationContext())
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade(400))
                .apply(new RequestOptions().override(Target.SIZE_ORIGINAL))
                .into(view);
    }
}
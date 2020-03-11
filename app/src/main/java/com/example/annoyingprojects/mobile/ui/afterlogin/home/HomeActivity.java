package com.example.annoyingprojects.mobile.ui.afterlogin.home;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.mobile.basemodels.BaseActivity;
import com.example.annoyingprojects.mobile.ui.beforelogin.FragmentLogIn;
import com.example.annoyingprojects.mobile.ui.beforelogin.FragmentSignUp;
import com.example.annoyingprojects.mobile.ui.beforelogin.FramgentSplashScreen;
import com.example.annoyingprojects.utilities.CheckSetup;
import com.example.annoyingprojects.utilities.RequestFunction;
import com.example.connectionframework.requestframework.languageData.SavedInformation;

import java.util.ArrayList;
import java.util.List;

import yalantis.com.sidemenu.util.ViewAnimator;

import static com.example.annoyingprojects.utilities.CheckSetup.initializeApplicationActivity;
import static java.security.AccessController.getContext;

public class HomeActivity extends BaseActivity implements ViewAnimator.ViewAnimatorListener{
     private RecyclerView rv_stories;
    private ListView mainListView;
    private PagerAdapterCustomerClaims adapter;
    StoryRecyclerViewAdapter mStoryRVAdapter = new StoryRecyclerViewAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void initViews() {
        rv_stories = findViewById(R.id.rv_stories);
        mainListView = findViewById(R.id.mainListView);
    }

    @Override
    public void bindEvents() {

    }

    @Override
    public void setViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ArrayList<StoryInfo> storyInfos = new ArrayList<>();
        for (int i =0; i<6; i++){
            StoryInfo storyInfo = new StoryInfo();
            storyInfo.ID = i+"";
            storyInfo.Name = "test";
            storyInfo.Title = "test";
            storyInfo.setLink("https://cdn.urldecoder.org/assets/images/url-fb.png");
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
        postModel.setLinkImage("https://lh3.googleusercontent.com/proxy/Xn_Ws4-0VMtEWUFczySC7ojKqeQoOQ4E9BLLJcrc-8x5UhqHluQBxsCLytza11xDiI9zAaSQjrH0_g3otLXGile5EZ7HKt9RrG9WUFBwkxw4YzdgBw");
        postModel.setLinkUserImg("https://media-exp1.licdn.com/dms/image/C4D03AQFz1Xa99TZZmw/profile-displayphoto-shrink_200_200/0?e=1586995200&v=beta&t=Wkm1OKAc9TNV4JWpBQ0JLzlxmWwpRikgFFPCkTPkUJk");
        postModels.add(postModel);

        adapter = new PagerAdapterCustomerClaims(postModels, getApplicationContext());
        if (mainListView != null)
            mainListView.setAdapter(adapter);
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
}
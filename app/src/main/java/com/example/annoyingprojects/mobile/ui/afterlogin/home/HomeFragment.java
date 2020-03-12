package com.example.annoyingprojects.mobile.ui.afterlogin.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.mobile.basemodels.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {
    public static String HOME_FRAMGENT_DATA_LIST = "HOME_FRAMGENT_DATA_LIST";
    private RecyclerView rv_stories;
    private ListView mainListView;
    private ListViewAdapterPost adapter;
    StoryRecyclerViewAdapter mStoryRVAdapter = new StoryRecyclerViewAdapter();


    public static HomeFragment newInstance(Bundle args){
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(args);
        return homeFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        bindEvents();
        setViews();
    }

    @Override
    public void initViews() {
        rv_stories = containerView.findViewById(R.id.rv_stories);
        mainListView = containerView.findViewById(R.id.mainListView);

    }

    @Override
    public void bindEvents() {

    }

    @Override
    public void setViews() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
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

        adapter = new ListViewAdapterPost(postModels, getContext());
        if (mainListView != null)
            mainListView.setAdapter(adapter);


    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_layout;
    }

}

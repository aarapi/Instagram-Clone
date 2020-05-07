package com.example.annoyingprojects.mobile.ui.afterlogin.home;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.adapters.ListViewAdapterPost;
import com.example.annoyingprojects.adapters.StoryRecyclerViewAdapter;
import com.example.annoyingprojects.data.PostModel;
import com.example.annoyingprojects.data.Posts;
import com.example.annoyingprojects.data.StoryInfo;
import com.example.annoyingprojects.mobile.basemodels.BaseFragment;
import com.example.annoyingprojects.utilities.CheckSetup;
import com.example.annoyingprojects.utilities.RequestFunction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.annoyingprojects.mobile.ui.afterlogin.home.HomeActivity.scrollTime;

public class HomeFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    public static String HOME_FRAMGENT_DATA_LIST = "HOME_FRAMGENT_DATA_LIST";
    private int postNumbers;

    private List<PostModel> postModelList;
    StoryRecyclerViewAdapter mStoryRVAdapter = new StoryRecyclerViewAdapter();
    private RecyclerView rv_stories;

    private ListView mainListView;
    private ListViewAdapterPost adapter;

    private ImageView iv_send_message;
    private ImageView iv_upload;

    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout rl_upload_post;
    private ProgressBar progress;


    public static HomeFragment newInstance(Bundle args){
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(args);
        return homeFragment;
    }


    @Override
    public void initViews() {
        rv_stories = containerView.findViewById(R.id.rv_stories);
        mainListView = containerView.findViewById(R.id.mainListView);

        iv_send_message = containerView.findViewById(R.id.iv_send_message);
        iv_upload = containerView.findViewById(R.id.iv_upload);

        swipeRefreshLayout = containerView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        rl_upload_post = containerView.findViewById(R.id.rl_upload_post);
        progress = containerView.findViewById(R.id.progress);


    }

    @Override
    public void bindEvents() {
        iv_send_message.setOnClickListener(this);
    }

    @Override
    public void setViews() {
        List<Object> homeData = (List<Object>) getArguments().getSerializable(HOME_FRAMGENT_DATA_LIST);
        setFragmentView(homeData);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_layout;
    }

    @Override
    public void setFragmentView(List<Object> data) {

        ArrayList<StoryInfo> storyInfoArrayList = (ArrayList<StoryInfo>) data.get(0);
        postModelList = (List<PostModel>) data.get(1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        mStoryRVAdapter.setList(storyInfoArrayList);
        rv_stories.setLayoutManager(layoutManager);
        rv_stories.setAdapter(mStoryRVAdapter);
        mStoryRVAdapter.notifyDataSetChanged();



        adapter = new ListViewAdapterPost(postModelList, getContext(), getParentFragmentManager(),
                false, mainListView);
        if (mainListView != null)
            mainListView.setAdapter(adapter);

        adapter.setOnLoadMoreListener(() -> {
            adapter.getDataSet().add(null);
            adapter.notifyDataSetChanged();
            sendRequest(RequestFunction.getPostData(0, scrollTime));
            scrollTime++;
        });

    }

    @Override
    public void onClick(View view) {
        if (view == iv_send_message){
            startActivity(CheckSetup.Activities.MESSAGES_ACTIVITY);
        }
    }

    @Override
    public void onDataReceive(int action, List<Object> data) {
        Gson gson = new Gson();
        if (action == CheckSetup.ServerActions.INSTA_COMMERCE_HOME_DATA){

            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
                adapter.getDataSet().clear();
                scrollTime = 1;
            }
            Type founderListType = new TypeToken<ArrayList<Posts>>(){}.getType();

            ArrayList<Posts> posts = gson.fromJson(gson.toJson(data.get(0)),
                    founderListType);

            adapter.setData(posts);
            if (posts.size() > 0) {
                adapter.isLoading = false;
            }


            adapter.notifyDataSetChanged();
        } else if (action == CheckSetup.ServerActions.INSTA_COMMERCE_CREATE_NEW_POST) {
            Posts posts = gson.fromJson(gson.toJson(data.get(0)), Posts.class);
            posts.setLinkUserImg(((HomeActivity) getContext()).getUser().userImage);
            adapter.createNewPost(posts);
            ((HomeActivity) getContext()).getBitmapTask().onProgressUpdate("100");
            rl_upload_post.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onRefresh() {
        sendRequest(RequestFunction.getPostData(0, 0));
    }


    public LinearLayout getRl_upload_post() {
        return rl_upload_post;
    }

    public ImageView getIv_upload() {
        return iv_upload;
    }

    public ProgressBar getProgress() {
        return progress;
    }
}

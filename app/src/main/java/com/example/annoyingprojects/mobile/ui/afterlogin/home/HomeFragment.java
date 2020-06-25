package com.example.annoyingprojects.mobile.ui.afterlogin.home;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.baoyz.widget.PullRefreshLayout;
import com.example.annoyingprojects.R;
import com.example.annoyingprojects.adapters.ListViewAdapterPost;
import com.example.annoyingprojects.adapters.StoryRecyclerViewAdapter;
import com.example.annoyingprojects.data.FilterModel;
import com.example.annoyingprojects.data.PostModel;
import com.example.annoyingprojects.data.Posts;
import com.example.annoyingprojects.data.StoryModel;
import com.example.annoyingprojects.mobile.basemodels.BaseFragment;
import com.example.annoyingprojects.utilities.CheckSetup;
import com.example.annoyingprojects.utilities.RequestFunction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


import gujc.directtalk9.MainActivity;
import gujc.directtalk9.SplashActivity;

import static com.example.annoyingprojects.mobile.ui.afterlogin.home.FilterDialogFragment.FILTER_DIALOG_FRAGMENT;
import static com.example.annoyingprojects.mobile.ui.afterlogin.home.HomeActivity.scrollTime;

public class HomeFragment extends BaseFragment implements View.OnClickListener, PullRefreshLayout.OnRefreshListener, MaterialSearchView.OnQueryTextListener, MaterialSearchView.SearchViewListener {
    public static String HOME_FRAMGENT_DATA_LIST = "HOME_FRAMGENT_DATA_LIST";
    private int postNumbers;

    private List<PostModel> postModelList;
    StoryRecyclerViewAdapter mStoryRVAdapter = new StoryRecyclerViewAdapter();
    private RecyclerView rv_stories;

    private ListView mainListView;
    private ListViewAdapterPost adapter;

    private ImageView iv_send_message;
    private ImageView iv_upload;
    private ImageView iv_filter;

    private MaterialSearchView searchView;

    private PullRefreshLayout swipeRefreshLayout;
    private LinearLayout rl_upload_post;
    private ProgressBar progress;

    private boolean isLoading = false;

    private String searchString = "";

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
        iv_filter = containerView.findViewById(R.id.iv_filter);

        searchView = containerView.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(this);
        searchView.setOnSearchViewListener(this);

        swipeRefreshLayout = containerView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setEnabled(true);
        rl_upload_post = containerView.findViewById(R.id.rl_upload_post);
        progress = containerView.findViewById(R.id.progress);


    }

    @Override
    public void bindEvents() {
        iv_send_message.setOnClickListener(this);
        iv_filter.setOnClickListener(this::onClick);
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

        ArrayList<StoryModel> storyModelArrayList = (ArrayList<StoryModel>) data.get(0);
        postModelList = (List<PostModel>) data.get(1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        mStoryRVAdapter.setList(storyModelArrayList);
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
            sendRequest(RequestFunction.getPostData(0, scrollTime, FilterModel.newInstance(),
                    searchString));
            scrollTime++;
        });

    }

    @Override
    public void onClick(View view) {
        if (view == iv_send_message){
            Intent intent = new Intent(getActivity(), MainActivity.class);
            getActivity().startActivity(intent);
        }else if (view == iv_filter){
            getParentFragmentManager();
                    FilterDialogFragment filterDialogFragment = new FilterDialogFragment();
                    filterDialogFragment.show(getParentFragmentManager(), FILTER_DIALOG_FRAGMENT);
            searchView.showSearch();


        }
    }

    @Override
    public void onDataReceive(int action, List<Object> data) {
        Gson gson = new Gson();
        if (action == CheckSetup.ServerActions.INSTA_COMMERCE_HOME_DATA){

            if (isLoading) {
                swipeRefreshLayout.setRefreshing(false);
                adapter.getDataSet().clear();
                scrollTime = 1;
                isLoading = false;
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
            posts.setLinkUserImg(((HomeActivity) getContext()).getUserModel().userImage);
            adapter.createNewPost(posts);
            ((HomeActivity) getContext()).getBitmapTask().onProgressUpdate("100");
            rl_upload_post.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onErrorDataReceive(int action, List<Object> data) {
        rl_upload_post.setVisibility(View.GONE);

            swipeRefreshLayout.setRefreshing(false);
            isLoading = false;

        Toast toast = Toast.makeText(activity, "Couldn't refresh feed", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    @Override
    public void onRefresh() {
        isLoading = true;
        sendRequest(RequestFunction.getPostData(0, 0,
                FilterModel.newInstance(), searchString));
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

    public PullRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (searchView.isSearchOpen()) {
            searchString = newText;
            if (!isLoading) {
                swipeRefreshLayout.setRefreshing(true);
            }
            onRefresh();
        }
        return false;
    }

    @Override
    public void onSearchViewShown() {

    }

    @Override
    public void onSearchViewClosed() {
        if (!searchString.equals("")) {
            searchString = "";
            onRefresh();
        }
    }
}

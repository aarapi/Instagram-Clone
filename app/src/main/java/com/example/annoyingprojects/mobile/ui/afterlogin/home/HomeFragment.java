package com.example.annoyingprojects.mobile.ui.afterlogin.home;

import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.stfalcon.chatkit.sample.features.demo.styled.StyledDialogsActivity;
import com.stfalcon.chatkit.sample.features.demo.styled.StyledMessagesActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.annoyingprojects.mobile.ui.afterlogin.home.HomeActivity.scrollTime;

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    public static String HOME_FRAMGENT_DATA_LIST = "HOME_FRAMGENT_DATA_LIST";
    private RecyclerView rv_stories;
    private ListView mainListView;
    private ListViewAdapterPost adapter;
    private ImageView iv_send_message;
    private int postNumbers;
    StoryRecyclerViewAdapter mStoryRVAdapter = new StoryRecyclerViewAdapter();
    private List<PostModel> postModelList;


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

        List<Object> homeData = (List<Object>) getArguments().getSerializable(HOME_FRAMGENT_DATA_LIST);
        setFragmentView(homeData);

    }

    @Override
    public void initViews() {
        rv_stories = containerView.findViewById(R.id.rv_stories);
        mainListView = containerView.findViewById(R.id.mainListView);
        iv_send_message = containerView.findViewById(R.id.iv_send_message);


    }

    @Override
    public void bindEvents() {
        iv_send_message.setOnClickListener(this);
    }

    @Override
    public void setViews() {
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
        });

    }

    @Override
    public void onClick(View view) {
        if (view == iv_send_message){
            StyledDialogsActivity.open(getContext());
        }
    }

    @Override
    public void onDataReceive(int action, List<Object> data) {
        if (action == CheckSetup.ServerActions.INSTA_COMMERCE_HOME_DATA){

            Gson gson = new Gson();
            Type founderListType = new TypeToken<ArrayList<Posts>>(){}.getType();

            ArrayList<Posts> posts = gson.fromJson(gson.toJson(data.get(0)),
                    founderListType);

            adapter.setData(posts);
            adapter.isLoading = false;
            ((HomeActivity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });

        }
    }
}

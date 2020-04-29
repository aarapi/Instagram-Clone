package com.example.annoyingprojects.mobile.ui.afterlogin.userprofile;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.adapters.GridAdapter;
import com.example.annoyingprojects.data.Posts;
import com.example.annoyingprojects.data.User;
import com.example.annoyingprojects.mobile.basemodels.BaseFragment;
import com.example.annoyingprojects.data.PostModel;
import com.example.annoyingprojects.mobile.ui.afterlogin.home.HomeActivity;
import com.example.annoyingprojects.repository.LocalServer;
import com.example.annoyingprojects.utilities.RequestFunction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.annoyingprojects.utilities.Util.setUserImageRes;

public class UserProfileFragment extends BaseFragment implements View.OnClickListener {

    private GridView gv_user_post;
    private GridAdapter gridAdapter;
    private ProgressBar progressBar;
    private ImageView iv_user_profile, iv_menu_settings;
    public static String USER_PROFILE_DATA = "USER_PROFILE_DATA";
    private static FragmentManager fragmentManager;
    private User user;
    private RelativeLayout rl_edit_profile;

    public static UserProfileFragment newInstance(Bundle args, FragmentManager fragmentManagerSupport){
        UserProfileFragment userProfileFragment = new UserProfileFragment();
        userProfileFragment.setArguments(args);
        fragmentManager = fragmentManagerSupport;
        return userProfileFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        user = LocalServer.getInstance(getContext()).getUser();
        initViews();
        bindEvents();
        setViews();

        progressBar.setVisibility(View.VISIBLE);
        sendRequest(RequestFunction.getUserProfileData(0, user.username));

    }

    @Override
    public void initViews() {
        iv_user_profile = containerView.findViewById(R.id.iv_user_profile);
        gv_user_post = containerView.findViewById(R.id.gv_user_post);
        iv_menu_settings = containerView.findViewById(R.id.iv_menu_settings);
        progressBar = containerView.findViewById(R.id.progressbar);
        rl_edit_profile = containerView.findViewById(R.id.rl_edit_profile);
    }

    @Override
    public void bindEvents() {
        iv_menu_settings.setOnClickListener(this::onClick);
        rl_edit_profile.setOnClickListener(this::onClick);
    }

    @Override
    public void setViews() {
        setUserImageRes(getContext(), user.userImage, iv_user_profile);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_userprofile_layout;
    }

    @Override
    public void onDataReceive(int action, List<Object> data) {
        List<Object> profileData = new ArrayList<>();
        profileData.add(getUserPosts(data));

        ((HomeActivity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                setFragmentView(profileData);
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v == iv_menu_settings){
            showBottomSheet();
        }
    }

    @Override
    public void setFragmentView(List<Object> data) {
        List<PostModel> postModels = (List<PostModel>) data.get(0);
        gridAdapter = new GridAdapter(getContext(), postModels, fragmentManager);
        gv_user_post.setAdapter(gridAdapter);
    }


    public void showBottomSheet() {
        SettingFragment addPhotoBottomDialogFragment =
                SettingFragment.newInstance();
        addPhotoBottomDialogFragment.show(getFragmentManager(),
                SettingFragment.TAG);
    }


    private List<PostModel> getUserPosts(List<Object> data){
        Gson gson = new Gson();
        Type postsType = new TypeToken<ArrayList<Posts>>() {}.getType();
        ArrayList<Posts> posts = gson.fromJson(gson.toJson(data.get(0)),postsType);

        ArrayList<PostModel> postModels = new ArrayList<>();
        int postsSize = posts.size();
        for (int i = 0; i < postsSize; i++) {
            PostModel postModel = posts.get(i).getPostModel();
            postModel.setLinkUserImg(posts.get(i).getLinkUserImg());
            postModel.setLinkImages(posts.get(i).getLinkImages());

            postModels.add(postModel);

        }

        return postModels;
    }

}

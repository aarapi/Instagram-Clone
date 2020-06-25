package com.example.annoyingprojects.mobile.ui.afterlogin.userprofile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.adapters.GridAdapter;
import com.example.annoyingprojects.data.MessageUsersModel;
import com.example.annoyingprojects.data.Posts;
import com.example.annoyingprojects.data.UserModel;
import com.example.annoyingprojects.mobile.basemodels.BaseFragment;
import com.example.annoyingprojects.data.PostModel;
import com.example.annoyingprojects.mobile.ui.afterlogin.home.HomeActivity;
import com.example.annoyingprojects.mobile.ui.afterlogin.messages.FragmentUserMessages;
import com.example.annoyingprojects.repository.LocalServer;
import com.example.annoyingprojects.utilities.FragmentUtil;
import com.example.annoyingprojects.utilities.RequestFunction;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.annoyingprojects.utilities.Util.setUserImageResPicasso;

public class UserProfileFragment extends BaseFragment implements View.OnClickListener {
    public static String USER_PROFILE_DATA = "USER_PROFILE_DATA";
    private UserModel userModel;
    private List<PostModel> postModels;

    private GridView gv_user_post;
    private GridAdapter gridAdapter;
    private ProgressBar progressBar;
    private RelativeLayout rl_edit_profile;

    private CircleImageView iv_user_profile;
    private TextView tv_log_out;

    private TextView tv_posts_value;
    private TextView tv_email;
    private TextView tv_username;
    private TextView tv_user_action;
    private TextView tv_phone;
    private TextView tv_posts;

    private boolean isUser = true;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private AppBarConfiguration mAppBarConfiguration;

    public static UserProfileFragment newInstance(Bundle args) {
        UserProfileFragment userProfileFragment = new UserProfileFragment();
        userProfileFragment.setArguments(args);
        return userProfileFragment;
    }

    @Override
    public void initViews() {
        rl_edit_profile = containerView.findViewById(R.id.rl_edit_profile);
        tv_user_action= containerView.findViewById(R.id.tv_user_action);

        drawer = containerView.findViewById(R.id.drawer_layout);
        navigationView = containerView.findViewById(R.id.nav_view);

        if (getArguments() != null) {
            userModel = (UserModel) getArguments().getSerializable(USER_PROFILE_DATA);
        } else {
            userModel = LocalServer.getInstance(getContext()).getUser();
        }

        if (!userModel.username.equals(LocalServer
                .getInstance(getContext()).getUser().username)) {
            tv_user_action.setText("Send Message");
            isUser = false;
        }

        progressBar = containerView.findViewById(R.id.progressbar);

        iv_user_profile = containerView.findViewById(R.id.iv_user_profile);
        gv_user_post = containerView.findViewById(R.id.gv_user_post);
        tv_log_out = containerView.findViewById(R.id.iv_menu_settings);

        tv_posts_value = containerView.findViewById(R.id.tv_posts_value);
        tv_email = containerView.findViewById(R.id.tv_email);
        tv_username = containerView.findViewById(R.id.tv_username);
        tv_phone = containerView.findViewById(R.id.tv_phone);
        tv_posts = containerView.findViewById(R.id.tv_posts);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();

    }

    @Override
    public void bindEvents() {
        tv_log_out.setOnClickListener(this::onClick);
        rl_edit_profile.setOnClickListener(this::onClick);
        tv_phone.setOnClickListener(this::onClick);
    }

    @Override
    public void setViews() {
        setUserImageResPicasso(getContext(), userModel.userImage, iv_user_profile);
        tv_email.setText(userModel.email);
        tv_username.setText(userModel.username);
        tv_phone.setText(userModel.phoneNumber);

        progressBar.setVisibility(View.VISIBLE);
        sendRequest(RequestFunction.getUserProfileData(0, userModel.username));
    }

    @Override
    public int getLayoutId() {
        return R.layout.settings_drawer_menu_layout;
    }

    @Override
    public void onDataReceive(int action, List<Object> data) {
        List<Object> profileData = new ArrayList<>();
        List<PostModel> postModels = getUserPosts(data);
        profileData.add(postModels);
        tv_posts.setVisibility(View.VISIBLE);
        if (!isUser) {
            Gson gson = new Gson();
            UserModel user = gson.fromJson(gson.toJson(data.get(1)), UserModel.class);
            tv_email.setText(user.email);
            tv_username.setText(user.username);
            tv_phone.setText(user.phoneNumber);
            userModel.phoneNumber = user.phoneNumber;
            tv_phone.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
        tv_posts_value.setText(postModels.size() + "");

        setFragmentView(profileData);
    }

    @Override
    public void onErrorDataReceive(int action, List<Object> data) {
        progressBar.setVisibility(View.GONE);
        Toast toast = Toast.makeText(activity, "Couldn't retrieve data", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onClick(View v) {
        if (v == tv_log_out) {
            SharedPreferences preferences = getContext().getSharedPreferences("PREFERENCE_NAME",
                    Context.MODE_PRIVATE);
            preferences.edit().remove("user").commit();
            getActivity().finish();

        }else if (v == rl_edit_profile){
            if (isUser){
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                FragmentUtil.switchFragmentWithAnimation(R.id.fl_fragment_container,
                        editProfileFragment,
                        getActivity(),
                        FragmentUtil.EDIT_PROFILE_FRAGMENT,
                        FragmentUtil.AnimationType.SLIDE_UP
                );
            }else {
                MessageUsersModel messageUsersModel = new MessageUsersModel();
                messageUsersModel.setUsername_from(LocalServer.getInstance(getContext()).getUser().username);
                messageUsersModel.setUsername_to(userModel.username);
                Bundle args = new Bundle();
                args.putSerializable("USER_MESSAGES", (Serializable) messageUsersModel);
                args.putSerializable("IS_FROM_MAIN_ACTIVITY", (Serializable) true);
                FragmentUserMessages fragmentUserMessages = FragmentUserMessages.newInstance(args);

                FragmentUtil.switchFragmentWithAnimation(R.id.fl_fragment_container
                        , fragmentUserMessages
                        , activity
                        ,FragmentUtil.USER_MESSAGES_FRAGMENT
                        ,null);
            }
        } else if (v == tv_phone) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", userModel.phoneNumber, null));
            startActivity(intent);
        }
    }

    @Override
    public void setFragmentView(List<Object> data) {
        postModels = (List<PostModel>) data.get(0);
        gridAdapter = new GridAdapter(getContext(), postModels);
        gv_user_post.setAdapter(gridAdapter);
    }


    public void showBottomSheet() {
        SettingFragment addPhotoBottomDialogFragment =
                SettingFragment.newInstance();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        addPhotoBottomDialogFragment.show(fragmentManager,
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

    @Override
    public void onBackClicked() {
        FragmentUtil
                .switchContent(R.id.fl_fragment_container,
                        FragmentUtil.HOME_FRAGMENT,
                        (HomeActivity) getContext(),
                        null);
        ((HomeActivity) getContext()).setHomeIcon();
    }

    public List<PostModel> getPostModels() {
        return postModels;
    }

    public void updateUserPosts(List<PostModel> modelList) {
        gridAdapter.updatePosts(modelList);
    }

    public CircleImageView getIv_user_profile(){return iv_user_profile;}
    public TextView getTv_posts_value() { return tv_posts_value;}
    public TextView getTv_email(){return tv_email;}
    public TextView getTv_username(){return tv_username;}
}

package com.example.annoyingprojects.mobile.ui.afterlogin.userprofile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.annoyingprojects.utilities.CheckSetup;
import com.example.annoyingprojects.utilities.FragmentUtil;
import com.example.annoyingprojects.utilities.RequestFunction;
import com.example.connectionframework.requestframework.languageData.SavedInformation;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import gujc.directtalk9.chat.ChatActivity;
import gujc.directtalk9.model.User;

import static com.example.annoyingprojects.utilities.Util.setUserImageResPicasso;

public class UserProfileFragment extends BaseFragment implements View.OnClickListener {
    public static String USER_PROFILE_DATA = "USER_PROFILE_DATA";
    private UserModel userModel;
    private List<PostModel> postModels;

    private GridView gv_user_post;
    private GridAdapter gridAdapter;
    private ProgressBar progressBar;
    private RelativeLayout rl_edit_profile;
    private ProgressBar loading_bar;
    private FrameLayout shape_layout;

    private CircleImageView iv_user_profile;

    private TextView tv_posts_value;
    private TextView tv_email;
    private TextView tv_username;
    private TextView tv_user_action;
    private TextView tv_phone;
    private TextView tv_posts;
    private TextView tv_log_out;

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
        shape_layout = containerView.findViewById(R.id.shape_layout);
        tv_user_action= containerView.findViewById(R.id.tv_user_action);

        drawer = containerView.findViewById(R.id.drawer_layout);
        navigationView = containerView.findViewById(R.id.nav_view);
        loading_bar = containerView.findViewById(R.id.loading_bar);

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
        tv_phone.setOnClickListener(this::onClick);
        rl_edit_profile.setOnClickListener(this::onClick);
        shape_layout.setOnClickListener(this::onClick);
    }

    @Override
    public void setViews() {
        setUserImageResPicasso(userModel.userImage, iv_user_profile);
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
        if (action == CheckSetup.ServerActions.INSTA_COMMERCE_LOG_OUT) {
            getActivity().finish();
        } else {
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
    }

    @Override
    public void onErrorDataReceive(int action, List<Object> data) {
        if (action == CheckSetup.ServerActions.INSTA_COMMERCE_LOG_OUT) {
            tv_log_out.setVisibility(View.VISIBLE);
            loading_bar.setVisibility(View.GONE);
            Toast toast = Toast.makeText(activity, (String) data.get(0), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            progressBar.setVisibility(View.GONE);
            Toast toast = Toast.makeText(activity, "Couldn't retrieve data", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == tv_log_out) {
            FirebaseAuth.getInstance().signOut();

            SharedPreferences preferences = getContext().getSharedPreferences("PREFERENCE_NAME",
                    Context.MODE_PRIVATE);

            preferences.edit().remove("user").commit();


            tv_log_out.setVisibility(View.GONE);
            loading_bar.setVisibility(View.VISIBLE);
            sendRequest(RequestFunction.logOut(0,
                    SavedInformation.getInstance().getPreferenceData(getContext(), "USER_TOKEN")));
            preferences.edit().remove("USER_TOKEN").commit();

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
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Query query = db.collection("users").whereEqualTo("usernm", userModel.username);

                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<User> list = new ArrayList<>();
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getContext(), ChatActivity.class);
                            if (task.getResult().getDocuments().size() > 0) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    User user = document.toObject(User.class);
                                    intent.putExtra("toUid", user.getUid());
                                    startActivity(intent);
                                }
                            } else {
                                intent.putExtra("roomID", "");
                                intent.putExtra("roomTitle", userModel.username);
                                startActivity(intent);
                            }
                        }
                    }
                });

            }
        } else if (v == tv_phone) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", userModel.phoneNumber, null));
            startActivity(intent);
        } else if (v == shape_layout) {
            ImagePicker.Companion.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)        //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        }
    }

    @Override
    public void setFragmentView(List<Object> data) {
        postModels = (List<PostModel>) data.get(0);
        gridAdapter = new GridAdapter(getContext(), postModels);
        gv_user_post.setAdapter(gridAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Bundle args = new Bundle();
            args.putSerializable("SELECTED_IMAGE", data.getData().getEncodedPath());

            AddUserStoryFragment addUserStoryFragment = AddUserStoryFragment.newInstance(args);
            FragmentUtil.switchFragmentWithAnimation(R.id.fl_fragment_container,
                    addUserStoryFragment,
                    getActivity(),
                    FragmentUtil.ADD_STORY_FRAGMENT,
                    FragmentUtil.AnimationType.SLIDE_UP
            );
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getContext(), "Error on picker.", Toast.LENGTH_SHORT).show();
            onBackClicked();
        } else {
            Toast.makeText(getContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
            onBackClicked();
        }
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


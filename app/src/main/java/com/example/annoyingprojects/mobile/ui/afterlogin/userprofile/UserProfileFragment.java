package com.example.annoyingprojects.mobile.ui.afterlogin.userprofile;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.mobile.basemodels.BaseFragment;
import com.example.annoyingprojects.mobile.ui.afterlogin.home.PostModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.annoyingprojects.utilities.Util.setUserImageRes;

public class UserProfileFragment extends BaseFragment implements View.OnClickListener {

    private GridView gv_user_post;
    private GridAdapter gridAdapter;
    private ImageView iv_user_profile, iv_menu_settings;
    public static String USER_PROFILE_DATA = "USER_PROFILE_DATA";

    public static UserProfileFragment newInstance(Bundle args){
        UserProfileFragment userProfileFragment = new UserProfileFragment();
        userProfileFragment.setArguments(args);
        return userProfileFragment;
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
        iv_user_profile = containerView.findViewById(R.id.iv_user_profile);
        gv_user_post = containerView.findViewById(R.id.gv_user_post);
        iv_menu_settings = containerView.findViewById(R.id.iv_menu_settings);
    }

    @Override
    public void bindEvents() {
        iv_menu_settings.setOnClickListener(this);
    }

    @Override
    public void setViews() {
        setUserImageRes(getContext(), "https://techcrunch.com/wp-content/uploads/2016/07/gettyimages-80751598-e1484590456411.jpg?w=730&crop=1", iv_user_profile);

        List<PostModel> postModels = new ArrayList<>();

        for(int i = 0; i<20; i++) {
            PostModel postModel = new PostModel();
            if (i%2 == 0) {
                postModel.setLinkImage("https://www.kbb.com/articles/wp-content/uploads/2019/10/20-2019-hyundai-elantra-sedan-oem.jpg");
            }else
                postModel.setLinkImage("https://images.complex.com/complex/image/upload/c_fill,dpr_auto,f_auto,fl_lossy,g_face,q_auto,w_1280/agk5vpa12obus75lddae.jpg");
            postModels.add(postModel);
        }
        gridAdapter = new GridAdapter(getContext(), postModels);
        gv_user_post.setAdapter(gridAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_userprofile_layout;
    }

    @Override
    public void onClick(View v) {
        if(v == iv_menu_settings){
            showBottomSheet();
        }
    }

    public void showBottomSheet() {
        SettingFragment addPhotoBottomDialogFragment =
                SettingFragment.newInstance();
        addPhotoBottomDialogFragment.show(getFragmentManager(),
                SettingFragment.TAG);
    }

}

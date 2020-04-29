package com.example.annoyingprojects.mobile.ui.afterlogin.userprofile;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.annoyingprojects.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MoreBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private RelativeLayout rl_cancel, rl_copy_link;
    private RelativeLayout rl_report_home, rl_unfollow_home, rl_delete_user, rl_edit_user;
    private boolean isUserPost;

    public static MoreBottomSheetFragment newInstance(Bundle args) {
        MoreBottomSheetFragment fragment = new MoreBottomSheetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        isUserPost = (Boolean) getArguments().getSerializable("isUserPost");
        View contentView = null;
        if (isUserPost) {
             contentView = View.inflate(getContext(), R.layout.more_bottomsheet_userpost_layout, null);
            dialog.setContentView(contentView);
            ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
            rl_delete_user = (RelativeLayout) contentView.findViewById(R.id.rl_delete);
            rl_edit_user = (RelativeLayout) contentView.findViewById(R.id.rl_edit);

        }else {
            contentView = View.inflate(getContext(), R.layout.more_bottomsheet_homeposts_layout, null);
            dialog.setContentView(contentView);
            ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
            rl_report_home = (RelativeLayout) contentView.findViewById(R.id.rl_report);
            rl_unfollow_home = (RelativeLayout) contentView.findViewById(R.id.rl_unfollow);
        }
        rl_cancel = (RelativeLayout) contentView.findViewById(R.id.rl_cancel);
        rl_copy_link = (RelativeLayout) contentView.findViewById(R.id.rl_copy_link);

        bindViews(isUserPost);

    }


    private void bindViews(boolean isUserPost){
        if (isUserPost){
            rl_delete_user.setOnClickListener(this);
            rl_edit_user.setOnClickListener(this);
        }else {
            rl_report_home.setOnClickListener(this);
            rl_unfollow_home.setOnClickListener(this);
        }
        rl_cancel.setOnClickListener(this);
        rl_copy_link.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

         if (view == rl_copy_link){
            Toast.makeText(getContext(), "Link Copied", Toast.LENGTH_SHORT).show();
        }

        if(isUserPost){
            if (view == rl_delete_user){
                Toast.makeText(getContext(), "Post Deleted", Toast.LENGTH_SHORT).show();
            }else if (view == rl_edit_user){
                Toast.makeText(getContext(), "Post Edited", Toast.LENGTH_SHORT).show();
            }

        }else {
            if (view == rl_report_home){
                Toast.makeText(getContext(), "Post Reported", Toast.LENGTH_SHORT).show();
            }else if(view == rl_unfollow_home){
                Toast.makeText(getContext(), "User Unfollowed", Toast.LENGTH_SHORT).show();
            }

        }

        dismiss();


    }
}
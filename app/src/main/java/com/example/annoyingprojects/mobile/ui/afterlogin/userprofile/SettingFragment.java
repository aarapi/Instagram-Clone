package com.example.annoyingprojects.mobile.ui.afterlogin.userprofile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.annoyingprojects.R;
import com.example.connectionframework.requestframework.languageData.SavedInformation;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SettingFragment extends BottomSheetDialogFragment
        implements View.OnClickListener {
    public static final String TAG = "ActionBottomDialog";
    private ItemClickListener mListener;

    private LinearLayout ll_logout;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings_bottom_layout, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ll_logout = (LinearLayout) view.findViewById(R.id.ll_logout);
        ll_logout.setOnClickListener(this::onClick);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemClickListener) {
            mListener = (ItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ItemClickListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override public void onClick(View view) {
        if (view == ll_logout){
            SharedPreferences preferences = getContext().getSharedPreferences("PREFERENCE_NAME",
                    Context.MODE_PRIVATE);
            preferences.edit().remove("user").commit();
            getActivity().finish();
        }
    }
    public interface ItemClickListener {
        void onItemClick(String item);
    }
}
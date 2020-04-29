package com.example.annoyingprojects.mobile.ui.afterlogin.messages;

import android.app.Activity;
import android.graphics.Point;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.adapters.ListViewAdapterMessages;
import com.example.annoyingprojects.adapters.ListViewAdapterPost;
import com.example.annoyingprojects.mobile.basemodels.BaseActivity;
import com.example.annoyingprojects.utilities.CheckSetup;
import com.google.android.material.navigation.NavigationView;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.List;


public class MessagesActivity extends BaseActivity implements  MaterialSearchBar.OnSearchActionListener {

    @Override
    public void initViews() {

    }

    @Override
    public void bindEvents() {

    }

    @Override
    public void setViews() {

    }



    @Override
    public int getLayoutContent() {
        return R.layout.message_chat_view;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public int getActivityId() {
        return CheckSetup.Activities.MESSAGES_ACTIVITY;
    }


    @Override
    public void onSearchStateChanged(boolean enabled) {
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }
}

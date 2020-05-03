package com.example.annoyingprojects.mobile.basemodels;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.appconfiguration.ApplicationActivity;
import com.example.annoyingprojects.mobile.ui.afterlogin.personaldata.FragmentMenu;
import com.example.annoyingprojects.utilities.CheckSetup;
import com.example.connectionframework.requestframework.receiver.RequestReceived;
import com.example.connectionframework.requestframework.sender.Request;
import com.example.connectionframework.requestframework.sender.SenderBridge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import yalantis.com.sidemenu.interfaces.Resourceble;
import yalantis.com.sidemenu.interfaces.ScreenShotable;
import yalantis.com.sidemenu.model.SlideMenuItem;
import yalantis.com.sidemenu.util.ViewAnimator;

public abstract class BaseActivity extends AppCompatActivity implements
        ViewAnimator.ViewAnimatorListener,
        View.OnClickListener {
    public FragmentManager fragmentManager;
    private SenderBridge senderBridge;
    protected BaseActivity activity;
    private ApplicationActivity applicationActivity;
    private List<SlideMenuItem> list = new ArrayList<>();
    private ViewAnimator viewAnimator;
    protected DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private LinearLayout linearLayout;
    private ImageView iv_menu;
    boolean isCreated = true;

    private static final long MOVE_DEFAULT_TIME = 700;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this;
        fragmentManager = getSupportFragmentManager();
        setContentView(getLayoutContent());

        RequestReceived requestReceived = new RequestReceived() {
            @Override
            public void onRequestReceived(int p_action, List<Object> data) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onDataReceive(p_action, data);
                    }
                });

            }
            @Override
            public void onErrorReceived(int p_action, List<Object> data) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onErrorDataReceive(p_action, data);
                    }
                });

            }
        };
        senderBridge = new SenderBridge(requestReceived);


        drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout != null)
            setLeftMenu();

       iv_menu =  findViewById(R.id.iv_menu);
       if (iv_menu != null)
        iv_menu.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isCreated) {
            initViews();
            setViews();
            bindEvents();

            isCreated = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    public void sendRequest(Request request) {

        if (senderBridge != null)
            senderBridge.sendMessage(request);
    }

    public void onDataReceive(int action, List<Object> data) {}
    public void onErrorDataReceive(int action, List<Object> data) {}


    protected void changeFragment(String tagTransition, int viewId, Fragment nextFragment) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        // 2. Shared Elements Transition
        TransitionSet enterTransitionSet = new TransitionSet();
        enterTransitionSet.addTransition(TransitionInflater.from(getApplicationContext()).inflateTransition(android.R.transition.move));
        enterTransitionSet.setDuration(MOVE_DEFAULT_TIME);
        nextFragment.setSharedElementEnterTransition(enterTransitionSet);


        if (tagTransition != null || viewId != -1) {
            View logo = findViewById(viewId);
            fragmentTransaction.addSharedElement(logo, tagTransition);
        }
        fragmentTransaction.replace(android.R.id.content, nextFragment);
        fragmentTransaction
                .commit();
    }

    protected void startActivity(int activityId) {
        applicationActivity = CheckSetup.applicationActivityMap.get(activityId);
        if (applicationActivity != null) {
            Intent intent = new Intent(getApplicationContext(), applicationActivity.getActivityClass());
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);

        }
    }
    protected void startActivity(int activityId, List<Object> data) {
        applicationActivity = CheckSetup.applicationActivityMap.get(activityId);
        if (applicationActivity != null) {
            Intent intent = new Intent(getApplicationContext(), applicationActivity.getActivityClass());
            intent.putExtra("data", (Serializable) data);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);

        }
    }


    protected void setLeftMenu(){
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        linearLayout = findViewById(R.id.left_drawer);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });


        setActionBar();
        createMenuList();

        ScreenShotable screenShotable = new ScreenShotable() {
            @Override
            public void takeScreenShot() {
            }

            @Override
            public Bitmap getBitmap() {
                return null;
            }
        };

        viewAnimator = new ViewAnimator<>(this, list, screenShotable, drawerLayout, this);
    }

    protected void setActionBar() {
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6 && linearLayout.getChildCount() == 0)
                    viewAnimator.showMenuContent();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
    }


    protected void createMenuList() {
        SlideMenuItem menuItem = new SlideMenuItem(FragmentMenu.BUILDING, R.drawable.icn_1);
        list.add(menuItem);
        SlideMenuItem menuItem2 = new SlideMenuItem(FragmentMenu.BOOK, R.drawable.icn_2);
        list.add(menuItem2);
        SlideMenuItem menuItem3 = new SlideMenuItem(FragmentMenu.PAINT, R.drawable.icn_3);
        list.add(menuItem3);
        SlideMenuItem menuItem4 = new SlideMenuItem(FragmentMenu.CASE, R.drawable.icn_4);
        list.add(menuItem4);
        SlideMenuItem menuItem5 = new SlideMenuItem(FragmentMenu.SHOP, R.drawable.icn_5);
        list.add(menuItem5);
        SlideMenuItem menuItem6 = new SlideMenuItem(FragmentMenu.PARTY, R.drawable.icn_6);
        list.add(menuItem6);
        SlideMenuItem menuItem7 = new SlideMenuItem(FragmentMenu.MOVIE, R.drawable.icn_7);
        list.add(menuItem7);
    }
    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {
        switch (slideMenuItem.getName()) {
            case FragmentMenu.CLOSE:
                return screenShotable;
            default:
                openActivity(screenShotable, position, slideMenuItem);
                return new ScreenShotable() {
                    @Override
                    public void takeScreenShot() {

                    }

                    @Override
                    public Bitmap getBitmap() {
                        return null;
                    }
                };
        }
    }

    private void openActivity(ScreenShotable screenShotable, int topPosition, Resourceble slideMenuItem) {
        switch (slideMenuItem.getName()){
            case FragmentMenu.BUILDING:
                if (!isActivityOpened(CheckSetup.Activities.COURSE_EXAMPLE_ACTIVITY)) {
                    startActivity(CheckSetup.Activities.COURSE_EXAMPLE_ACTIVITY);
                }
                break;
        }

    }

    private boolean isActivityOpened(int nextActivityId){
        if (getActivityId() == nextActivityId){
            return true;
        }

        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (drawerToggle != null)
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (drawerToggle != null)
         drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void disableHomeButton() {

    }

    @Override
    public void enableHomeButton() {
        drawerLayout.closeDrawers();

    }

    @Override
    public void addViewToContainer(View view) {
        linearLayout.addView(view);
    }
    @Override
    public void onClick(View v) {
        if (v == iv_menu) {
            drawerLayout.openDrawer(Gravity.LEFT, true);
        }
    }

    public abstract void initViews();
    public abstract void bindEvents();
    public abstract void setViews();
    public abstract int getLayoutContent();
    public abstract Activity getActivity();
    public abstract int getActivityId();
}


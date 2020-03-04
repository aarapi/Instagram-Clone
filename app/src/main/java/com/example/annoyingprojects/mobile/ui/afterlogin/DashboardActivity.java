package com.example.annoyingprojects.mobile.ui.afterlogin;

import android.animation.Animator;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.mobile.basemodels.BaseActivity;
import com.example.annoyingprojects.mobile.ui.afterlogin.personaldata.FragmentMenu;
import com.example.annoyingprojects.utilities.CheckSetup;
import com.example.annoyingprojects.utilities.RequestFunction;

import java.util.ArrayList;
import java.util.List;

import yalantis.com.sidemenu.interfaces.Resourceble;
import yalantis.com.sidemenu.interfaces.ScreenShotable;
import yalantis.com.sidemenu.model.SlideMenuItem;
import yalantis.com.sidemenu.util.ViewAnimator;

public class DashboardActivity extends BaseActivity implements ViewAnimator.ViewAnimatorListener, View.OnClickListener {

    private ImageView user_photo, iv_menu;
    private TextView user_name, user_id;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private List<SlideMenuItem> list = new ArrayList<>();
    private ViewAnimator viewAnimator;
    private int res = R.drawable.content_music;
    private LinearLayout linearLayout;

    private CardView cv_personal_data, cv_course_schedules, cv_study_result, cv_attendance, cv_course_booking, cv_course_plan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        sendRequest(RequestFunction.getDashboardData(getActivityId()));
        addToRegister(CheckSetup.ServerActions.ANNOYING_PROJECTS_DASHBOARD_ACTIVITY);


        drawerLayout = findViewById(R.id.drawer_layout);
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

    @Override
    public void initViews() {
        iv_menu = findViewById(R.id.iv_menu);

        cv_attendance = findViewById(R.id.cv_attendance);
        cv_course_booking = findViewById(R.id.cv_course_booking);
        cv_course_plan = findViewById(R.id.cv_course_plan);
        cv_course_schedules = findViewById(R.id.cv_course_schedules);
        cv_personal_data = findViewById(R.id.cv_personal_data);
        cv_study_result = findViewById(R.id.cv_study_result);

        user_id = findViewById(R.id.user_id);
        user_name = findViewById(R.id.user_name);
        user_photo = findViewById(R.id.user_photo);
        iv_menu = findViewById(R.id.iv_menu);

    }

    @Override
    public void bindEvents() {
        iv_menu.setOnClickListener(this);

        cv_study_result.setOnClickListener(this);
        cv_course_booking.setOnClickListener(this);
        cv_course_plan.setOnClickListener(this);
        cv_course_schedules.setOnClickListener(this);
        cv_personal_data.setOnClickListener(this);
        cv_study_result.setOnClickListener(this);
        cv_attendance.setOnClickListener(this);
        iv_menu.setOnClickListener(this);
    }

    @Override
    public void setViews() {

    }

    @Override
    public int getLayoutContent() {
        return R.layout.dashboard_activity;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public int getActivityId() {
        return CheckSetup.Activities.DASHBOARD_ACTIVITY;
    }

    @Override
    public void onDataReceive(int action, List<Object> data) {
        super.onDataReceive(action, data);
    }

    private void setActionBar() {
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


    private void createMenuList() {
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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
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
}

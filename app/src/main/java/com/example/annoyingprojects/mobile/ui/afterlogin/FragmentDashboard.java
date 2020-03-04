package com.example.annoyingprojects.mobile.ui.afterlogin;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.mobile.basemodels.BaseFragment;
import com.example.annoyingprojects.mobile.ui.afterlogin.personaldata.FragmentMenu;
import com.example.annoyingprojects.utilities.CheckSetup;

import yalantis.com.sidemenu.interfaces.ScreenShotable;

public class FragmentDashboard extends BaseFragment implements ScreenShotable, View.OnClickListener {
    public static final String CLOSE = "Close";
    public static final String BUILDING = "Building";
    public static final String BOOK = "Book";
    public static final String PAINT = "Paint";
    public static final String CASE = "Case";
    public static final String SHOP = "Shop";
    public static final String PARTY = "Party";
    public static final String MOVIE = "Movie";

    private View container;
    protected ImageView mImageView;
    protected int res;
    private Bitmap bitmap;

    private ImageView user_photo, iv_menu;
    private TextView user_name, user_id;
    private CardView cv_personal_data, cv_course_schedules, cv_study_result, cv_attendance, cv_course_booking, cv_course_plan;

    public FragmentDashboard() {
        super(R.layout.fragment_dashboard_layout);
    }


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
    public void takeScreenShot() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                container = containerView.findViewById(R.id.container);
                Bitmap bitmap = Bitmap.createBitmap(container.getWidth(),
                        container.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                container.draw(canvas);
            }
        };

        thread.start();

    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public void onClick(View v) {
        if (v == cv_attendance) {

        } else if (v == cv_course_booking) {

        } else if (v == cv_course_plan) {

        } else if (v == cv_course_schedules) {

        } else if (v == cv_personal_data) {
        } else if (v == cv_study_result) {

        }
    }
}

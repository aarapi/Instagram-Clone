package com.instacommerce.annoyingprojects.mobile.ui.beforelogin;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.instacommerce.annoyingprojects.R;

import com.instacommerce.annoyingprojects.mobile.basemodels.BaseFragment;

public class FramgentSplashScreen extends BaseFragment {
    private ImageView logoSplash, chmaraTech, logoWhite;
    private Animation anim1, anim2, anim3;


    public static FramgentSplashScreen newInstance(){
        FramgentSplashScreen framgentSplashScreen = new FramgentSplashScreen();
        return framgentSplashScreen;
    }

    @Override
    public void initViews() {
        init();

    }

    @Override
    public void bindEvents() {

    }

    @Override
    public void setViews() {

        logoSplash.startAnimation(anim1);
        anim1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                logoSplash.startAnimation(anim2);
                logoSplash.setVisibility(View.GONE);

                logoWhite.startAnimation(anim3);
                chmaraTech.startAnimation(anim3);
                anim3.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        logoWhite.setVisibility(View.VISIBLE);
                        chmaraTech.setVisibility(View.VISIBLE);

                        switchFragment(R.id.container_frame, new FragmentLogIn());

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_splashscr_layout;
    }

    private void init() {

        logoSplash = containerView.findViewById(R.id.ivLogoSplash);
        logoWhite = containerView.findViewById(R.id.ivLogoWhite);
        chmaraTech = containerView.findViewById(R.id.ivCHTtext);
        anim1 = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
        anim2 = AnimationUtils.loadAnimation(getContext(), R.anim.fadeout);
        anim3 = AnimationUtils.loadAnimation(getContext(), R.anim.fadein);
    }


}

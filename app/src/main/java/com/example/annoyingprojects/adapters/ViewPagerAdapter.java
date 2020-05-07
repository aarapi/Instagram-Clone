package com.example.annoyingprojects.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.data.PostModel;
import com.octopepper.mediapickerinstagram.commons.models.Post;

import java.util.ArrayList;
import java.util.List;

import static com.example.annoyingprojects.utilities.Util.setUserImageRes;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    private ArrayList<String> postImages;
    private List<Object> likeData;

    boolean doubleBackToExitPressedOnce = false;

    public ViewPagerAdapter(Context context, List<Object> likeData, ArrayList<String> postImages) {
        this.context = context;
        this.postImages = postImages;
        this.likeData = likeData;
    }

    @Override
    public int getCount() {
        return postImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.viewpager_post_item_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        ImageView heartAnim = (ImageView) view.findViewById(R.id.heart_anim);
        setUserImageRes(context,postImages.get(position), imageView);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListViewAdapterPost adapterPost = (ListViewAdapterPost) likeData.get(0);
                PostModel postModel = (PostModel) likeData.get(1);
                ListViewAdapterPost.ViewHolder viewHolder = (ListViewAdapterPost.ViewHolder) likeData.get(2);
                int position = (Integer) likeData.get(3);

                if (doubleBackToExitPressedOnce) {

                    Animation pulse_fade = AnimationUtils.loadAnimation(context, R.anim.pulse_fade_in);
                    pulse_fade.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            heartAnim.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            heartAnim.setVisibility(View.GONE);
                            adapterPost.setLike(postModel, viewHolder, position);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    heartAnim.startAnimation(pulse_fade);
                }

                doubleBackToExitPressedOnce = true;
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);

            }
        });

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
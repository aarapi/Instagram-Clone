package com.example.annoyingprojects.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.data.PostModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;
import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE;
import static com.example.annoyingprojects.utilities.Util.setUserImageResGlide;
import static com.example.annoyingprojects.utilities.Util.setUserImageResPicasso;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    private ArrayList<String> postImages;
    private List<Object> likeData;

    boolean doubleBackToExitPressedOnce = false;
    private Picasso picasso;

    public ViewPagerAdapter(Context context, List<Object> likeData, ArrayList<String> postImages, Picasso picasso) {
        this.context = context;
        this.postImages = postImages;
        this.likeData = likeData;
        this.picasso = picasso;
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
//        setUserImageResGlide(context,postImages.get(position), imageView);
        picasso.get().load(postImages.get(position))
                .placeholder(R.drawable.placeholder_error_media)
                .error(R.drawable.placeholder_error_media)
                .noFade()
                .fit()
                .centerCrop().into(imageView);
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
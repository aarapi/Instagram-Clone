package com.example.annoyingprojects.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import com.example.annoyingprojects.R;
import com.example.annoyingprojects.data.PostModel;
import com.example.annoyingprojects.data.Posts;
import com.example.annoyingprojects.data.UserModel;
import com.example.annoyingprojects.mobile.basemodels.BaseActivity;
import com.example.annoyingprojects.mobile.ui.afterlogin.home.HomeActivity;
import com.example.annoyingprojects.mobile.ui.afterlogin.home.LocationBottomSheet;
import com.example.annoyingprojects.mobile.ui.afterlogin.messages.FragmentBottomPostMessage;
import com.example.annoyingprojects.mobile.ui.afterlogin.userprofile.ActivitySinglePost;
import com.example.annoyingprojects.mobile.ui.afterlogin.userprofile.MoreBottomSheetFragment;
import com.example.annoyingprojects.mobile.ui.afterlogin.userprofile.SettingFragment;
import com.example.annoyingprojects.mobile.ui.afterlogin.userprofile.UserProfileFragment;
import com.example.annoyingprojects.utilities.FragmentUtil;
import com.example.annoyingprojects.utilities.RequestFunction;
import com.skyhope.showmoretextview.ShowMoreTextView;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;
import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE;


public class ListViewAdapterPost extends ArrayAdapter<PostModel> implements View.OnClickListener {

    private List<PostModel> dataSet;
    private Context mContext;
    private FragmentManager fragmentManager;
    private boolean isUserPost;
    private OnLoadMoreListener onLoadMoreListener;
    public boolean isLoading = false;
    private int preLast = 0;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private ListViewAdapterPost adapterPost;
    private Picasso picasso;

    // View lookup cache
    public static class ViewHolder {
        TextView tv_user_name, tv_product_name, product_price, tv_likedby_value;
        ShowMoreTextView tv_product_description_value;
        ImageView iv_post_img, iv_send_message;
        CircleImageView cv_user_img;
        CheckBox iv_like;
        ViewPager viewPager;
        ImageView iv_more;
        LinearLayout sliderDotspanel;
        ImageView[] dots;
        ImageView iv_location;
    }

    public ListViewAdapterPost(List<PostModel> data, Context context, FragmentManager fragmentManager,
                               boolean isUserPost, ListView listView) {
        super(context, R.layout.cell_post_layout, data);
        this.dataSet = data;
        this.mContext = context;
        adapterPost = this;
        this.fragmentManager = fragmentManager;
        this.isUserPost = isUserPost;
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (picasso == null) {
                    picasso = Picasso.get();
                }
                if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    picasso.resumeTag(context);
                } else {
                    picasso.pauseTag(context);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (listView.getAdapter() == null)
                    return ;

                if (listView.getAdapter().getCount() == 0)
                    return ;

                int l = visibleItemCount + firstVisibleItem;
                if (!isLoading && l >= totalItemCount) {
                    // It is time to add new data. We call the listener

                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {


    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public PostModel getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void updatePostList(List<PostModel> newlist) {
        dataSet.clear();
        dataSet.addAll(newlist);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PostModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag


        viewHolder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            convertView = inflater.inflate(R.layout.cell_post_layout, parent, false);
            viewHolder.cv_user_img = (CircleImageView) convertView.findViewById(R.id.cv_user_img);
            viewHolder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
            viewHolder.iv_post_img = (ImageView) convertView.findViewById(R.id.iv_post_img);
            viewHolder.tv_product_name = (TextView) convertView.findViewById(R.id.tv_product_name);
            viewHolder.tv_product_description_value = (ShowMoreTextView) convertView.findViewById(R.id.tv_product_description_value);
            viewHolder.product_price = (TextView) convertView.findViewById(R.id.product_price);
            viewHolder.iv_like = (CheckBox) convertView.findViewById(R.id.iv_like);
            viewHolder.iv_send_message = (ImageView) convertView.findViewById(R.id.iv_send_message);
            viewHolder.tv_likedby_value = (TextView) convertView.findViewById(R.id.tv_likedby_value);
            viewHolder.viewPager = (ViewPager) convertView.findViewById(R.id.viewPager);
            viewHolder.sliderDotspanel = (LinearLayout) convertView.findViewById(R.id.SliderDots);
            viewHolder.iv_more = (ImageView) convertView.findViewById(R.id.iv_more);
            viewHolder.iv_location = (ImageView) convertView.findViewById(R.id.iv_location);

            viewHolder.iv_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putSerializable("PRODUCT_DATA", dataModel);
                    LocationBottomSheet locationBottomSheet = LocationBottomSheet.newInstance(args);
                    locationBottomSheet.show(((BaseActivity) mContext).getSupportFragmentManager(), LocationBottomSheet.TAG);
                }
            });

            if (!isUserPost){
                viewHolder.iv_more.setVisibility(View.GONE);
            }

            viewHolder.iv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setLike(dataModel, viewHolder, position);
                    viewHolder.tv_likedby_value.setText(getItem(position).getLikedByNo() + "");
                }
            });
            viewHolder.iv_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showBottomSheet(isUserPost, position);
                }
            });
            viewHolder.iv_send_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putSerializable("POST", (Serializable) dataModel);
                    FragmentBottomPostMessage fragmentBottomPostMessage = FragmentBottomPostMessage.newInstance(args);
                    BaseActivity activity = (BaseActivity) getContext();
                    fragmentBottomPostMessage.show(activity.getSupportFragmentManager(), FragmentBottomPostMessage.TAG);
                }
            });
            viewHolder.tv_user_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isUserPost) {
                        UserModel userModel = new UserModel();
                        userModel.email = "";
                        userModel.userImage = dataModel.getLinkUserImg();
                        userModel.username = dataModel.getUserName();

                        Bundle args = new Bundle();
                        args.putSerializable(UserProfileFragment.USER_PROFILE_DATA, (Serializable) userModel);
                        UserProfileFragment userProfileFragment = UserProfileFragment.newInstance(args);

                        FragmentUtil.switchFragmentWithAnimation(R.id.fl_fragment_container,
                                userProfileFragment,
                                (HomeActivity) getContext(),
                                FragmentUtil.USER_PROFILE_FRAGMENT,
                                null);
                    }
                }
            });


            viewHolder.tv_user_name.setText(dataModel.getUserName());
            viewHolder.product_price.setText(dataModel.getProductPrice());
            viewHolder.tv_product_description_value.setText(dataModel.getProductDescription());
            viewHolder.tv_product_name.setText(dataModel.getProductName());
            viewHolder.tv_likedby_value.setText(dataModel.getLikedByNo() + "");
            viewHolder.tv_product_description_value.setShowingLine(3);
            viewHolder.tv_product_description_value.addShowMoreText("more");
            viewHolder.tv_product_description_value.addShowLessText("less");
            viewHolder.tv_product_description_value.setShowLessTextColor(R.color.gray);
            viewHolder.tv_product_description_value.setShowMoreColor(R.color.gray);
            setimageresource(dataModel.getLinkUserImg(), viewHolder.cv_user_img);


            if (dataModel.isLikeChecked()) {
                viewHolder.iv_like.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_heart_clicked));
            } else {
                viewHolder.iv_like.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_heart));

            }


            List<Object> likeData = new ArrayList<>();
            likeData.add(adapterPost);
            likeData.add(dataModel);
            likeData.add(viewHolder);
            likeData.add(position);


            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getContext(), likeData, dataModel.getLinkImages(), picasso);
            viewHolder.viewPager.setAdapter(viewPagerAdapter);

            viewHolder.dots = new ImageView[dataModel.getLinkImages().size()];
            for (int i = 0; i < dataModel.getLinkImages().size(); i++) {

                viewHolder.dots[i] = new ImageView(getContext());
                viewHolder.dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.nonactive_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                params.setMargins(8, 0, 8, 0);

                viewHolder.sliderDotspanel.addView(viewHolder.dots[i], params);

            }

            viewHolder.dots[0].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));
            viewHolder.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    for (int i = 0; i < dataModel.getLinkImages().size(); i++) {
                        viewHolder.dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.nonactive_dot));
                    }

                    viewHolder.dots[position].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {

            convertView = inflater.inflate(R.layout.item_progressbar, parent, false);
        }

        return convertView;
    }

    public void setLike(PostModel dataModel, ViewHolder viewHolder, int position) {
        BaseActivity activity;
        if (isUserPost) {
            activity = (ActivitySinglePost) getContext();
        } else {
            activity = (HomeActivity) getContext();
        }

        if (dataModel.isLikeChecked()) {
            viewHolder.iv_like.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_heart));
            getItem(position).setLikeChecked(false);
            getItem(position).setLikedByNo(dataModel.getLikedByNo() - 1);

            activity.sendRequest(RequestFunction.setPostLike(0, dataModel.getPostId(), false));
        } else {
            viewHolder.iv_like.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_heart_clicked));
            getItem(position).setLikeChecked(true);
            getItem(position).setLikedByNo(dataModel.getLikedByNo() + 1);
            activity.sendRequest(RequestFunction.setPostLike(0, dataModel.getPostId(), true));
        }
        this.notifyDataSetChanged();
    }
    private void setimageresource(String url, ImageView img){
        Picasso.get().load(url)
                .placeholder(R.drawable.placeholder_error_media).error(R.drawable.placeholder_error_media).fit()
                .centerCrop().into(img);
    }

    public void showBottomSheet(boolean isUserPost, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("isUserPost", isUserPost);
        bundle.putSerializable("postId", dataSet.get(position).getPostId());
        MoreBottomSheetFragment addPhotoBottomDialogFragment =
                MoreBottomSheetFragment.newInstance(bundle);
        addPhotoBottomDialogFragment.show(fragmentManager ,
                SettingFragment.TAG);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }


    public void setData(ArrayList<Posts> posts){
        int postsSize = posts.size();
        if (dataSet.size() > 0) {
            dataSet.remove(dataSet.size() - 1);
        }
        for (int i =0; i<postsSize; i++){
            PostModel postModel = posts.get(i).getPostModel();
            postModel.setLinkUserImg(posts.get(i).getLinkUserImg());
            postModel.setLinkImages(posts.get(i).getLinkImages());

            dataSet.add(postModel);
        }
    }

    public void createNewPost(Posts posts) {
        PostModel postModel = posts.getPostModel();
        postModel.setLinkImages(posts.getLinkImages());
        postModel.setLinkUserImg(posts.getLinkUserImg());
        dataSet.add(0, postModel);
    }

    public List<PostModel> getDataSet() {
        return dataSet;
    }
}

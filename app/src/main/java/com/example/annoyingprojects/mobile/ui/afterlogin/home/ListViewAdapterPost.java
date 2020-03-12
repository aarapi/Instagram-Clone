package com.example.annoyingprojects.mobile.ui.afterlogin.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.annoyingprojects.R;

import java.util.List;



public class ListViewAdapterPost extends ArrayAdapter<PostModel> implements View.OnClickListener {

    private List<PostModel> dataSet;
    private Context mContext;

    // View lookup cache
    private static class ViewHolder {
        ImageView cv_user_img;
        TextView tv_user_name;
        ImageView iv_post_img;

    }

    public ListViewAdapterPost(List<PostModel> data, Context context) {
        super(context, R.layout.post_cell_layout, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View v) {


    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PostModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.post_cell_layout, parent, false);
            viewHolder.cv_user_img = (ImageView) convertView.findViewById(R.id.cv_user_img);
            viewHolder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
            viewHolder.iv_post_img = (ImageView) convertView.findViewById(R.id.iv_post_img);



            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        viewHolder.tv_user_name.setText(dataModel.getName());
       setImageResourcE(dataModel.getLinkUserImg(),viewHolder.cv_user_img);
        setImageResourcE(dataModel.getLinkImage(),viewHolder.iv_post_img);



        return convertView;
    }

    private void setImageResourcE(String url, ImageView img){
        Glide.with(mContext)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade(400))
                .apply(new RequestOptions().override(Target.SIZE_ORIGINAL))
                .into(img);
    }
}
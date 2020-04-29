package com.example.annoyingprojects.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.fragment.app.FragmentManager;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.data.PostModel;
import com.example.annoyingprojects.mobile.basemodels.BaseActivity;
import com.example.annoyingprojects.mobile.ui.afterlogin.home.HomeFragment;
import com.example.annoyingprojects.mobile.ui.afterlogin.userprofile.ActivitySinglePost;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.example.annoyingprojects.mobile.ui.afterlogin.userprofile.ActivitySinglePost.SINGLE_POST_DATA;
import static com.example.annoyingprojects.utilities.Util.setUserImageRes;

public class GridAdapter extends BaseAdapter
{
   private Context context;
   private List<PostModel> postModelList;
   private FragmentManager fragmentManager;
    private static class ViewHolder {
        ImageView iv_post_image;
        ImageView iv_multiple;

    }



    public GridAdapter(Context context, List<PostModel> postModelList
            , FragmentManager fragmentManager)
    {
        this.context = context;
        this.postModelList = postModelList;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {

        return postModelList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
       return postModelList.get(position);
    }

    @Override
    public long getItemId(int position) {

        // TODO Auto-generated method stub

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
         ViewHolder viewHolder;
        final View result;

        PostModel postModel = (PostModel) getItem(position);

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.user_post_cell_layout, parent, false);
            viewHolder.iv_post_image =  convertView.findViewById(R.id.iv_post_image);
            viewHolder.iv_multiple = convertView.findViewById(R.id.iv_multiple);
            viewHolder.iv_post_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<Object> data = new ArrayList<>();
                    data.add(postModelList);

                    Intent intent = new Intent(context, ActivitySinglePost.class);
                    intent.putExtra(SINGLE_POST_DATA, (Serializable) data);
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                    ((BaseActivity)context).overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            });


            if (postModel.getLinkImages().size() > 1){
                viewHolder.iv_multiple.setVisibility(View.VISIBLE);
            }

        setUserImageRes(context, postModel.getLinkImages().get(0), viewHolder.iv_post_image);

        return convertView;
    }


}

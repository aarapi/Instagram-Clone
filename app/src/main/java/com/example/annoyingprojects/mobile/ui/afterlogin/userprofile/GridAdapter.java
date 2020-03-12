package com.example.annoyingprojects.mobile.ui.afterlogin.userprofile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.mobile.ui.afterlogin.home.PostModel;

import java.util.List;

import static com.example.annoyingprojects.utilities.Util.setUserImageRes;

public class GridAdapter extends BaseAdapter
{
   private Context context;
   private List<PostModel> postModelList;
    private static class ViewHolder {
        ImageView iv_post_image;

    }



    public GridAdapter(Context context, List<PostModel> postModelList)
    {
        this.context = context;
        this.postModelList = postModelList;
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
        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.user_post_cell_layout, parent, false);
            viewHolder.iv_post_image = (ImageView) convertView.findViewById(R.id.iv_post_image);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        setUserImageRes(context, postModel.getLinkImage(), viewHolder.iv_post_image);

        return convertView;
    }


}

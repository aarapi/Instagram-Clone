package com.example.annoyingprojects.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.data.PostModel;
import com.skyhope.showmoretextview.ShowMoreTextView;

import java.util.List;

public class ListViewAdapterMessages extends ArrayAdapter<PostModel> implements View.OnClickListener {

    private List<PostModel> dataSet;
    private Context mContext;
    private FragmentManager fragmentManager;

    // View lookup cache
    private static class ViewHolder {
        TextView tv_user_name;
    }

    public ListViewAdapterMessages(List<PostModel> data, Context context, FragmentManager fragmentManager) {
        super(context, R.layout.post_cell_layout, data);
        this.dataSet = data;
        this.mContext = context;
        this.fragmentManager = fragmentManager;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PostModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ListViewAdapterMessages.ViewHolder viewHolder; // view lookup cache stored in tag


        viewHolder = new ListViewAdapterMessages.ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.message_cell_layout, parent, false);





        return convertView;
    }

}

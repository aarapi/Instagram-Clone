package com.instacommerce.annoyingprojects.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.instacommerce.annoyingprojects.R;
import com.instacommerce.annoyingprojects.utilities.Util;

import java.util.ArrayList;

public class StaggerPostRecyclerAdapter extends RecyclerView.Adapter<StaggerPostRecyclerAdapter.MyViewHolder> {
    ArrayList personNames;
    ArrayList<String> personImages;
    Context context;
    public StaggerPostRecyclerAdapter(Context context, ArrayList personNames, ArrayList<String> personImages) {
        this.context = context;
        this.personNames = personNames;
        this.personImages = personImages;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stager_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
        holder.name.setText((String) personNames.get(position));
        Util.setUserImageResPicasso(personImages.get(position), holder.image);
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    @Override
    public int getItemCount() {
        return personNames.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name;
        ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name = (TextView) itemView.findViewById(R.id.name);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
package com.example.annoyingprojects.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.data.User;
import com.example.annoyingprojects.data.UserMessagesModel;
import com.example.annoyingprojects.utilities.Util;

import java.util.ArrayList;

public class RecyclerViewAdapterSearch extends RecyclerView.Adapter<RecyclerViewAdapterSearch.RecyclerViewAdapterSearchViewHolder> {
    private ArrayList<User> users;
    private Context context;
    private OnItemClickListener clickListener;

    public RecyclerViewAdapterSearch(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_search, parent, false);
        return new RecyclerViewAdapterSearchViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterSearchViewHolder holder, int position) {
        User user = getItem(position);
        holder.bind(user);
    }

    public User getItem(int position) {
        return users.size() > position ? users.get(position) : null;
    }


    @Override
    public int getItemCount() {
        return users.size();
    }


    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(
            final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    class RecyclerViewAdapterSearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Context context;

        TextView tv_username, tv_name;
        ImageView iv_user;


        public RecyclerViewAdapterSearchViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            this.context = context;

            tv_username = itemView.findViewById(R.id.tv_username);
            tv_name = itemView.findViewById(R.id.tv_name);

            iv_user = itemView.findViewById(R.id.iv_user);

            itemView.setOnClickListener(this);
        }

        void bind(User user) {
            tv_username.setText(user.username);
            tv_name.setText(user.email);

            Util.setUserImageRes(context, user.userImage, iv_user);
        }


        @Override
        public void onClick(View view) {
            clickListener.onItemClick(itemView, getPosition());
        }
    }
}

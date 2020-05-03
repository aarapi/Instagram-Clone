package com.example.annoyingprojects.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.data.PostModel;
import com.example.annoyingprojects.data.UserMessagesModel;

import java.util.ArrayList;

public class RecyclerViewAdapterMessageUsers extends RecyclerView.Adapter<RecyclerViewAdapterMessageUsersViewHolder> {

    private ArrayList<UserMessagesModel> userMessagesModels;

    public RecyclerViewAdapterMessageUsers(ArrayList<UserMessagesModel> userMessagesModels) {
        this.userMessagesModels = userMessagesModels;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterMessageUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog, null);
        return new RecyclerViewAdapterMessageUsersViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterMessageUsersViewHolder holder, int position) {
        UserMessagesModel userMessagesModel = getItem(position);
        holder.bind(userMessagesModel);
    }

    @Override
    public int getItemCount() {
        return userMessagesModels.size();
    }

    public UserMessagesModel getItem(int position) {
        return userMessagesModels.size() > position ? userMessagesModels.get(position) : null;
    }


}

class RecyclerViewAdapterMessageUsersViewHolder extends RecyclerView.ViewHolder {


    public RecyclerViewAdapterMessageUsersViewHolder(@NonNull View itemView, RecyclerViewAdapterMessageUsers recyclerViewAdapterMessageUsers) {
        super(itemView);
    }

    void bind(UserMessagesModel userMessagesModel) {

    }
}

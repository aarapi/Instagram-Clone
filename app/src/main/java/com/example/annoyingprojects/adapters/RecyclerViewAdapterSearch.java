package com.example.annoyingprojects.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.data.UserModel;
import com.example.annoyingprojects.utilities.Util;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapterSearch extends RecyclerView.Adapter<RecyclerViewAdapterSearch.RecyclerViewAdapterSearchViewHolder> {
    private ArrayList<UserModel> userModels;
    private Context context;
    private OnItemClickListener clickListener;

    private int selectedNr = 0;

    private boolean isSendMessage;

    public RecyclerViewAdapterSearch(Context context, ArrayList<UserModel> userModels, boolean isSendMessage) {
        this.context = context;
        this.userModels = userModels;
        this.isSendMessage = isSendMessage;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_search, parent, false);
        return new RecyclerViewAdapterSearchViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterSearchViewHolder holder, int position) {
        UserModel userModel = getItem(position);
        holder.bind(userModel);
    }

    public UserModel getItem(int position) {
        return userModels.get(position);
    }


    @Override
    public int getItemCount() {
        return userModels.size();
    }


    public ArrayList<UserModel> getUserModels() {
        return userModels;
    }

    public void setUserModels(ArrayList<UserModel> userModels) {
        this.userModels.clear();
        this.userModels.addAll(userModels);
        this.notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);

        public void onRadioButtonClicked(CheckBox checkBox, int position, int selectedNr);
    }

    public void SetOnItemClickListener(
            final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public int getSelectedNr() {
        return selectedNr;
    }

    public void setSelectedNr(int selectedNr) {
        this.selectedNr = selectedNr;
    }

    class RecyclerViewAdapterSearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Context context;

        TextView tv_username, tv_name;
        CircleImageView iv_user;
        CheckBox chb_choose_user;


        public RecyclerViewAdapterSearchViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            this.context = context;

            tv_username = itemView.findViewById(R.id.tv_username);
            tv_name = itemView.findViewById(R.id.tv_name);

            iv_user = itemView.findViewById(R.id.iv_user);
            chb_choose_user = itemView.findViewById(R.id.chb_choose_user);

            itemView.setOnClickListener(this);
            chb_choose_user.setOnClickListener(this);

            if (!isSendMessage) {
                chb_choose_user.setVisibility(View.GONE);
            }
        }

        void bind(UserModel userModel) {
            tv_username.setText(userModel.username);
            tv_name.setText(userModel.email);

            Util.setUserImageRes(context, userModel.userImage, iv_user);
        }


        @Override
        public void onClick(View view) {
            if (view == itemView) {
                clickListener.onItemClick(itemView, getAdapterPosition());
            } else if (view == chb_choose_user) {
                if (chb_choose_user.isChecked()) {
                    selectedNr += 1;
                } else {
                    selectedNr -= 1;
                }
                clickListener.onRadioButtonClicked(chb_choose_user, getAdapterPosition(), selectedNr);
            }
        }
    }
}

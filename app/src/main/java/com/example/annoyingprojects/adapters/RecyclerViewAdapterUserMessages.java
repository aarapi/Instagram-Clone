package com.example.annoyingprojects.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.data.UserMessagesModel;
import com.example.annoyingprojects.data.UserModel;
import com.example.annoyingprojects.repository.LocalServer;
import com.example.annoyingprojects.utilities.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapterUserMessages extends RecyclerView.Adapter<RecyclerViewAdapterUserMessages.RecyclerViewAdapterMessageUsersViewHolder> {

    private ArrayList<UserMessagesModel> messageUsersModels;
    private Context context;
    private OnItemClickListener clickListener;
    private UserModel userModel;


    public RecyclerViewAdapterUserMessages(Context context, ArrayList<UserMessagesModel> messageUsersModels) {
        this.messageUsersModels = messageUsersModels;
        this.context = context;
        userModel = LocalServer.getInstance(context).getUser();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(
            final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public void addNewMessage(UserMessagesModel messageUsersModel) {
        this.messageUsersModels.add(0, messageUsersModel);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewAdapterMessageUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_user_messages_income_layout, null);
        return new RecyclerViewAdapterMessageUsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterMessageUsersViewHolder holder, int position) {
        UserMessagesModel messageUsersModel = getItem(position);
        holder.bind(messageUsersModel);
    }

    @Override
    public int getItemCount() {
        return messageUsersModels.size();
    }

    public UserMessagesModel getItem(int position) {
        return messageUsersModels.size() > position ? messageUsersModels.get(position) : null;
    }

    class RecyclerViewAdapterMessageUsersViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_message;
        private TextView tv_time;
        private TextView tv_sent;
        private TextView tv_username;
        private TextView tv_message_post;

        private RelativeLayout dialogRootLayout;
        private RelativeLayout rl_message;
        private RelativeLayout rl_post_message;

        private CircleImageView cv_user_img;
        private ImageView iv_post_image;

        public RecyclerViewAdapterMessageUsersViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_message = (TextView) itemView.findViewById(R.id.tv_message);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_sent = (TextView) itemView.findViewById(R.id.tv_sent);
            tv_username = (TextView) itemView.findViewById(R.id.tv_username);
            tv_message_post = (TextView) itemView.findViewById(R.id.tv_message_post);

            cv_user_img = (CircleImageView) itemView.findViewById(R.id.cv_user_img);
            iv_post_image = (ImageView) itemView.findViewById(R.id.iv_post_image);

            dialogRootLayout = (RelativeLayout) itemView.findViewById(R.id.dialogRootLayout);
            rl_message = (RelativeLayout) itemView.findViewById(R.id.rl_message);
            rl_post_message = (RelativeLayout) itemView.findViewById(R.id.rl_post_message);
        }

        void bind(UserMessagesModel messageUsersModel) {
            RelativeLayout.LayoutParams params = new RelativeLayout
                    .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if (messageUsersModel.getUsernamFrom().equals(userModel.username)) {
                rl_message.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.bg_outcoming));
                rl_post_message.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.bg_outcoming));
                params.setMargins(50, 10, 0, 10);
                rl_message.setPadding(20, 20, 50, 20);
                params.addRule(RelativeLayout.ALIGN_PARENT_END);

                tv_sent.setText("Sent ");

            } else {
                rl_message.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.bg_incoming));
                rl_post_message.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.bg_incoming));
                rl_message.setPadding(50, 20, 20, 20);
                params.setMargins(0, 10, 50, 10);
                params.addRule(RelativeLayout.ALIGN_PARENT_START);

                tv_sent.setText("");
            }

            if (messageUsersModel.isPostMessage()) {
                rl_post_message.setVisibility(View.VISIBLE);
                rl_message.setVisibility(View.GONE);
                rl_post_message.setLayoutParams(params);

                tv_username.setText(messageUsersModel.getPostUsername());
                tv_message_post.setText(messageUsersModel.getMessage());

                Util.setUserImageResPicasso(context, messageUsersModel.getPostImage(), iv_post_image);
                Util.setUserImageResPicasso(context, messageUsersModel.getPostUserImg(), cv_user_img);
            } else {
                rl_post_message.setVisibility(View.GONE);
                rl_message.setVisibility(View.VISIBLE);
                rl_message.setLayoutParams(params);
            }
            tv_message.setText(messageUsersModel.getMessage());

            SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
            String time = localDateFormat.format(messageUsersModel.getMessageTime());

            tv_time.setText(time);
        }

    }

}



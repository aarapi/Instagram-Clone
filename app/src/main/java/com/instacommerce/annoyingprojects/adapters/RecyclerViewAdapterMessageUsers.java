package com.instacommerce.annoyingprojects.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.instacommerce.annoyingprojects.R;
import com.instacommerce.annoyingprojects.data.MessageUsersModel;
import com.instacommerce.annoyingprojects.repository.LocalServer;
import com.instacommerce.annoyingprojects.utilities.Util;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapterMessageUsers extends RecyclerView.Adapter<RecyclerViewAdapterMessageUsers.RecyclerViewAdapterMessageUsersViewHolder> {

    private ArrayList<MessageUsersModel> messageUsersModels;
    private Context context;
    private OnItemClickListener clickListener;

    public RecyclerViewAdapterMessageUsers(Context context, ArrayList<MessageUsersModel> messageUsersModels) {
        this.messageUsersModels = messageUsersModels;
        this.context = context;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(
            final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public void setMessageUsersModels(ArrayList<MessageUsersModel> messageUsersModels) {
        this.messageUsersModels.clear();
        this.messageUsersModels.addAll(messageUsersModels);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewAdapterMessageUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_messsage_users_layout, null);
        return new RecyclerViewAdapterMessageUsersViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterMessageUsersViewHolder holder, int position) {
        MessageUsersModel messageUsersModel = getItem(position);
        holder.bind(messageUsersModel);
    }

    @Override
    public int getItemCount() {
        return messageUsersModels.size();
    }

    public MessageUsersModel getItem(int position) {
        return messageUsersModels.size() > position ? messageUsersModels.get(position) : null;
    }

    class RecyclerViewAdapterMessageUsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Context context;
        private CircleImageView iv_user_from;

        private TextView dialogName;
        private TextView dialogLastMessage;
        private TextView dialogUnreadBubble;

        public RecyclerViewAdapterMessageUsersViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;

            iv_user_from = (CircleImageView) itemView.findViewById(R.id.iv_user_from);

            dialogLastMessage = itemView.findViewById(R.id.dialogLastMessage);
            dialogName = itemView.findViewById(R.id.dialogName);
            dialogUnreadBubble = itemView.findViewById(R.id.dialogUnreadBubble);

            itemView.setOnClickListener(this);

        }

        void bind(MessageUsersModel messageUsersModel) {
            List<String> data = getMessageUsersData(messageUsersModel);
            Util.setUserImageResPicasso(data.get(1), iv_user_from);

            dialogName.setText(data.get(0));
            dialogLastMessage.setText(data.get(1));

            if (messageUsersModel.isOnline()) {
                dialogUnreadBubble.setBackgroundTintList(context.getResources().getColorStateList(R.color.dialog_unread_bubble));
            } else {
                dialogUnreadBubble.setBackgroundTintList(context.getResources().getColorStateList(R.color.red_record));
            }

        }

        private List<String> getMessageUsersData(MessageUsersModel messageUsersModel) {
            List<String> data = new ArrayList<>();
            if (messageUsersModel.getUsernameFrom().equals(LocalServer.getInstance(context).getUser().username)) {
                data.add(messageUsersModel.getUsernameTo());
                data.add(messageUsersModel.getAvatarTo());
            } else {
                data.add(messageUsersModel.getUsernameFrom());
                data.add(messageUsersModel.getAvatarFrom());
            }

            return data;
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getAdapterPosition());
        }
    }

}



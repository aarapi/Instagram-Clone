package com.example.annoyingprojects.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.annoyingprojects.R;
import com.example.annoyingprojects.data.PostModel;
import com.example.annoyingprojects.data.UserMessagesModel;
import com.example.annoyingprojects.utilities.Util;
import com.stfalcon.chatkit.utils.ShapeImageView;

import java.util.ArrayList;

public class RecyclerViewAdapterMessageUsers extends RecyclerView.Adapter<RecyclerViewAdapterMessageUsersViewHolder> {

    private ArrayList<UserMessagesModel> userMessagesModels;
    private Context context;


    public RecyclerViewAdapterMessageUsers(Context context, ArrayList<UserMessagesModel> userMessagesModels) {
        this.userMessagesModels = userMessagesModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterMessageUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog, null);
        return new RecyclerViewAdapterMessageUsersViewHolder(view, context);
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

    private Context context;
    private ImageView iv_user_from;

    private TextView dialogName;
    private TextView dialogLastMessage;
    private TextView dialogUnreadBubble;

    public RecyclerViewAdapterMessageUsersViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;

        iv_user_from = itemView.findViewById(R.id.iv_user_from);

        dialogLastMessage = itemView.findViewById(R.id.dialogLastMessage);
        dialogName = itemView.findViewById(R.id.dialogName);
        dialogUnreadBubble = itemView.findViewById(R.id.dialogUnreadBubble);
    }

    void bind(UserMessagesModel userMessagesModel) {
        Util.setUserImageRes(context, userMessagesModel.getAvatar(), iv_user_from);

        dialogName.setText(userMessagesModel.getUsername_to());
        dialogLastMessage.setText(userMessagesModel.getAvatar());

        if (userMessagesModel.isOnline()) {
            dialogUnreadBubble.setBackgroundTintList(context.getResources().getColorStateList(R.color.dialog_unread_bubble));
        } else {
            dialogUnreadBubble.setBackgroundTintList(context.getResources().getColorStateList(R.color.red_record));
        }

    }
}

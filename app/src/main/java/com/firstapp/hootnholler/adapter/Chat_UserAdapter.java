package com.firstapp.hootnholler.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.ChatUser;
import com.firstapp.hootnholler.R;

import java.util.ArrayList;
import java.util.List;

public class Chat_UserAdapter extends RecyclerView.Adapter<Chat_UserAdapter.UserViewHolder> {

    private List<ChatUser> userList;
    private OnItemClickListener onItemClickListener;
    private List<ChatUser> filteredUserList;

    public void updateUsers(List<ChatUser> userList) {
        this.userList = userList;
        this.filteredUserList = userList;  // Initialize filtered list with full list
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public Chat_UserAdapter(List<ChatUser> userList) {
        this.userList = userList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        ChatUser chatUser = userList.get(position);

        holder.profileImageView.setImageResource(R.drawable.profile_circle_svgrepo_com);
        holder.usernameTextView.setText(chatUser.getFullname());
        holder.lastMessageTextView.setText(chatUser.getLastMessage());
        holder.unreadCountTextView.setText(String.valueOf(ChatUser.getUnreadCount()));

        // Set unread count
        if (ChatUser.getUnreadCount() > 0) {
            holder.unreadCountTextView.setVisibility(View.VISIBLE);
            holder.unreadCountTextView.setText(String.valueOf(ChatUser.getUnreadCount()));
        } else {
            holder.unreadCountTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView usernameTextView;
        private TextView lastMessageTextView;
        private TextView unreadCountTextView;
        private ImageView profileImageView;

        public UserViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.username);
            lastMessageTextView = itemView.findViewById(R.id.lastmessage);
            unreadCountTextView = itemView.findViewById(R.id.unread_count);
            profileImageView = itemView.findViewById(R.id.profileImage);

            // Handle item click event
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    // Method to filter the user list based on search query
    public void filterList(String query) {
        List<ChatUser> filteredList = new ArrayList<>();
        for (ChatUser user : userList) {
            if (TextUtils.isEmpty(query) || user.getFullname().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(user);
            }
        }
        filteredUserList = filteredList;
        notifyDataSetChanged();
    }
}


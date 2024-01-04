package com.firstapp.hootnholler.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.R;
import com.firstapp.hootnholler.models.GroupMessageModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class GroupMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<GroupMessageModel> groupMessagesArrayList;
    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 2;

    public GroupMessageAdapter(Context context, ArrayList<GroupMessageModel> groupMessagesArrayList) {
        this.context = context;
        this.groupMessagesArrayList = groupMessagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.groupchat_receiver_layout, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GroupMessageModel groupMessage = groupMessagesArrayList.get(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this message?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Implement the deletion logic if needed
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();

                return false;
            }
        });

        if (holder.getItemViewType() == ITEM_SEND) {
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            viewHolder.msgTxt.setText(groupMessage.getMessage());
        } else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            viewHolder.receiverName.setText(groupMessage.getReceiverName()); // Display the sender's name in the receiver layout
            viewHolder.msgReceiverTxt.setText(groupMessage.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return groupMessagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        GroupMessageModel groupMessage = groupMessagesArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser() != null &&
                FirebaseAuth.getInstance().getCurrentUser().getUid().equals(groupMessage.getSenderUid())) {
            return ITEM_SEND;
        } else {
            return ITEM_RECEIVE;
        }
    }

    static class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView msgTxt;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            msgTxt = itemView.findViewById(R.id.msgSender);
        }
    }

    static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView receiverName;
        TextView msgReceiverTxt;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverName = itemView.findViewById(R.id.receivername);
            msgReceiverTxt = itemView.findViewById(R.id.msgReceiver);
        }
    }
}


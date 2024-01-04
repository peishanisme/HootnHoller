package com.firstapp.hootnholler.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.R;
import com.firstapp.hootnholler.models.GroupMemberModel;

import java.util.ArrayList;

public class GroupMemberAdapter extends RecyclerView.Adapter<GroupMemberAdapter.ViewHolder> {

    private ArrayList<GroupMemberModel> groupMembersList;

    public GroupMemberAdapter(ArrayList<GroupMemberModel> groupMembersList) {
        this.groupMembersList = groupMembersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroupMemberModel groupMember = groupMembersList.get(position);
        holder.memberNameTextView.setText(groupMember.getMemberName());
    }

    @Override
    public int getItemCount() {
        return groupMembersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView memberNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            memberNameTextView = itemView.findViewById(R.id.groupMemberName);
        }
    }
}


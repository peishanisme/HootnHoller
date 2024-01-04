package com.firstapp.hootnholler.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.R;
import com.firstapp.hootnholler.models.ContactItem;

import java.util.ArrayList;

public class GroupContactAdapter extends RecyclerView.Adapter<GroupContactAdapter.ContactViewHolder> {

    private Context context;
    private ArrayList<ContactItem> groupContactList;
    private Button addContactButton;

    public GroupContactAdapter(Context context, ArrayList<ContactItem> groupContactList) {
        this.context = context;
        this.groupContactList = groupContactList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.group_contact_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        ContactItem contactItem = groupContactList.get(position);
        holder.username.setText(contactItem.getUsername());
        holder.roleShown.setText(contactItem.getRole());

        // Add your logic to load profile images if needed

        holder.addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContactButton.setVisibility(View.VISIBLE);
                addContactButton.setText(contactItem.getUsername());
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupContactList.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView username, roleShown;
        CardView addContactButton;

        ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            username = itemView.findViewById(R.id.username);
            roleShown = itemView.findViewById(R.id.role_shown);
            addContactButton = itemView.findViewById(R.id.addContactbutt);
        }
    }
}


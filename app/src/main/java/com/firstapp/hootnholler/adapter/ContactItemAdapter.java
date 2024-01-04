package com.firstapp.hootnholler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.R;
import com.firstapp.hootnholler.models.ContactItem;

import org.w3c.dom.Text;

import java.text.BreakIterator;
import java.util.List;

public class ContactItemAdapter extends RecyclerView.Adapter<ContactItemAdapter.ContactItemViewHolder> {

    private List<ContactItem> contactItemList;
    private Context context;

    public ContactItemAdapter(Context context, List<ContactItem> contactItemList) {
        this.context = context;
        this.contactItemList = contactItemList;
    }

    @NonNull
    @Override
    public ContactItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ContactItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactItemViewHolder holder, int position) {
        ContactItem contactItem = contactItemList.get(position);

        // Set data to views
        holder.profileImage.setImageResource(contactItem.getProfileImage());
        holder.usernameTextView.setText(contactItem.getUsername());
        holder.roleTextView.setText(contactItem.getRole());

    }

    @Override
    public int getItemCount() {
        return contactItemList.size();
    }

    static class ContactItemViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage;
        TextView usernameTextView;
        TextView roleTextView;

        ContactItemViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            usernameTextView = itemView.findViewById(R.id.username);
            roleTextView = itemView.findViewById(R.id.role_shown);
        }
    }
}


package com.firstapp.hootnholler.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.ChatUser;
import com.firstapp.hootnholler.R;

import java.util.ArrayList;
import java.util.List;

public class ContactTitleAdapter extends RecyclerView.Adapter<ContactTitleAdapter.ContactTitleViewHolder> {

    List<ChatUser> contactTitles = new ArrayList<>();

    public void updateContactTitles(List<ChatUser> userList) {
        List<String> rolesList = new ArrayList<>();
        for (ChatUser user : userList) {
            rolesList.add(user.getRole());
        }

//        this.contactTitles = rolesList;
        notifyDataSetChanged();
    }
    public void filterList(String query) {
        List<ChatUser> filteredList = new ArrayList<>();
        for (ChatUser user : contactTitles) {
            // Adjust the condition based on the fields you want to search
            if (user.getFullname().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(user);
            }
        }
        updateContactTitles(filteredList);
    }

    @NonNull
    @Override
    public ContactTitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_title, parent, false);
        return new ContactTitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactTitleViewHolder holder, int position) {
//        String contactTitle = contactTitles.get(position);
//        holder.bind(contactTitle);
    }

    @Override
    public int getItemCount() {
        return contactTitles == null ? 0 : contactTitles.size();
    }

    static class ContactTitleViewHolder extends RecyclerView.ViewHolder {

        private TextView educatorTitle;

        public ContactTitleViewHolder(@NonNull View itemView) {
            super(itemView);
            educatorTitle = itemView.findViewById(R.id.contactTitle);
        }

        public void bind(String contactTitle) {
            educatorTitle.setText(contactTitle);
        }
    }
}


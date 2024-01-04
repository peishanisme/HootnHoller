package com.firstapp.hootnholler.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.databinding.UserItemBinding;
import com.firstapp.hootnholler.entity.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<User> users;

    public UserAdapter(List<User> users){
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserItemBinding userItemBinding = UserItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserViewHolder(userItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        UserItemBinding binding;

        UserViewHolder(UserItemBinding userItemBinding){
            super(userItemBinding.getRoot());
            binding = userItemBinding;
        }

        void setUserData(User user){
            binding.username.setText(user.fullname);
            //here need to do last message binding
            //plus profile image if want
        }
    }
}

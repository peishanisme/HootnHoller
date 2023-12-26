package com.firstapp.hootnholler.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.R;
import com.firstapp.hootnholler.entity.Student;
import com.firstapp.hootnholler.entity.User;

import java.util.List;

public class Parent_Monitored_Students extends RecyclerView.Adapter<Parent_Monitored_Students.ViewHolder> {

    private List<Student> monitoredStudents;
    User user;

    // Constructor to receive the list of monitored students
    public Parent_Monitored_Students(List<Student> monitoredStudents) {
        this.monitoredStudents = monitoredStudents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout here
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.monitored_students, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to views
        Student student = monitoredStudents.get(position);

        // Check if user is not null before accessing properties
        if (user != null) {
            holder.fullnameTextView.setText(user.getFullname());
            holder.emailTextView.setText(user.getEmail());
            holder.phoneNumberTextView.setText(user.getPhone_number());
        }

        holder.connectionKeyTextView.setText("(" + student.getConnection_key() + ")");
        holder.schoolTextView.setText(student.getSchool());
    }


    @Override
    public int getItemCount() {
        return monitoredStudents.size();
    }

    // ViewHolder class to hold references to your views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fullnameTextView;
        public TextView connectionKeyTextView;
        public TextView emailTextView;
        public TextView phoneNumberTextView;
        public TextView schoolTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullnameTextView = itemView.findViewById(R.id.fullname);
            connectionKeyTextView = itemView.findViewById(R.id.connectionKey);
            emailTextView = itemView.findViewById(R.id.email);
            phoneNumberTextView = itemView.findViewById(R.id.phoneNumber);
            schoolTextView = itemView.findViewById(R.id.school);
        }
    }
}

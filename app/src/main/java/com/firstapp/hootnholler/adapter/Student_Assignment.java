package com.firstapp.hootnholler.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firstapp.hootnholler.R;
import com.firstapp.hootnholler.entity.Assignment;

import java.util.ArrayList;
import java.util.Date;

public class Student_Assignment extends RecyclerView.Adapter<Student_Assignment.ViewHolder> {

    private ArrayList<Assignment> Assignment;

    public Student_Assignment(ArrayList<Assignment> assignment){
        this.Assignment = assignment;
    }

    @NonNull
    @Override
    public Student_Assignment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_assignments, parent, false);
        return new Student_Assignment.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Student_Assignment.ViewHolder holder, int position) {
        Assignment assignment = this.Assignment.get(position);
        holder.assignmentTitleTextView.setText(assignment.getTitle());
        holder.assignmentDescriptionTextView.setText(assignment.getDescription());
        // Completed
        if(assignment.getTaskStatus() == 0){
            holder.assignmentStatusIconImageView.setImageResource(R.drawable.task_done);
        }
        // In Progress
        else if(assignment.getTaskStatus() == 1){
            holder.assignmentStatusIconImageView.setImageResource(R.drawable.task_inprogress_icon);
        }
        // Incompleted
        else if(assignment.getTaskStatus() == 2){
            holder.assignmentStatusIconImageView.setImageResource(R.drawable.task_incompleted_icon);
        }
    }

    @Override
    public int getItemCount() {
        return this.Assignment.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView assignmentTitleTextView, assignmentDescriptionTextView;
        private ImageView assignmentStatusIconImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            assignmentTitleTextView = itemView.findViewById(R.id.assignment_title);
            assignmentDescriptionTextView = itemView.findViewById(R.id.assignment_description);
            assignmentStatusIconImageView = (ImageView) itemView.findViewById(R.id.assignment_status_icon);
        }
    }

    public int checkTaskStatus(Assignment assignment, boolean isSubmit){
        Date dueDate = new Date(assignment.getDueDate());
        Date currentDate = new Date();

        // if due
        if(dueDate.before(currentDate)){
            if(isSubmit){
                // Completed
                return 0;
            }
            else{
                // Incomplete
                return 2;
            }
        }
        // if not due
        else if(dueDate.after(currentDate)){
            if(isSubmit){
                // completed
                return 0;
            }
            else{
                // in progress
                return 1;
            }
        }
        return 3;
    }
}
package com.firstapp.hootnholler.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.R;
import com.firstapp.hootnholler.entity.Feedback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Feedback_List_Adapter extends RecyclerView.Adapter<Feedback_List_Adapter.ViewHolder> {
    private List<Feedback> feedbacks;
    private Context context;
    private boolean isParent;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault());
    private DatabaseReference ClassroomRef = FirebaseDatabase.getInstance().getReference("Classroom");
    private DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference("Users");

    public Feedback_List_Adapter(Context context, List<Feedback> feedbacks, boolean isParent){
        this.feedbacks = feedbacks;
        this.context = context;
        this.isParent = isParent;
    }

    @NonNull
    @Override
    public Feedback_List_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout here
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_list_item, parent, false);
        return new Feedback_List_Adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Feedback_List_Adapter.ViewHolder holder, int position) {
        // Bind data to views
        Feedback feedback = feedbacks.get(position);
        holder.feedbackMsgTextView.setText(feedback.getContent());
        holder.timeTextView.setText(sdf.format(new Date(feedback.getTimeStamp())));

        if(feedback.isPositive()){
            holder.container.setBackgroundColor(Color.parseColor("#CDFB9F"));
        }
        else{
            holder.container.setBackgroundColor(Color.parseColor("#F5E4D3"));
        }
        ClassroomRef.child(feedback.getClassCode()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserRef.child(snapshot.child("classOwner").getValue(String.class)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                        holder.classroomNameTextView.setText(snapshot.child("className").getValue(String.class));
                        holder.teacherNameTextView.setText(userSnapshot.child("fullname").getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return feedbacks.size();
    }

    // ViewHolder class to hold references to your views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView feedbackMsgTextView, timeTextView, teacherNameTextView, classroomNameTextView;
        private LinearLayout container;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.student_feedback_list_item);
            feedbackMsgTextView = itemView.findViewById(R.id.feedback_msg);
            timeTextView = itemView.findViewById(R.id.feedback_timestamp);
            teacherNameTextView = itemView.findViewById(R.id.feedback_teacher_name);
            classroomNameTextView = itemView.findViewById(R.id.feedback_classroom_name);
        }
    }
}

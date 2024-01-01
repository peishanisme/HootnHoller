package com.firstapp.hootnholler.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.R;
import com.firstapp.hootnholler.FeedbackList;
import com.firstapp.hootnholler.entity.Student;
import com.firstapp.hootnholler.entity.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Feedback_Student_List_Adapter extends RecyclerView.Adapter<Feedback_Student_List_Adapter.ViewHolder>{
    private List<Student> students;
    private String currentClassCode;
    User user;
    private Context context;
    private DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference("Users");

    // Constructor to receive the list of monitored students
    public Feedback_Student_List_Adapter(Context context, List<Student> students, String currentClassCode) {
        this.context = context;
        this.students = students;
        this.currentClassCode = currentClassCode;
    }

    @NonNull
    @Override
    public Feedback_Student_List_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout here
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_student_list_item, parent, false);
        return new Feedback_Student_List_Adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Feedback_Student_List_Adapter.ViewHolder holder, int position) {
        // Bind data to views
        Student student = students.get(position);
        UserRef.child(student.studentUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                student.setUserName(snapshot.child("fullname").getValue(String.class));
                student.setProfileURL("");
                holder.StudentName.setText(student.getUserName());
                holder.StudentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Intent intent = new Intent(context, Teacher_Feedback.class);
                        Intent intent = new Intent(context, FeedbackList.class);
                        intent.putExtra("studentUID", student.studentUID);
                        intent.putExtra("classCode", currentClassCode);
                        context.startActivity(intent);
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
        return students.size();
    }

    // ViewHolder class to hold references to your views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView StudentProfile;
        private TextView StudentName;
        private LinearLayout StudentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            StudentProfile = (ImageView) itemView.findViewById(R.id.feedback_student_list_item_profile);
            StudentName = (TextView) itemView.findViewById(R.id.feedback_student_list_item_name);
            StudentLayout = (LinearLayout) itemView.findViewById(R.id.feedback_student_list_item_layout);
        }
    }
}

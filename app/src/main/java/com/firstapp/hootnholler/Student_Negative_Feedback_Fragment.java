package com.firstapp.hootnholler;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firstapp.hootnholler.adapter.Feedback_List_Adapter;
import com.firstapp.hootnholler.entity.Feedback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Student_Negative_Feedback_Fragment extends Fragment {
    private DatabaseReference FeedbackRef = FirebaseDatabase.getInstance().getReference("Feedback");
    private String classCode, studentUID;
    private Feedback_List_Adapter adapter;
    private ArrayList<Feedback> feedbacks;
    private RecyclerView StudentFeedbackList;
    public Student_Negative_Feedback_Fragment(String classCode, String studentUID){
        this.classCode = classCode;
        this.studentUID = studentUID;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_student__negative__feedback_, container, false);
        feedbacks = new ArrayList<>();
        adapter = new Feedback_List_Adapter(getActivity(),  feedbacks);
        StudentFeedbackList = view.findViewById(R.id.teacher_student_negative_feedback_list);
        StudentFeedbackList.setAdapter(adapter);
        StudentFeedbackList.setLayoutManager(new LinearLayoutManager(getActivity()));
        getFeedbacksFromStudent();
        // Inflate the layout for this fragment
        return view;
    }

    public void getFeedbacksFromStudent(){
        feedbacks.clear();
        this.FeedbackRef.child(this.classCode).child(this.studentUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot feedbackSnapShot: snapshot.getChildren()) {
                    Feedback feedback = feedbackSnapShot.getValue(Feedback.class);
                    if(!feedback.isPositive()){
                        feedback.setTimeStamp(Long.parseLong(feedbackSnapShot.getKey()));
                        feedback.setClassCode(classCode);
                        feedbacks.add(feedback);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
package com.firstapp.hootnholler;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.firstapp.hootnholler.adapter.Student_Classroom_List;
import com.firstapp.hootnholler.databinding.ActivityEditAccountBinding;
import com.firstapp.hootnholler.entity.Classroom;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Parent_Statistic_Fragment extends Fragment {

    private TextView AvgPerformance, AvgAtRisk, AvgPositive, AvgNegative;
    private String studentUID;
    private DatabaseReference Database = FirebaseDatabase.getInstance().getReference();
    private Calendar calendar;
    private double WeeklyAverageQuizScore, WeeklyAverageAssignmentScore, WeeklyAveragePerformanceScore;
    private Spinner classroomSpinner;
    private Student_Classroom_List adapter;
    private ArrayList<Classroom> classroomList;
    private Classroom selectedClassroom;
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public Parent_Statistic_Fragment(String studentUID) {
        this.studentUID = studentUID;
    }

    public Parent_Statistic_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent__statistic_, container, false);
        classroomSpinner = view.findViewById(R.id.classroom_list);
        AvgPerformance = view.findViewById(R.id.avgPerformance);
        AvgPositive = view.findViewById(R.id.avg_positive);
        AvgNegative = view.findViewById(R.id.avg_negative);

        classroomList = new ArrayList<>();
        adapter = new Student_Classroom_List(getActivity(), android.R.layout.simple_spinner_item, classroomList);
        classroomSpinner.setAdapter(adapter);
        getClassroom();
        this.classroomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedClassroom = classroomList.get(i);
                getWeeklyAveragePerformance();
                getFeedback();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }

    public void getClassroom(){
        classroomList.clear();
        this.Database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot joinedClassSnapshot : snapshot.child("Student").child(studentUID).child("JoinedClass").getChildren()) {
                    Classroom classroom = snapshot.child("Classroom").child(joinedClassSnapshot.getKey()).getValue(Classroom.class);
                    classroom.classCode = joinedClassSnapshot.getKey();
                    classroomList.add(classroom);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getWeeklyAveragePerformance() {
        WeeklyAverageAssignmentScore = 0;
        WeeklyAverageQuizScore = 0;
        WeeklyAveragePerformanceScore = 0;
        this.Database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> setKey = new ArrayList<>();
                double totalSetNum = 0, totalTask = 0, totalCompleted = 0, totalMarkSet = 0;

                for (DataSnapshot assignmentSnapshot : snapshot.child("Classroom").child(selectedClassroom.getClassCode()).child("Assignment").getChildren()) {
                    totalTask++;
                    for (DataSnapshot submissionSnapShot : assignmentSnapshot.child("Submission").getChildren()) {
                        if (submissionSnapShot.getKey().equals(studentUID)) {
                            totalCompleted++;
                            break;
                        }
                    }
                }

                if(totalTask != 0){
                    WeeklyAverageAssignmentScore = totalCompleted / totalTask * 100;
                }

                // loop subject (quiz score)

                for (DataSnapshot subjectSnapshot : snapshot.child("Student").child(studentUID).child("quiz").child(selectedClassroom.getClassCode()).getChildren()) {
                    setKey.clear();
                    for(DataSnapshot setSubjectSnapshot : subjectSnapshot.child("setKeyInfo").getChildren()){
                        setKey.add(setSubjectSnapshot.getKey());
                    }
                    for (DataSnapshot setSnapshot : snapshot.child("Categories").child(subjectSnapshot.getKey()).child("Sets").getChildren()) {
                        if(!setKey.contains(setSnapshot.getKey())){
                            continue;
                        }
                        for (DataSnapshot rankingSnapshot : setSnapshot.child("Ranking").getChildren()) {
                            if (rankingSnapshot.child("uid").getValue(String.class).equals(studentUID)) {
                                totalMarkSet += rankingSnapshot.child("score").getValue(Double.class);
                                break;
                            }
                        }
                        totalSetNum ++;
                    }
                }

                if(totalSetNum != 0){
                    WeeklyAverageQuizScore = totalMarkSet / totalSetNum;
                }
                WeeklyAveragePerformanceScore = WeeklyAverageAssignmentScore * 0.40 + WeeklyAverageQuizScore * 0.60;
                AvgPerformance.setText(decimalFormat.format(WeeklyAveragePerformanceScore) + "%");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getFeedback(){
        this.Database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int numPositive = 0, numNegative = 0;

                // feedback
                for(DataSnapshot feedbackSnapshot : snapshot.child("Feedback")
                        .child(selectedClassroom.getClassCode())
                        .child(studentUID)
                        .getChildren()){
                    if(!feedbackSnapshot.getKey().equals("at-risk status")){
                        if(feedbackSnapshot.child("positive").getValue(Boolean.class) == null){
                            continue;
                        }
                        if(feedbackSnapshot.child("positive").getValue(Boolean.class)){
                            numPositive ++;
                        }
                        else if(!feedbackSnapshot.child("positive").getValue(Boolean.class)){
                            numNegative ++;
                        }
                    }
                }
                AvgPositive.setText(String.valueOf(numPositive));
                AvgNegative.setText(String.valueOf(numNegative));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
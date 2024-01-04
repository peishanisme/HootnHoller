package com.firstapp.hootnholler;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;

import com.firstapp.hootnholler.adapter.Monitored_Student_List;
import com.firstapp.hootnholler.databinding.FragmentParentHomeBinding;
import com.firstapp.hootnholler.entity.Assignment;
import com.firstapp.hootnholler.entity.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class Parent_Home_Fragment extends Fragment {

    private View TaskStatusBtn, QuizScoreBtn, FeedbackBtn;
    private TextView StudentName, StudentClass, WeeklyAvgPerformance;
    private FragmentParentHomeBinding binding;
    private String studentUID;
    private Spinner Student_List;
    private ArrayList<Student> MonitoredStudentList;
    private DatabaseReference ParentRef = FirebaseDatabase.getInstance().getReference("Parent");
    private DatabaseReference StudentRef = FirebaseDatabase.getInstance().getReference("Student");
    private DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference("Users");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String UserUID = mAuth.getUid();
    private Monitored_Student_List monitoredStudentAdapter;
    private DatabaseReference ClassroomRef = FirebaseDatabase.getInstance().getReference("Classroom");
    private DatabaseReference QuizRef = FirebaseDatabase.getInstance().getReference("Categories");
    private int WeekFromToday, TotalTask = 0, TotalCompleted = 0, TotalIncompleted = 0, TotalInProgress = 0;
    private double WeeklyAveragePerformance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment using View Binding
        binding = FragmentParentHomeBinding.inflate(inflater, container, false);

        TaskStatusBtn = binding.TaskStatusBtn;
        QuizScoreBtn = binding.QuizBtn;
        FeedbackBtn = binding.FeedbackBtn;
        Student_List = binding.studentList;
        StudentName = binding.homeMonitoredStudentName;
        StudentClass = binding.homeMonitoredStudentClass;
        WeeklyAvgPerformance = binding.weeklyAvg;
        MonitoredStudentList = new ArrayList<>();
        monitoredStudentAdapter = new Monitored_Student_List(getActivity(), MonitoredStudentList);
        getStudentUIDList();
        //getWeeklyAveragePerformance();

        Student_List.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Student student = (Student) adapterView.getItemAtPosition(i);
                notifyStudentChanges(student);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Set click listeners for each button
        TaskStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the TaskStatusFragment
                Intent intent=new Intent(getActivity(), TaskStatus_Activity.class);
                intent.putExtra("Student_UID", studentUID);
                startActivity(intent);
            }
        });

        QuizScoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the QuizScoreFragment
                Intent intent=new Intent(getActivity(), Quiz_Score_Activity.class);
                intent.putExtra("Student_UID", studentUID);
                startActivity(intent);
            }
        });

        FeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the FeedbackFragment
                Intent intent=new Intent(getActivity(), Feedback_Activity.class);
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void getStudentUIDList(){
        MonitoredStudentList.clear();
        this.ParentRef.child(UserUID).child("ConnectionKey").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    // get student uid
                    String studentUID = dataSnapshot.getValue(String.class);
                    // get student details
                    StudentRef.child(studentUID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot studentSnapshot) {
                            Student student = studentSnapshot.getValue(Student.class);
                            student.userUID = studentUID;
                            UserRef.child(studentUID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot userSnapShot) {
                                    student.setUserName(userSnapShot.child("fullname").getValue(String.class));
                                    student.setProfile_URL("");
                                    MonitoredStudentList.add(student);
                                    Student_List.setAdapter(monitoredStudentAdapter);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void notifyStudentChanges(Student student){
        StudentName.setText(student.getUserName());
        StudentClass.setText(student.getStudent_class());
        studentUID = student.getUserUID();
    }

    public void getWeeklyAveragePerformance(){
        this.StudentRef.child("classroom").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // loop classroom
                for (DataSnapshot dataSnapShot : snapshot.getChildren()) {
                    String classCode = dataSnapShot.getKey();
                    Double AveragePerformanceForClass = 0.0;

                    for (DataSnapshot quizCategorySnapshot : dataSnapShot.child("quizCategory").getChildren()) {
                        String quizKey = quizCategorySnapshot.getKey();
                        Double AverageMarkForSet = 0.0;
                        QuizRef.child(quizKey).child("Sets").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot SetSnapShot : snapshot.getChildren()){
                                    // loop ranking in the sets
                                    for(DataSnapshot RankingSnapShot: SetSnapShot.child("Ranking").getChildren()){
                                        if(RankingSnapShot.exists()){
                                            if(RankingSnapShot.child("uid").getValue(String.class).equals(studentUID)){
                                                RankingSnapShot.child("score").getValue(Integer.class);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    ClassroomRef.child(classCode).child("Assignment").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot classRoomSnapshot) {
                            for (DataSnapshot assignmentSnapShot: classRoomSnapshot.getChildren()) {
                                int TaskStatus = 0;
                                Assignment assignment = assignmentSnapShot.getValue(Assignment.class);
                                // iterate submission inside the assignment
                                for (DataSnapshot submissionSnapShot : assignmentSnapShot.child("submission").getChildren()){
                                    if(submissionSnapShot.getKey().equals(studentUID)){
                                        TaskStatus = checkTaskStatus(assignment, true);
                                        break;
                                    }
                                    else{
                                        TaskStatus = checkTaskStatus(assignment, false);
                                    }
                                }
                                TotalTask++;
                                if(TaskStatus == 0){
                                    TotalCompleted++;
                                }
                                else if(TaskStatus == 1){
                                    TotalInProgress++;
                                }
                                else{
                                    TotalIncompleted++;
                                }
                            }

                            // now calculate the total average percentage
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public int checkTaskStatus(Assignment assignment, boolean isSubmit){
        Date dueDate = new Date(assignment.getDueDate());
        Date currentDate = new Date();

        // if due
        if(dueDate.before(currentDate)){
            if(isSubmit){
                return 0;
            }
            else{
                return 2;
            }
        }
        // if not due
        else if(dueDate.after(currentDate)){
            if(isSubmit){
                return 0;
            }
            else{
                return 1;
            }
        }

        return -1;
    }
}

package com.firstapp.hootnholler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.firstapp.hootnholler.adapter.Feedback_Student_List_Adapter;
import com.firstapp.hootnholler.entity.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Teacher_FeedbackStudentList extends AppCompatActivity {
    private String currentClassCode;
    private TextView classroomName, numOfStudents;
    private DatabaseReference ClassroomRef = FirebaseDatabase.getInstance().getReference("Classroom");
    private DatabaseReference StudentRef = FirebaseDatabase.getInstance().getReference("Student");
    private DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference("Users");
    private ArrayList<Student> StudentList = new ArrayList<>();
    private RecyclerView StudentRecycledView;
    private Feedback_Student_List_Adapter FeedbackStudentListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_feedback_student_list);
        currentClassCode = getIntent().getExtras().get("classCode").toString();
        classroomName = findViewById(R.id.feedback_classroom_name);
        numOfStudents = findViewById(R.id.feedback_classroom_student_num);
        FeedbackStudentListAdapter = new Feedback_Student_List_Adapter(Teacher_FeedbackStudentList.this, StudentList, currentClassCode);
        StudentRecycledView = findViewById(R.id.feedback_student_list);
        StudentRecycledView.setAdapter(FeedbackStudentListAdapter);
        StudentRecycledView.setLayoutManager(new LinearLayoutManager(this));
        getClassroomDetails();
    }

    private void getClassroomDetails(){
        StudentList.clear();
        ClassroomRef.child(currentClassCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("className").getValue(String.class) != null){
                    classroomName.setText(snapshot.child("className").getValue(String.class));
                }

                // loop for joined student in class
                for (DataSnapshot studentSnapshot: snapshot.child("StudentsJoined").getChildren()) {
                    String studentUID = studentSnapshot.getKey();
                    // get the student details
                    StudentRef.child(studentUID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Student student = snapshot.getValue(Student.class);
                            student.studentUID = studentUID;
                            // get student's user detail
                            StudentList.add(student);
                            if(FeedbackStudentListAdapter != null){
                                FeedbackStudentListAdapter.notifyDataSetChanged();
                            }
                            numOfStudents.setText(String.valueOf(StudentList.size()) + " Student(s)");
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
}
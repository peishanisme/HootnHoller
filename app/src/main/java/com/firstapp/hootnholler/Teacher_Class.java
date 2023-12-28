package com.firstapp.hootnholler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Teacher_Class extends AppCompatActivity {
    String currentClassCode;
    DatabaseReference classroom;
    TextView className, classSession,classDescription,numberofStudents;
    CardView classDetails,announcement,taskAssignment,learningMaterials;
    ImageView backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_activity_class);


        currentClassCode=getIntent().getStringExtra("classCode");
        classroom= FirebaseDatabase.getInstance().getReference("Classroom").child(currentClassCode);
        className=findViewById(R.id.className);
        classDescription=findViewById(R.id.classDescription);
        classSession=findViewById(R.id.classSession);
        numberofStudents=findViewById(R.id.studentOfClass);

        classDetails=findViewById(R.id.classDetails);
        announcement=findViewById(R.id.announcement);
        taskAssignment=findViewById(R.id.TasksAssignment);
        learningMaterials=findViewById(R.id.LearningMaterials);
        backButton=findViewById(R.id.back_button);


        classroom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String ClassName=snapshot.child("className").getValue(String.class);
                    String ClassDescription=snapshot.child("classDescription").getValue(String.class);
                    String ClassSessiom=snapshot.child("classSession").getValue(String.class);
//                    long NumberOfStudents=snapshot.child("StudentsJoined").getChildrenCount();

                    className.setText(ClassName);
                    classDescription.setText(ClassDescription);
                    classSession.setText(ClassSessiom);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Teacher_Class.this, Teacher_Announcement.class);
                intent.putExtra("classCode", currentClassCode);
                startActivity(intent);
            }
        });

        taskAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Teacher_Class.this,Teacher_UpcomingAsgm.class);
                startActivity(intent);
            }
        });

        learningMaterials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Teacher_Class.this,Teacher_LearningMaterials.class);
                startActivity(intent);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
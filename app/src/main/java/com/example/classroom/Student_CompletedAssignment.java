package com.example.classroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Student_CompletedAssignment extends AppCompatActivity {

    public ImageButton backButton;
    public TextView upcoming, due;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_completed_asgm);

        backButton = (ImageButton) findViewById(R.id.btnBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(Student_CompletedAssignment.this, Student_Class.class);
                startActivity(intent);
            }
        });

        upcoming = (TextView) findViewById(R.id.TVupcoming);
        upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Student_CompletedAssignment.this, Student_UpcomingAssignment.class);
                startActivity(intent);
            }
        });

        due = (TextView) findViewById(R.id.TVdue);
        due.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Student_CompletedAssignment.this, Student_DueAssignment.class);
                startActivity(intent);
            }
        });
    }
}
package com.example.classroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Student_DueAssignment extends AppCompatActivity {

    public TextView upcoming, completed;
    public ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_due_asgm);

        backButton = (ImageButton) findViewById(R.id.btnBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(Student_DueAssignment.this, Student_Class.class);
                startActivity(intent);
            }
        });

        upcoming = (TextView) findViewById(R.id.TVupcoming);
        upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Student_DueAssignment.this, Student_UpcomingAssignment.class);
                startActivity(intent);
            }
        });

        completed = (TextView) findViewById(R.id.TVcompleted);
        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Student_DueAssignment.this, Student_CompletedAssignment.class);
                startActivity(intent);
            }
        });
    }
}
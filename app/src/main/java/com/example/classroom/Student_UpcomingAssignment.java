package com.example.classroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Student_UpcomingAssignment extends AppCompatActivity {

    public ImageButton backButton;
    public TextView due, completed;
    public CardView tuto6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_asgm_upcoming);

        backButton = (ImageButton) findViewById(R.id.btnBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(Student_UpcomingAssignment.this, Student_Class.class);
                startActivity(intent);
            }
        });

        due = (TextView) findViewById(R.id.TVdue);
        due.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Student_UpcomingAssignment.this, Student_DueAssignment.class);
                startActivity(intent);
            }
        });

        completed = (TextView) findViewById(R.id.TVcompleted);
        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Student_UpcomingAssignment.this, Student_CompletedAssignment.class);
                startActivity(intent);
            }
        });

        tuto6 = (CardView) findViewById(R.id.tutorial6);
        tuto6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(Student_UpcomingAssignment.this, Student_AsgmDetails.class);
                startActivity(intent);
            }
        });
    }
}
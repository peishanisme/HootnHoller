package com.example.classroom;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Student_AsgmDetails extends AppCompatActivity {

    public Button addSubmisssionButton;
    public ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_asgn_details);

        addSubmisssionButton = (Button) findViewById(R.id.btnAddSubmission);
        addSubmisssionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(Student_AsgmDetails.this, Student_UploadAsgm.class);
                startActivity(intent);
            }
        });

        backButton = (ImageButton) findViewById(R.id.btnBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(Student_AsgmDetails.this, Student_UpcomingAssignment.class);
                startActivity(intent);
            }
        });
    }
}
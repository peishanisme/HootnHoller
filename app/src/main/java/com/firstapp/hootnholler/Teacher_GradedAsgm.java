package com.firstapp.hootnholler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Teacher_GradedAsgm extends AppCompatActivity {

    ImageButton back;
    TextView upcoming, RFG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_activity_graded_asgm);

        back = findViewById(R.id.back);
        upcoming = findViewById(R.id.upcoming);
        RFG = findViewById(R.id.RFG);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Teacher_GradedAsgm.this, Teacher_Class.class);
                startActivity(intent);
            }
        });

        upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Teacher_GradedAsgm.this, Teacher_UpcomingAsgm.class);
                startActivity(intent);
            }
        });

        RFG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Teacher_GradedAsgm.this, Teacher_ReadyForGrading_Asgm.class);
                startActivity(intent);
            }
        });
    }
}
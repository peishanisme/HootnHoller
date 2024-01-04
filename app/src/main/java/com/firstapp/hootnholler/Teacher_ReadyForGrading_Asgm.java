package com.firstapp.hootnholler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Teacher_ReadyForGrading_Asgm extends AppCompatActivity {
    ImageButton back;
    TextView upcoming, graded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_activity_ready_for_grading_asgm);

        back = findViewById(R.id.btnBack);
        upcoming = findViewById(R.id.upcoming);
        graded = findViewById(R.id.graded);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Teacher_ReadyForGrading_Asgm.this, Teacher_Class.class);
                startActivity(intent);
            }
        });

        upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Teacher_ReadyForGrading_Asgm.this, Teacher_UpcomingAsgm.class);
                startActivity(intent);
            }
        });

        graded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Teacher_ReadyForGrading_Asgm.this, Teacher_GradedAsgm.class);
                startActivity(intent);
            }
        });
    }

}


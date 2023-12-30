package com.firstapp.hootnholler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Teacher_UpcomingAsgm extends AppCompatActivity {

    ImageButton backButton;
    Button createAsgm;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_activity_upcoming_asgm);

        createAsgm = (Button)findViewById(R.id.createAsgm);
        back = (ImageButton)findViewById(R.id.backButton);

        createAsgm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Teacher_UpcomingAsgm.this, Teacher_CreateAss.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Teacher_UpcomingAsgm.this, Teacher_Class.class);
                startActivity(intent);
            }
        });
    }
}
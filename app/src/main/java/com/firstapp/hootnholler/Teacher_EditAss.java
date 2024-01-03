package com.firstapp.hootnholler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Teacher_EditAss extends AppCompatActivity {
    ImageButton back;
    String currentClassCode;
    TextView openDate, dueDate;
    DatabaseReference assRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        back = (ImageButton) findViewById(R.id.backButton);
        openDate = (TextView)findViewById(R.id.openDate);
        dueDate = (TextView)findViewById(R.id.dueDate);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Teacher_EditAss.this, Teacher_UpcomingAsgm.class);
                startActivity(intent);
            }
        });

        currentClassCode = getIntent().getStringExtra("classCode");
        assRef = FirebaseDatabase.getInstance().getReference("Classroom")
                .child(currentClassCode)
                .child("Assignment");

    }

}
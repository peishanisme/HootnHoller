package com.firstapp.hootnholler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Teacher_LearningMaterials extends AppCompatActivity {

    ImageButton backButton;
    Button btnPostResources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_activity_learning_materials);

        backButton = (ImageButton) findViewById(R.id.btnBack);
        btnPostResources = (Button) findViewById(R.id.btnPostResources);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Teacher_LearningMaterials.this, Teacher_Class.class);
                startActivity(intent);
            }
        });

        btnPostResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Teacher_LearningMaterials.this, Teacher_PostLM.class);
                startActivity(intent);
            }
        });




    }
}
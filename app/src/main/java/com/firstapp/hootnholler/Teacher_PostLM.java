package com.firstapp.hootnholler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Teacher_PostLM extends AppCompatActivity {
    Button btnPost;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_post_lm);

        backButton = (ImageButton) findViewById(R.id.btnBack);
        btnPost = (Button)findViewById(R.id.btnPost);




        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Teacher_PostLM.this, Teacher_LearningMaterials.class);
                startActivity(intent);
            }
        });

    }
}
package com.firstapp.hootnholler;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Student_AsgmDetails extends AppCompatActivity {

    private Button addSubmisssionButton,uploadButton;
    private ImageButton backButton;
    private TextView title,openTime,dueTime,description,fileName,submissionStatus,timeRemaining;
    private EditText uploadFileName;
    View uploadFile;
    private View submitLayout;
    String assId,currentClassCode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_asgn_details);

        title=findViewById(R.id.assTitle);
        openTime=findViewById(R.id.openTime);
        dueTime=findViewById(R.id.dueTime);
        description=findViewById(R.id.description);
        fileName=findViewById(R.id.fileName);
        submissionStatus=findViewById(R.id.submissionStatus);
        timeRemaining=findViewById(R.id.timeLeft);
        uploadFileName=findViewById(R.id.uploadFileName);
        uploadFile=findViewById(R.id.addFile);
        submitLayout=findViewById(R.id.submitLayout);
        uploadButton=findViewById(R.id.uploadButton);
        addSubmisssionButton =findViewById(R.id.btnAddSubmission);

        assId=getIntent().getStringExtra("assID");
        currentClassCode=getIntent().getStringExtra("classCode");


        submitLayout.setVisibility(View.GONE);

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
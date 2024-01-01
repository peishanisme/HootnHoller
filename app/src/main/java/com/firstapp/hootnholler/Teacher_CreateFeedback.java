package com.firstapp.hootnholler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firstapp.hootnholler.entity.Feedback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Teacher_CreateFeedback extends AppCompatActivity {

    private String studentUID, currentClassCode, userUID;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private TextView studentNameTextView;
    private EditText feedbackMsgEditText;
    private Button positiveFeedbackBtn, negativeFeedbackBtn, uploadFeedbackBtn, cancelFeedbackBtn;
    private int isPositive = -1;
    private DatabaseReference StudentRef = FirebaseDatabase.getInstance().getReference("Student");
    private DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference("Users");
    private DatabaseReference FeedbackRef = FirebaseDatabase.getInstance().getReference("Feedback");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_feedback);

        studentUID = getIntent().getExtras().get("studentUID").toString();
        currentClassCode = getIntent().getExtras().get("classCode").toString();
        userUID = mAuth.getUid();
        studentNameTextView = findViewById(R.id.feedback_student_name);
        feedbackMsgEditText = findViewById(R.id.feedback_msg_edit_text);
        positiveFeedbackBtn = findViewById(R.id.positive_feedback_button);
        negativeFeedbackBtn = findViewById(R.id.negative_feedback_button);
        uploadFeedbackBtn = findViewById(R.id.upload_feedback_button);
        cancelFeedbackBtn = findViewById(R.id.cancel_feedback_button);

        getStudentDetails();
        uploadFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFeedback();
            }
        });

        positiveFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positiveFeedbackBtn.setEnabled(false);
                positiveFeedbackBtn.setBackgroundColor(Color.GRAY);
                negativeFeedbackBtn.setBackgroundColor(Color.parseColor("#EEB88C"));
                negativeFeedbackBtn.setEnabled(true);
                isPositive = 1;
            }
        });

        negativeFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                negativeFeedbackBtn.setEnabled(false);
                negativeFeedbackBtn.setBackgroundColor(Color.GRAY);
                positiveFeedbackBtn.setBackgroundColor(Color.parseColor("#CDFB9F"));
                positiveFeedbackBtn.setEnabled(true);
                isPositive = 0;
            }
        });
    }

    private void getStudentDetails(){
        UserRef.child(studentUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentNameTextView.setText(snapshot.child("fullname").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void uploadFeedback(){
        String feedbackMsg = feedbackMsgEditText.getText() != null ? feedbackMsgEditText.getText().toString() : "";
        boolean isPositiveFeedback;
        if(isPositive == 0){
            isPositiveFeedback = false;
        }
        else if(isPositive == 1){
            isPositiveFeedback = true;
        }
        else {
            // if user did not click the positive or negative
            Toast.makeText(Teacher_CreateFeedback.this, "Please select whether positive or negative feedback.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(feedbackMsg.isEmpty()){
            // if user did not tyoe any message
            Toast.makeText(Teacher_CreateFeedback.this, "Please do not leave the feedback message empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        FeedbackRef.child(currentClassCode).child(studentUID).child(String.valueOf(System.currentTimeMillis()))
                .setValue(new Feedback(feedbackMsg, isPositiveFeedback)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Teacher_CreateFeedback.this, "Feedback uploaded successfully.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Teacher_CreateFeedback.this, Teacher_FeedbackStudentList.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(Teacher_CreateFeedback.this, "Feedback uploaded failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
package com.firstapp.hootnholler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Student_AnnouncementDetails extends AppCompatActivity {

    ImageButton btnToast, backButton;
    EditText comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_announcement_details);

        btnToast = (ImageButton) findViewById(R.id.btnSend);
        backButton = (ImageButton) findViewById(R.id.btnBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                finish();
            }
        });

        comment = findViewById(R.id.ETComment);

        btnToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from the EditText
                String inputText = comment.getText().toString();

                // Check if the inputText is not empty
                if (!inputText.isEmpty()) {
                    // Display the Toast message
                    Toast.makeText(Student_AnnouncementDetails.this, "Posted Successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    // Show a different message or handle the case where no text is entered
                    Toast.makeText(Student_AnnouncementDetails.this, "Please type something", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
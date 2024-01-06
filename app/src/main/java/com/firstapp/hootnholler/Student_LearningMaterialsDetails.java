package com.firstapp.hootnholler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firstapp.hootnholler.databinding.ActivityStudentLearningMaterialsDetailsBinding;

public class Student_LearningMaterialsDetails extends AppCompatActivity {

    public ImageButton btnToast;
    public EditText comment;
    ActivityStudentLearningMaterialsDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentLearningMaterialsDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Student_LearningMaterialsDetails.this, Student_LearningMaterialsDetails.class);
                startActivity(intent);
                finish();
            }
        });

        comment = findViewById(R.id.ETComment);
        btnToast = (ImageButton) findViewById(R.id.btnSend);
        btnToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from the EditText
                String inputText = comment.getText().toString();

                // Check if the inputText is not empty
                if (!inputText.isEmpty()) {
                    // Display the Toast message
                    Toast.makeText(Student_LearningMaterialsDetails.this, "Posted Successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    // Show a different message or handle the case where no text is entered
                    Toast.makeText(Student_LearningMaterialsDetails.this, "Please type something", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void buttonBold(View view){
        Spannable spannableString = new SpannableStringBuilder(comment.getText());
        spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                comment.getSelectionStart(),
                comment.getSelectionEnd(),
                0);

        comment.setText(spannableString);
    }

    public void buttonItalics(View view){
        Spannable spannableString = new SpannableStringBuilder(comment.getText());
        spannableString.setSpan(new StyleSpan(Typeface.ITALIC),
                comment.getSelectionStart(),
                comment.getSelectionEnd(),
                0);

        comment.setText(spannableString);
    }

    public void buttonUnderline(View view) {
        Spannable spannableString = new SpannableStringBuilder(comment.getText());
        spannableString.setSpan(new UnderlineSpan(),
                comment.getSelectionStart(),
                comment.getSelectionEnd(),
                0);

        comment.setText(spannableString);
    }
}
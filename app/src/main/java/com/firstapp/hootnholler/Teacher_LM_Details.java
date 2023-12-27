package com.firstapp.hootnholler;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;

public class Teacher_LM_Details extends AppCompatActivity {

    public EditText comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_activity_lm_details);

        comment = findViewById(R.id.ETComment);
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
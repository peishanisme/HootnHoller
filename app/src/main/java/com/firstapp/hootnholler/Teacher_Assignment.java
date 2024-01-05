package com.firstapp.hootnholler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Teacher_Assignment extends AppCompatActivity {


    public TextView upcoming,readyForGrading,graded;
    public ImageButton backButton;
    String currentClassCode;
    public FragmentContainerView fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_assignment);
        currentClassCode = getIntent().getStringExtra("classCode");

        backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Teacher_Assignment.this, Teacher_Class.class);
                intent.putExtra("classCode",currentClassCode);
                startActivity(intent);
            }
        });

        fragmentContainer = findViewById(R.id.assList);

        upcoming = findViewById(R.id.TVupcoming);
        upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new Teacher_Ass_Upcoming_Fragment(), currentClassCode);

                // Update text style
                upcoming.setTypeface(null, Typeface.BOLD);
                upcoming.setPaintFlags(upcoming.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                readyForGrading.setTypeface(null, Typeface.NORMAL);
                readyForGrading.setPaintFlags(readyForGrading.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                graded.setTypeface(null, Typeface.NORMAL);
                graded.setPaintFlags(graded.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
            }
        });

        readyForGrading = findViewById(R.id.TVreadyForGrading);
        readyForGrading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load Past Assignment Fragment
                loadFragment(new Teacher_Ass_RFG_Fragment(), currentClassCode);

                // Update text style
                readyForGrading.setTypeface(null, Typeface.BOLD);
                readyForGrading.setPaintFlags(readyForGrading.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                upcoming.setTypeface(null, Typeface.NORMAL);
                upcoming.setPaintFlags(upcoming.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                graded.setTypeface(null, Typeface.NORMAL);
                graded.setPaintFlags(graded.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
            }
        });
        graded = findViewById(R.id.TVgraded);
        graded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load Past Assignment Fragment
                loadFragment(new Teacher_Ass_Graded_Fragment(), currentClassCode);

                // Update text style
                graded.setTypeface(null, Typeface.BOLD);
                graded.setPaintFlags(readyForGrading.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                upcoming.setTypeface(null, Typeface.NORMAL);
                upcoming.setPaintFlags(upcoming.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                readyForGrading.setTypeface(null, Typeface.NORMAL);
                readyForGrading.setPaintFlags(readyForGrading.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
            }
        });
    }

    private void loadFragment(Fragment fragment, String classCode) {
        Bundle bundle = new Bundle();
        bundle.putString("classCode", classCode);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(fragmentContainer.getId(), fragment)
                .commit();
    }
}
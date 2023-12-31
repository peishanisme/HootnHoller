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
import android.widget.ImageView;
import android.widget.TextView;

import com.firstapp.hootnholler.databinding.ActivityStudentAsgmBinding;

public class Student_Assignment extends AppCompatActivity {

    public TextView past, ongoing;
    public ImageView backButton;
    String currentClassCode;
    public FragmentContainerView fragmentContainer;
    ActivityStudentAsgmBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentAsgmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        currentClassCode = getIntent().getStringExtra("classCode");

        backButton = binding.back;

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Student_Assignment.this, Student_Class.class);
                intent.putExtra("classCode",currentClassCode);
                startActivity(intent);
                finish();
            }
        });

        fragmentContainer = findViewById(R.id.assList);
        loadFragment(new Student_OngoingAss_Fragment(), currentClassCode);

        ongoing = findViewById(R.id.TVongoing);
        ongoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new Student_OngoingAss_Fragment(), currentClassCode);

                // Update text style
                ongoing.setTypeface(null, Typeface.BOLD);
                ongoing.setPaintFlags(ongoing.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                past.setTypeface(null, Typeface.NORMAL);
                past.setPaintFlags(past.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
            }
        });

        past = findViewById(R.id.TVpast);
        past.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load Past Assignment Fragment
                loadFragment(new Student_PastAss_Fragment(), currentClassCode);

                // Update text style
                past.setTypeface(null, Typeface.BOLD);
                past.setPaintFlags(past.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                ongoing.setTypeface(null, Typeface.NORMAL);
                ongoing.setPaintFlags(ongoing.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
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

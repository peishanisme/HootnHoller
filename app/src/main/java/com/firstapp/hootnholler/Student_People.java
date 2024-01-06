package com.firstapp.hootnholler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.firstapp.hootnholler.adapter.Student_People_ArrayAdapter;
import com.firstapp.hootnholler.adapter.Student_People_RecyclerViewAdapter;
import com.firstapp.hootnholler.databinding.ActivityStudentPeopleBinding;

import java.util.ArrayList;

public class Student_People extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Student_People_ArrayAdapter> arrayList = new ArrayList<Student_People_ArrayAdapter>();
    ActivityStudentPeopleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentPeopleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = findViewById(R.id.student_people_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        arrayList.add(new Student_People_ArrayAdapter(R.drawable.user1, "Title"));
        Student_People_RecyclerViewAdapter recyclerViewAdapter = new Student_People_RecyclerViewAdapter(this,arrayList);
        recyclerView.setAdapter(recyclerViewAdapter);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
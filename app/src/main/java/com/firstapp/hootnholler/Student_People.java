package com.firstapp.hootnholler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.firstapp.hootnholler.adapter.Student_People_ArrayAdapter;
import com.firstapp.hootnholler.adapter.Student_People_RecyclerViewAdapter;

import java.util.ArrayList;

public class Student_People extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Student_People_ArrayAdapter> arrayList = new ArrayList<Student_People_ArrayAdapter>();

    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_people);

        recyclerView = findViewById(R.id.student_people_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        arrayList.add(new Student_People_ArrayAdapter(R.drawable.user1, "Title"));
        Student_People_RecyclerViewAdapter recyclerViewAdapter = new Student_People_RecyclerViewAdapter(this,arrayList);
        recyclerView.setAdapter(recyclerViewAdapter);


        backButton = (ImageButton) findViewById(R.id.btnBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
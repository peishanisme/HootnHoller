package com.example.classroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class Student_Classroom extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Classroom_ArrayAdapter> arrayList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    Classroom_RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_classroom);

        recyclerView.findViewById(R.id.classList);
        layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        arrayList.add(new Classroom_ArrayAdapter("Title","Session", "Day & Time", "Educator Name"));
        recyclerViewAdapter = new Classroom_RecyclerViewAdapter(this, arrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}
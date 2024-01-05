package com.firstapp.hootnholler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.firstapp.hootnholler.adapter.LM_ArrayAdapter;
import com.firstapp.hootnholler.adapter.LM_RecyclerViewAdapter;

import java.util.ArrayList;

public class Student_LearningMaterials extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<LM_ArrayAdapter> arrayList = new ArrayList<LM_ArrayAdapter>();

    public ImageButton backButton;
    public CardView resource1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        recyclerView = findViewById(R.id.lm_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        arrayList.add(new LM_ArrayAdapter("Title", "Posted On"));
        LM_RecyclerViewAdapter recyclerViewAdapter = new LM_RecyclerViewAdapter(this,arrayList);
        recyclerView.setAdapter(recyclerViewAdapter);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_learning_materials);

        backButton = (ImageButton) findViewById(R.id.btnBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        resource1 = (CardView) findViewById(R.id.lm1);
        resource1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Student_LearningMaterials.this, Student_LearningMaterialsDetails.class);
                startActivity(intent);
            }
        });
    }
}
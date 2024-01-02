package com.firstapp.hootnholler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firstapp.hootnholler.adapter.Announcement_Adapter;
import com.firstapp.hootnholler.adapter.LMAdapter;
import com.firstapp.hootnholler.entity.Learning_Materials;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Teacher_LearningMaterials extends AppCompatActivity {
    TextView noAss;
    Button postButton;
    String currentClassCode;
    private List<Learning_Materials> lmList;
    private LMAdapter LMAdapter;
    private RecyclerView LMList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_activity_learning_materials);
        currentClassCode = getIntent().getStringExtra("classCode");

        noAss=findViewById(R.id.noAss);
        LMList=findViewById(R.id.materialList);
        postButton=findViewById(R.id.postbutton);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LMList.setLayoutManager(layoutManager);
        lmList = new ArrayList<>(); // Initialize the list
        LMAdapter = new LMAdapter(Teacher_LearningMaterials.this,lmList,currentClassCode);
        LMList.setAdapter(LMAdapter);

        DatabaseReference LMRef = FirebaseDatabase.getInstance().getReference("Classroom")
                .child(currentClassCode)
                .child("Learning Materials");

        LMRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                lmList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    Learning_Materials file = dataSnapshot.getValue(Learning_Materials.class);
                    file.setLMid(dataSnapshot.getKey());
                    if (file != null) {
                        lmList.add(file);

                    }
                }if (lmList.isEmpty()) {
                        noAss.setVisibility(View.VISIBLE);
                    } else {
                        noAss.setVisibility(View.GONE);

                }
                LMAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Teacher_LearningMaterials.this,Teacher_Upload_LM.class);
                intent.putExtra("classCode",currentClassCode);
                startActivity(intent);
            }
        });


    }
}
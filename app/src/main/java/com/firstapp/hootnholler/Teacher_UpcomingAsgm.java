package com.firstapp.hootnholler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firstapp.hootnholler.adapter.Asgm_ArrayAdapter;
import com.firstapp.hootnholler.entity.Assignment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Teacher_UpcomingAsgm extends AppCompatActivity {

    Button createAsgm;
    ImageButton back;
    TextView noUpcomingAss;
    String currentClassCode;
    RecyclerView recyclerView;
    DatabaseReference database;
    Asgm_ArrayAdapter asgm_ArrayAdapter;
    ArrayList <Assignment> asgmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_activity_upcoming_asgm);

        createAsgm = (Button)findViewById(R.id.createAsgm);
        back = (ImageButton)findViewById(R.id.backButton);
        noUpcomingAss = (TextView) findViewById(R.id.noUpcomingAss);

        currentClassCode = getIntent().getStringExtra("classCode");

        recyclerView = findViewById(R.id.upcomingList);
        database = FirebaseDatabase.getInstance().getReference("Classroom")
                        .child(currentClassCode)
                        .child("Assignment")
        ;

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        asgmList = new ArrayList<>();
        asgm_ArrayAdapter = new Asgm_ArrayAdapter(this, asgmList);
        recyclerView.setAdapter(asgm_ArrayAdapter);


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Assignment asgm = dataSnapshot.getValue(Assignment.class);
                    asgm.setAssKey(dataSnapshot.getKey());
                    if (asgm != null) {
                        asgmList.add(asgm);
                    }
                }
                if (asgmList.isEmpty()) {
                    noUpcomingAss.setVisibility(View.VISIBLE);
                } else {
                    noUpcomingAss.setVisibility(View.GONE);

                }

                asgm_ArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        createAsgm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Teacher_UpcomingAsgm.this, Teacher_CreateAss.class);
                intent.putExtra("classCode",currentClassCode);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Teacher_UpcomingAsgm.this, Teacher_Class.class);
                startActivity(intent);
            }
        });
    }
}
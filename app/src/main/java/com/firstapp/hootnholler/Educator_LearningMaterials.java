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

import com.firstapp.hootnholler.adapter.LMAdapter;
import com.firstapp.hootnholler.entity.Learning_Materials;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Educator_LearningMaterials extends AppCompatActivity {
    TextView noAss;
    Button postButton;
    ImageButton back;
    String currentClassCode,classOwner;
    private List<Learning_Materials> lmList;
    private LMAdapter LMAdapter;
    private RecyclerView LMList;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    String uid=mAuth.getUid().toString();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_activity_learning_materials);
        currentClassCode = getIntent().getStringExtra("classCode");

        noAss=findViewById(R.id.noAss);
        LMList=findViewById(R.id.materialList);
        postButton=findViewById(R.id.postbutton);
        back = findViewById(R.id.back);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LMList.setLayoutManager(layoutManager);
        lmList = new ArrayList<>(); // Initialize the list
        LMAdapter = new LMAdapter(Educator_LearningMaterials.this,lmList,currentClassCode);
        LMList.setAdapter(LMAdapter);

        DatabaseReference ClassroomRef = FirebaseDatabase.getInstance().getReference("Classroom")
                .child(currentClassCode);

        ClassroomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the existing list
                classOwner=snapshot.child("classOwner").getValue(String.class);

                if(uid.equals(classOwner)){
                    postButton.setVisibility(View.VISIBLE);
                    postButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Display the pop-up window
                            Intent intent=new Intent(Educator_LearningMaterials.this, Educator_Upload_LM.class);
                            intent.putExtra("classCode",currentClassCode);
                            startActivity(intent);                        }
                    });}else{

                    postButton.setVisibility(View.GONE);
                }




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


//        postButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(Educator_LearningMaterials.this, Educator_Upload_LM.class);
//                intent.putExtra("classCode",currentClassCode);
//                startActivity(intent);
//            }
//        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Educator_LearningMaterials.this, Educator_Class.class);
                startActivity(intent);
            }
        });

    }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
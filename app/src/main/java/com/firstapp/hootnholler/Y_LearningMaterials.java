package com.firstapp.hootnholler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firstapp.hootnholler.adapter.LMAdapter;
import com.firstapp.hootnholler.databinding.TeacherActivityLearningMaterialsBinding;
import com.firstapp.hootnholler.entity.Learning_Materials;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Y_LearningMaterials extends AppCompatActivity {
    TextView noAss;
    Button postButton;
    ImageView back;
    String currentClassCode,classOwner;
    private List<Learning_Materials> lmList;
    private LMAdapter LMAdapter;
    private RecyclerView LMList;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    String uid=mAuth.getUid().toString();
    TeacherActivityLearningMaterialsBinding binding;
    boolean isCurrentUserClassOwner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TeacherActivityLearningMaterialsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        currentClassCode = getIntent().getStringExtra("classCode");

        noAss=findViewById(R.id.noAss);
        LMList=findViewById(R.id.materialList);
        postButton=findViewById(R.id.postbutton);
        back = binding.back;


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LMList.setLayoutManager(layoutManager);
        lmList = new ArrayList<>(); // Initialize the list
        LMAdapter = new LMAdapter(Y_LearningMaterials.this,lmList,currentClassCode);
        LMList.setAdapter(LMAdapter);

        DatabaseReference ClassroomRef = FirebaseDatabase.getInstance().getReference("Classroom")
                .child(currentClassCode);

        ClassroomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the existing list
                classOwner = snapshot.child("classOwner").getValue(String.class);

                if (uid.equals(classOwner)) {
                    isCurrentUserClassOwner = true;
                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Y_LearningMaterials.this, Educator_Class.class);
                            intent.putExtra("classCode", currentClassCode);
                            startActivity(intent);
                        }
                    });
                    postButton.setVisibility(View.VISIBLE);
                    postButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Display the pop-up window
                            Intent intent = new Intent(Y_LearningMaterials.this, Educator_Upload_LM.class);
                            intent.putExtra("classCode", currentClassCode);
                            startActivity(intent);
                        }
                    });
                } else {
                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Y_LearningMaterials.this, Student_Class.class);
                            intent.putExtra("classCode", currentClassCode);
                            startActivity(intent);
                        }
                    });
                    postButton.setVisibility(View.GONE);
                }


                DatabaseReference LMRef = FirebaseDatabase.getInstance().getReference("Classroom")
                        .child(currentClassCode)
                        .child("Learning Materials");

                LMRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        lmList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Learning_Materials file = dataSnapshot.getValue(Learning_Materials.class);
                            file.setLMid(dataSnapshot.getKey());
                            if (file != null) {
                                lmList.add(file);

                            }
                        }
                        if (lmList.isEmpty()) {
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
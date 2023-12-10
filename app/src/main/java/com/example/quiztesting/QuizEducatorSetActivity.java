package com.example.quiztesting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.quiztesting.Adapters.GrideAdapter;
import com.example.quiztesting.databinding.ActivityQuizEducatorSetBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuizEducatorSetActivity extends AppCompatActivity {

    ActivityQuizEducatorSetBinding binding;
    FirebaseDatabase database;
    GrideAdapter adapter;
    int setNum;
    String keyCtg;
    ArrayList<String> list;
    Dialog progressDialog;
    DatabaseReference referenceSetKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizEducatorSetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        keyCtg = intent.getStringExtra("key");
        setNum = intent.getIntExtra("sets", 0);

        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.item_upload_progress_dialog);
        if(progressDialog.getWindow()!=null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(true);
        }

        database = FirebaseDatabase.getInstance();
        list = new ArrayList<>();
        referenceSetKey = database.getReference().child("SetKey").child(keyCtg);

        database.getReference().child("Categories").child(keyCtg).child("setNum").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Integer num = snapshot.getValue(Integer.class);
                    if(num != null) {
                        setNum = num;
                    }
                    database.getReference().child("SetKey").child(keyCtg).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                list.clear();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String setKey = snapshot.getValue(String.class);
                                    list.add(setKey);
                                }

                                if(list!=null && !list.isEmpty() ) {
                                    adapter = new GrideAdapter(QuizEducatorSetActivity.this, setNum, keyCtg, list);
                                    binding.gridView.setAdapter(adapter);
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(QuizEducatorSetActivity.this, "Fail to access.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.addSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                binding.addSet.setEnabled(false);

                String keySet = referenceSetKey.push().getKey();
                list.add(keySet);
                setNum++;

                database.getReference().child("Categories").child(keyCtg)
                        .child("setNum").setValue(setNum).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                referenceSetKey.setValue(list).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        if(adapter == null) {
                                            adapter = new GrideAdapter(QuizEducatorSetActivity.this, setNum, keyCtg, list);
                                            binding.gridView.setAdapter(adapter);
                                        }
                                        else {
                                            adapter.addSetAndUpdate();
                                        }
                                        progressDialog.dismiss();
                                        Toast.makeText(QuizEducatorSetActivity.this, "Set " + setNum + " is added successfully.", Toast.LENGTH_SHORT).show();
                                        binding.addSet.setEnabled(true);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(QuizEducatorSetActivity.this, "Fail to add a new set. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(QuizEducatorSetActivity.this, "Fail to add a new set. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizEducatorSetActivity.this, QuizEducatorCategoryActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
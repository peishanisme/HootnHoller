package com.example.quiztesting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.quiztesting.Adapters.SetAdapter;
import com.example.quiztesting.Models.QuestionModel;
import com.example.quiztesting.Models.SetModel;
import com.example.quiztesting.databinding.ActivityQuizStudentSetBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuizStudentSetActivity extends AppCompatActivity implements RecyViewInterface {

    ActivityQuizStudentSetBinding binding;
    FirebaseDatabase database;
    DatabaseReference referenceSets;
    String keyCtg, uid;
    ArrayList<String> keySetList;
    SetAdapter adapter;
    ArrayList<SetModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizStudentSetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        keySetList = new ArrayList<>();

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        keyCtg = intent.getStringExtra("keyCtg");
        keySetList = intent.getStringArrayListExtra("keySetList");

        list = new ArrayList<>();

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.recySet.setLayoutManager(layoutManager);

        adapter = new SetAdapter(this, this,list);
        binding.recySet.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        referenceSets = database.getReference().child("Categories").child(keyCtg).child("Sets");

        list.clear();

        if(keySetList != null) {
            referenceSets.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            SetModel model = snapshot.getValue(SetModel.class);
                            if (keySetList.contains(model.getSetKey())) {
                                list.add(model);
                            }
                        }
                        adapter.notifyDataSetChanged(); // Notify adapter of data change
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(QuizStudentSetActivity.this, "Error in retrieving set models", Toast.LENGTH_SHORT).show();
                }
            });
        }

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizStudentSetActivity.this, QuizStudentCategoryActivity.class);

                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onItemLongClick(int position) {

    }

    @Override
    public void onItemClick(int position) {
        SetModel model = list.get(position);
        ArrayList<QuestionModel> questions = new ArrayList<>();
        ArrayList<String> keyQuestionList = new ArrayList<>();

        referenceSets.child(model.getSetKey()).child("Questions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        QuestionModel questionModel = dataSnapshot.getValue(QuestionModel.class);
                        questions.add(questionModel);
                        keyQuestionList.add(questionModel.getKeyQuestion());
                    }
                    referenceSets.child(model.getSetKey()).child("Answers").child(uid).child("status").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                String status = snapshot.getValue(String.class);
                                if(status.equals("In progress")) {
                                    referenceSets.child(model.getSetKey()).child("Answers").child(uid).child("progress").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()) {
                                                Integer progress = snapshot.getValue(Integer.class);

                                                Intent intent = new Intent(QuizStudentSetActivity.this, QuizStudentDoQuizActivity.class);
                                                intent.putExtra("uid", uid);
                                                intent.putExtra("setName", model.getSetName());
                                                intent.putExtra("keyCtg", keyCtg);
                                                intent.putExtra("keySet", model.getSetKey());
                                                intent.putExtra("keySetList", keySetList);
                                                intent.putParcelableArrayListExtra("questions", questions);
                                                intent.putExtra("queIndex", progress);

                                                startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                } else if(status.equals("Completed")) {
                                    Intent intent = new Intent(QuizStudentSetActivity.this, QuizStudentLeaderboardActivity.class);
                                    intent.putExtra("uid", uid);
                                    intent.putExtra("keyCtg", keyCtg);
                                    intent.putExtra("keySet", model.getSetKey());
                                    intent.putExtra("keySetList", keySetList);
                                    intent.putExtra("keyQuestionList", keyQuestionList);
                                    intent.putExtra("setName", model.getSetName());
                                    intent.putParcelableArrayListExtra("questions", questions);

                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(QuizStudentSetActivity.this, QuizStudentDoQuizActivity.class);
                                    intent.putExtra("uid", uid);
                                    intent.putExtra("setName", model.getSetName());
                                    intent.putExtra("keyCtg", keyCtg);
                                    intent.putExtra("keySet", model.getSetKey());
                                    intent.putExtra("keySetList", keySetList);
                                    intent.putParcelableArrayListExtra("questions", questions);

                                    startActivity(intent);
                                }
                            } else {
                                Intent intent = new Intent(QuizStudentSetActivity.this, QuizStudentDoQuizActivity.class);
                                intent.putExtra("uid", uid);
                                intent.putExtra("setName", model.getSetName());
                                intent.putExtra("keyCtg", keyCtg);
                                intent.putExtra("keySet", model.getSetKey());
                                intent.putExtra("keySetList", keySetList);
                                intent.putParcelableArrayListExtra("questions", questions);

                                startActivity(intent);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(QuizStudentSetActivity.this, "Error in checking previous progress", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(QuizStudentSetActivity.this, "Your teacher haven't posted any question.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuizStudentSetActivity.this, "Error in retrieving question model", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
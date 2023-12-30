package com.example.quiztesting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.quiztesting.Adapters.QuizCategoryAdapter;
import com.example.quiztesting.Models.QuizModel;
import com.example.quiztesting.Models.TaskStatus;
import com.example.quiztesting.Models.PostedQuizModel;
import com.example.quiztesting.Models.TaskToDo;
import com.example.quiztesting.databinding.ActivityQuizStudentToDoBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class QuizStudentToDoActivity extends AppCompatActivity {

    ActivityQuizStudentToDoBinding binding;
    FirebaseDatabase database;
    DatabaseReference referenceStudentQuiz;
    ArrayList<String> ctgList;
    QuizCategoryAdapter ctgAdapter;
    String uid;
    TaskStatus taskStatus;
    TaskToDo taskToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizStudentToDoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        taskToDo = intent.getParcelableExtra("taskToDo");
        taskStatus = intent.getParcelableExtra("taskStatus");
        uid = intent.getStringExtra("uid");

        binding.allCompletedTV.setVisibility(View.INVISIBLE);

        ctgList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyQuizCtgToDo.setLayoutManager(layoutManager);

        ctgAdapter = new QuizCategoryAdapter(this, ctgList);
        binding.recyQuizCtgToDo.setAdapter(ctgAdapter);

        if(taskToDo.getQuizToDo().isEmpty()) {
            binding.allCompletedTV.setVisibility(View.VISIBLE);
        } else {
            ctgList.clear();
            ctgList.addAll(taskToDo.getQuizToDo().keySet());
            ctgAdapter.notifyDataSetChanged();
            for(String ctg : ctgList) {
                ArrayList<String> quizzes = taskToDo.getQuizToDo().get(ctg);

                for(int i=0; i<quizzes.size(); i++) {
                    String status = taskStatus.getHashMap().get(quizzes.get(i));
                    addQuizModel(status, ctg, quizzes.get(i));
                }
            }
        }

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizStudentToDoActivity.this, QuizStudentCategoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addQuizModel(String status, String ctgKey, String setKey) {
        QuizModel quizModel = new QuizModel();
        referenceStudentQuiz = database.getReference()
                .child("Student").child(uid)
                .child("quiz");
        referenceStudentQuiz.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot classSnapshot : snapshot.getChildren()) {
                        referenceStudentQuiz.child(classSnapshot.getKey()).child(ctgKey).child("setKeyInfo").child(setKey).child("dueDate").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()) {
                                    Long timestamp = snapshot.getValue(Long.class);
                                    if(timestamp != null) {
                                        Date date = new Date(timestamp);

                                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                        String formattedDate = sdf.format(date);

                                        quizModel.setDueDate(formattedDate);
                                        quizModel.setStatus(status);
                                        database.getReference().child("Categories").child(ctgKey).child("Sets").child(setKey).child("setName").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists()) {
                                                    quizModel.setTitle(snapshot.getValue(String.class));
                                                    ctgAdapter.addQuizModel(quizModel, ctgKey);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
package com.example.quiztesting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.quiztesting.Adapters.QuestionAdapter;
import com.example.quiztesting.Models.QuestionModel;
import com.example.quiztesting.databinding.ActivityQuizEducatorQuestionBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuizEducatorQuestionActivity extends AppCompatActivity implements RecyViewInterface {

    ActivityQuizEducatorQuestionBinding binding;
    int setNum;
    String keyCtg, keySet, keyQuestion;
    FirebaseDatabase database;
    QuestionAdapter adapter;
    ArrayList<QuestionModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizEducatorQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = new ArrayList<>();

        Intent intent = getIntent();
        keyCtg = intent.getStringExtra("key");
        keySet = intent.getStringExtra("keySet");
        setNum = intent.getIntExtra("currSetNum", -1);

        if (keyCtg == null || keySet == null) {
            Toast.makeText(this, "Missing data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyQuestion.setLayoutManager(layoutManager);

        adapter = new QuestionAdapter(this, list,this);
        binding.recyQuestion.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        database.getReference().child("Sets").child(keyCtg).child(keySet).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if(snapshot.exists()) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        QuestionModel model = dataSnapshot.getValue(QuestionModel.class);
                        list.add(model);
                    }
                    adapter.notifyDataSetChanged();
                    if(setNum == -1) {
                        setNum = list.size() + 1;
                    }
                }
                /*adapter = new QuestionAdapter(QuizEducatorQuestionActivity.this, list,QuizEducatorQuestionActivity.this);
                binding.recyQuestion.setAdapter(adapter);*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuizEducatorQuestionActivity.this, "Set not exist", Toast.LENGTH_SHORT).show();
            }
        });

        binding.addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizEducatorQuestionActivity.this, QuizEducatorAddQuestionActivity.class);
                intent.putExtra("key", keyCtg);
                intent.putExtra("keySet", keySet);
                intent.putExtra("currSetNum", setNum);
                intent.putExtra("questionNo", list.size() + 1);
                startActivity(intent);
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizEducatorQuestionActivity.this, QuizEducatorSetActivity.class);
                intent.putExtra("key", keyCtg);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onItemLongClick(int position) {
        QuestionModel selectedQuestion = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete Question " + (position + 1) + "?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteQuestionFromFirebase(selectedQuestion, position);
            }
        }).setNegativeButton("No", null);
        builder.show();
    }

    private void deleteQuestionFromFirebase(QuestionModel selectedQuestion, int position) {
        database.getReference().child("Sets")
                .child(keyCtg).child(keySet).child(selectedQuestion.getKeyQuestion()).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        list.remove(position);
                        Toast.makeText(QuizEducatorQuestionActivity.this, "Question " + (position + 1) +" is deleted successfully", Toast.LENGTH_SHORT).show();
                        adapter.notifyItemRemoved(position);
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(QuizEducatorQuestionActivity.this, "Fail to delete", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        QuestionModel model = list.get(position);
        Intent intent = new Intent(QuizEducatorQuestionActivity.this, QuizEducatorAddQuestionActivity.class);
        intent.putExtra("key", keyCtg);
        intent.putExtra("keySet", keySet);
        intent.putExtra("keyQuestion", model.getKeyQuestion());
        intent.putExtra("currSetNum", setNum);
        intent.putExtra("questionNo", position + 1);

        startActivity(intent);
    }
}
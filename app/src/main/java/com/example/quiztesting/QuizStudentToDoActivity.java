package com.example.quiztesting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.example.quiztesting.Adapters.QuestionAdapter;
import com.example.quiztesting.Models.HashMapParcelable;
import com.example.quiztesting.databinding.ActivityQuizStudentToDoBinding;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class QuizStudentToDoActivity extends AppCompatActivity {

    ActivityQuizStudentToDoBinding binding;
    FirebaseDatabase database;
    HashMapParcelable incomplete, inProgress;
    HashMap<String, String> hashMapIncomplete, hashMapInProgress;
    HashMap<String, Integer> hashMapProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   /*     binding = ActivityQuizStudentToDoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        incomplete = intent.getParcelableExtra("incomplete");
        inProgress = intent.getParcelableExtra("inProgress");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyIncomplete.setLayoutManager(layoutManager);

        adapter = new QuestionAdapter(this, list,this);
        binding.recyQuestion.setAdapter(adapter);

*/
    }
}
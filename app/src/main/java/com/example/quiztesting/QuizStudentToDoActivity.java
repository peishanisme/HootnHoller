package com.example.quiztesting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.example.quiztesting.databinding.ActivityQuizStudentToDoBinding;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class QuizStudentToDoActivity extends AppCompatActivity {

    ActivityQuizStudentToDoBinding binding;
    FirebaseDatabase database;
    String uid;
    ArrayList<String> keyCtgList, keySetList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizStudentToDoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        keyCtgList = intent.getStringArrayListExtra("keyCtgList");
        keySetList = intent.getStringArrayListExtra("keySetList");

        database = FirebaseDatabase.getInstance();


    }
}
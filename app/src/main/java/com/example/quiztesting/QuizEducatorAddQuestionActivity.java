package com.example.quiztesting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quiztesting.Models.QuestionModel;
import com.example.quiztesting.databinding.ActivityQuizEducatorAddQuestionBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuizEducatorAddQuestionActivity extends AppCompatActivity {

    ActivityQuizEducatorAddQuestionBinding binding;
    FirebaseDatabase database;
    int setNum, questionNo;
    String keyCtg, keySet, keyQuestion, category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizEducatorAddQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        keyCtg = intent.getStringExtra("key");
        keySet = intent.getStringExtra("keySet");
        keyQuestion = intent.getStringExtra("keyQuestion");
        category = intent.getStringExtra("category");
        questionNo = intent.getIntExtra("questionNo", -1);
        setNum = intent.getIntExtra("currSetNum", -1);

        database = FirebaseDatabase.getInstance();

        if(questionNo == -1) {
            finish();
            return;
        }

        if(keyQuestion != null) {
            database.getReference().child("Sets").child(keyCtg).child(keySet).child(keyQuestion)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                QuestionModel model = snapshot.getValue(QuestionModel.class);
                                binding.enterQuestion.setText(model.getQuestion());
                                binding.enterAnsA.setText(model.getOptionA());
                                binding.enterAnsB.setText(model.getOptionB());
                                binding.enterAnsC.setText(model.getOptionC());
                                binding.enterAnsD.setText(model.getOptionD());

                                String[] options = {model.getOptionA(), model.getOptionB(), model.getOptionC(), model.getOptionD()};
                                for(int i=0; i<options.length; i++) {
                                    if(model.getCorrectAns().equals(options[i])) {
                                        binding.RGAnswers.check(binding.RGAnswers.getChildAt(i).getId());
                                        break;
                                    }
                                }

                                binding.uploadBtn.setText("Update");

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(QuizEducatorAddQuestionActivity.this, "Question not exist", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        binding.textView.setText("Set" + setNum + " / Q" + questionNo);

        binding.uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String questionText = binding.enterQuestion.getText().toString().trim();
                if (questionText.isEmpty()) {
                    Toast.makeText(QuizEducatorAddQuestionActivity.this, "Please enter the question", Toast.LENGTH_SHORT).show();
                    return;
                }

                int checkedRadioButtonId = binding.RGAnswers.getCheckedRadioButtonId();

                if (checkedRadioButtonId == -1) {
                    Toast.makeText(QuizEducatorAddQuestionActivity.this, "Please mark the correct option", Toast.LENGTH_SHORT).show();
                    return;
                }

                EditText selectedEditText = null;

                if (checkedRadioButtonId == R.id.A) {
                    selectedEditText = binding.enterAnsA;
                } else if (checkedRadioButtonId == R.id.B) {
                    selectedEditText = binding.enterAnsB;
                } else if (checkedRadioButtonId == R.id.C) {
                    selectedEditText = binding.enterAnsC;
                } else if (checkedRadioButtonId == R.id.D) {
                    selectedEditText = binding.enterAnsD;
                }

                String correctAnswer;
                if (selectedEditText != null) {
                    correctAnswer = selectedEditText.getText().toString().trim();
                } else {
                    Toast.makeText(QuizEducatorAddQuestionActivity.this, "Please mark the correct option", Toast.LENGTH_SHORT).show();
                    return;
                }

                String optionA = ((EditText) binding.enterAnsA).getText().toString().trim();
                String optionB = ((EditText) binding.enterAnsB).getText().toString().trim();
                String optionC = ((EditText) binding.enterAnsC).getText().toString().trim();
                String optionD = ((EditText) binding.enterAnsD).getText().toString().trim();

                QuestionModel model = new QuestionModel();
                model.setQuestion(questionText);
                model.setOptionA(optionA);
                model.setOptionB(optionB);
                model.setOptionC(optionC);
                model.setOptionD(optionD);
                model.setCorrectAns(correctAnswer);

                DatabaseReference questionsRef = database.getReference().child("Sets").child(keyCtg).child(keySet);
                if(keyQuestion != null) {
                    model.setKeyQuestion(keyQuestion);
                    questionsRef.child(keyQuestion).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Question updated successfully", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to update question. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    String questionKey = questionsRef.push().getKey();
                    model.setKeyQuestion(questionKey);
                    questionsRef.child(questionKey).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Question added successfully", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to add question. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                Intent intent = new Intent(QuizEducatorAddQuestionActivity.this, QuizEducatorQuestionActivity.class);
                intent.putExtra("key", keyCtg);
                intent.putExtra("keySet", keySet);
                intent.putExtra("category", category);
                intent.putExtra("currSetNum", setNum);
                startActivity(intent);
                finish();

            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizEducatorAddQuestionActivity.this, QuizEducatorQuestionActivity.class);
                intent.putExtra("key", keyCtg);
                intent.putExtra("keySet", keySet);
                intent.putExtra("category", category);
                intent.putExtra("currSetNum", setNum);
                startActivity(intent);
                finish();
            }
        });
    }
}
package com.example.quiztesting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.quiztesting.Adapters.QuestionAdapter;
import com.example.quiztesting.Models.CategoryModel;
import com.example.quiztesting.Models.QuestionModel;
import com.example.quiztesting.databinding.ActivityQuizEducatorQuestionBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuizEducatorQuestionActivity extends AppCompatActivity implements RecyViewInterface {

    ActivityQuizEducatorQuestionBinding binding;
    String uid, keyCtg, keySet, keyQuestion, imageUrl;
    FirebaseDatabase database;
    DatabaseReference referenceQuestions, referenceStudent;
    QuestionAdapter adapter;
    ArrayList<QuestionModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizEducatorQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = new ArrayList<>();

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        keyCtg = intent.getStringExtra("key");
        keySet = intent.getStringExtra("keySet");
        imageUrl = intent.getStringExtra("categoryImage");

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
        referenceQuestions = database.getReference().child("Categories").child(keyCtg).child("Sets").child(keySet).child("Questions");
        referenceStudent = database.getReference().child("Student");
        referenceQuestions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if(snapshot.exists()) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        QuestionModel model = dataSnapshot.getValue(QuestionModel.class);
                        list.add(model);
                    }
                    adapter.notifyItemInserted(list.size());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuizEducatorQuestionActivity.this, "Set not exist", Toast.LENGTH_SHORT).show();
            }
        });

        binding.addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getReference().child("Categories").child(keyCtg).child("postedSet").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            ArrayList<String> postedSet = (ArrayList<String>) snapshot.getValue();

                            if(postedSet != null && postedSet.contains(keySet)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(QuizEducatorQuestionActivity.this);
                                builder.setTitle("Unable to Add Question");
                                builder.setMessage("You cannot add question in this set because it's already been posted to students. " +
                                        "\n\nTo modify this set, you need to cancel the posting to students. " +
                                        "\nYou can cancel the posting by long-clicking on the respective classroom.");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.show();
                            } else {
                                Intent intent = new Intent(QuizEducatorQuestionActivity.this, QuizEducatorAddQuestionActivity.class);
                                intent.putExtra("uid", uid);
                                intent.putExtra("key", keyCtg);
                                intent.putExtra("keySet", keySet);
                                intent.putExtra("questionNo", list.size() + 1);
                                intent.putExtra("categoryImage", imageUrl);
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(QuizEducatorQuestionActivity.this, QuizEducatorAddQuestionActivity.class);
                            intent.putExtra("uid", uid);
                            intent.putExtra("key", keyCtg);
                            intent.putExtra("keySet", keySet);
                            intent.putExtra("questionNo", list.size() + 1);
                            intent.putExtra("categoryImage", imageUrl);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizEducatorQuestionActivity.this, QuizEducatorSetActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("key", keyCtg);
                intent.putExtra("categoryImage", imageUrl);
                intent.putExtra("categoryImage", imageUrl);
                startActivity(intent);
                finish();
            }
        });

        binding.postQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.size() == 0) {
                    Toast.makeText(QuizEducatorQuestionActivity.this, "You haven't added any question to this set.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(QuizEducatorQuestionActivity.this, QuizEducatorPostActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("key", keyCtg);
                intent.putExtra("keySet", keySet);
                intent.putExtra("categoryImage", imageUrl);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemLongClick(int position) {
        QuestionModel selectedQuestion = list.get(position);
        database.getReference().child("Categories").child(keyCtg).child("postedSet").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    ArrayList<String> postedSet = (ArrayList<String>) snapshot.getValue();

                    if(postedSet != null && postedSet.contains(keySet)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(QuizEducatorQuestionActivity.this);
                        builder.setTitle("Unable to Delete Question");
                        builder.setMessage("You cannot delete questions in this set because it's already been posted to students. " +
                                "\n\nTo modify or delete questions, you need to cancel the posting of this set to students. " +
                                "\nYou can cancel the posting by long-clicking on the respective classroom.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(QuizEducatorQuestionActivity.this);
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
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(QuizEducatorQuestionActivity.this);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void deleteQuestionFromFirebase(QuestionModel selectedQuestion, int position) {
        referenceQuestions.child(selectedQuestion.getKeyQuestion()).removeValue()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    list.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(QuizEducatorQuestionActivity.this, "Question " + (position + 1) +" is deleted successfully", Toast.LENGTH_SHORT).show();
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
        database.getReference().child("Categories").child(keyCtg).child("postedSet").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    ArrayList<String> postedSet = (ArrayList<String>) snapshot.getValue();

                    if(postedSet != null && postedSet.contains(keySet)) {
                        QuestionModel model = list.get(position);
                        Intent intent = new Intent(QuizEducatorQuestionActivity.this, QuizEducatorAddQuestionActivity.class);
                        intent.putExtra("uid", uid);
                        intent.putExtra("key", keyCtg);
                        intent.putExtra("keySet", keySet);
                        intent.putExtra("keyQuestion", model.getKeyQuestion());
                        intent.putExtra("questionNo", position + 1);
                        intent.putExtra("categoryImage", imageUrl);
                        intent.putExtra("blockUpdate", true);

                        startActivity(intent);
                    } else {
                        proceedWithIntent(position);
                    }
                } else {
                    proceedWithIntent(position);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void proceedWithIntent(int position) {
        QuestionModel model = list.get(position);
        Intent intent = new Intent(QuizEducatorQuestionActivity.this, QuizEducatorAddQuestionActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("key", keyCtg);
        intent.putExtra("keySet", keySet);
        intent.putExtra("keyQuestion", model.getKeyQuestion());
        intent.putExtra("questionNo", position + 1);
        intent.putExtra("categoryImage", imageUrl);

        startActivity(intent);
    }

}
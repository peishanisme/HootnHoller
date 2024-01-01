package com.example.quiztesting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.quiztesting.Adapters.CategoryAdapter;
import com.example.quiztesting.Models.CategoryModel;
import com.example.quiztesting.Models.PostedQuizModel;
import com.example.quiztesting.Models.TaskStatus;
import com.example.quiztesting.Models.TaskToDo;
import com.example.quiztesting.databinding.ActivityQuizStudentCategoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class QuizStudentCategoryActivity extends AppCompatActivity implements RecyViewInterface {

    ActivityQuizStudentCategoryBinding binding;
    FirebaseDatabase database;
    DatabaseReference referenceClassKey, referenceCtg;
    FirebaseAuth auth;
    FirebaseUser user;
    String uid;
    CategoryAdapter adapter;
    ArrayList<CategoryModel> list;
    ArrayList<PostedQuizModel> postedQuizModels;
    HashMap<String, ArrayList<String>> quizToDo;
    HashMap<String, String> quizStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizStudentCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();

        /*auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            String uid = user.getUid();
        } else {
            Toast.makeText(this, "Error in identifying user", Toast.LENGTH_SHORT).show();
            finish();
        }*/

        uid = "Lw5nz7FCBda5vbGzfLr2esLLmk72";
        list = new ArrayList<>();
        postedQuizModels = new ArrayList<>();
        quizToDo = new HashMap<>();
        quizStatus = new HashMap<>();

        binding.bell.setVisibility(View.GONE);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.recyCategory.setLayoutManager(layoutManager);

        adapter = new CategoryAdapter(this, list, this);
        binding.recyCategory.setAdapter(adapter);

        referenceClassKey = database.getReference().child("Student").child(uid).child("quiz");
        referenceCtg = database.getReference().child("Categories");

        referenceClassKey.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    quizToDo.clear();
                    int cnt = 0;
                    long ttl = snapshot.getChildrenCount();
                    for (DataSnapshot classKeySnapshot : snapshot.getChildren()) {
                        cnt++;
                        int finalCnt = cnt;
                        referenceClassKey.child(classKeySnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot categoriesSnapshot) {
                                if(categoriesSnapshot.exists()) {
                                    for(DataSnapshot postedQuizSnapshot : categoriesSnapshot.getChildren()) {
                                        PostedQuizModel postedQuizModel = postedQuizSnapshot.getValue(PostedQuizModel.class);

                                        CategoryModel model = new CategoryModel();
                                        model.setCtgKey(postedQuizModel.getCtgKey());
                                        ArrayList<String> setKey = new ArrayList<>(postedQuizModel.getSetKeyInfo().keySet());
                                        model.setSetKey(setKey);

                                        referenceCtg.child(model.getCtgKey()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot categorySnapshot) {
                                                if(categorySnapshot.exists()) {
                                                    model.setCategoryName(categorySnapshot.child("categoryName").getValue(String.class));
                                                    model.setCategoryImage(categorySnapshot.child("categoryImage").getValue(String.class));
                                                    boolean checkRepetition = false;
                                                    for(CategoryModel categoryModel : list) {
                                                        if(categoryModel.getCtgKey().equals(model.getCtgKey())) {
                                                            checkRepetition = true;
                                                        }
                                                    }
                                                    if(!checkRepetition) {
                                                        list.add(model);
                                                        postedQuizModels.add(postedQuizModel);
                                                        identifyToDoTask(model.getCtgKey(), model.getSetKey());
                                                        adapter.notifyItemInserted(list.size());
                                                        if(finalCnt == ttl) {
                                                            binding.bell.setVisibility(View.VISIBLE);
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Toast.makeText(QuizStudentCategoryActivity.this, "Error in retrieving category details", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(QuizStudentCategoryActivity.this, "Error in retrieving category model", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuizStudentCategoryActivity.this, "Error in retrieving class key", Toast.LENGTH_SHORT).show();
            }
        });

        binding.bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskToDo toDo = new TaskToDo(quizToDo);
                TaskStatus status = new TaskStatus(quizStatus);

                Intent intent = new Intent(QuizStudentCategoryActivity.this, QuizStudentToDoActivity.class);
                intent.putExtra("taskToDo", toDo);
                intent.putExtra("taskStatus", status);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

    }

    private void identifyToDoTask(String keyCtg, ArrayList<String> setKeyList) {
        for (String keySet : setKeyList) {
            database.getReference()
                .child("Categories").child(keyCtg)
                .child("Sets").child(keySet)
                .child("Answers").child(uid)
                .child("status").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String status = snapshot.getValue(String.class);

                            if (status.equals("In progress")) {
                                quizStatus.put(keySet, status);
                                if(quizToDo.containsKey(keyCtg)) {
                                    ArrayList<String> quiz = quizToDo.get(keyCtg);
                                    if(!quiz.contains(keySet)) {
                                        quiz.add(keySet);
                                        quizToDo.put(keyCtg, quiz);
                                    }
                                } else {
                                    ArrayList<String> quiz = new ArrayList<>();
                                    quiz.add(keySet);
                                    quizToDo.put(keyCtg, quiz);
                                }

                                binding.bell.setImageResource(R.drawable.bellring);
                            }

                        } else {
                            quizStatus.put(keySet, "Incomplete");
                            if(quizToDo.containsKey(keyCtg)) {
                                ArrayList<String> quiz = quizToDo.get(keyCtg);
                                if(!quiz.contains(keySet)) {
                                    quiz.add(keySet);
                                    quizToDo.put(keyCtg, quiz);
                                }
                            } else {
                                ArrayList<String> quiz = new ArrayList<>();
                                quiz.add(keySet);
                                quizToDo.put(keyCtg, quiz);
                            }

                            binding.bell.setImageResource(R.drawable.bellring);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        }
    }

    @Override
    public void onItemLongClick(int position) {

    }

    @Override
    public void onItemClick(int position) {
        CategoryModel model = list.get(position);
        Intent intent = new Intent(QuizStudentCategoryActivity.this, QuizStudentSetActivity.class);
        intent.putExtra("keyCtg", model.getCtgKey());
        intent.putExtra("uid", uid);
        intent.putExtra("keySetList", model.getSetKey());

        this.startActivity(intent);
    }

}
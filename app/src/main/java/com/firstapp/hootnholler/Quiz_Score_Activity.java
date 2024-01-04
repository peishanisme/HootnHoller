package com.firstapp.hootnholler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firstapp.hootnholler.adapter.Student_Quiz;
import com.firstapp.hootnholler.databinding.ActivityQuizScoreBinding;
import com.firstapp.hootnholler.entity.Quiz;
import com.firstapp.hootnholler.entity.SetInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Quiz_Score_Activity extends AppCompatActivity {

    private ActivityQuizScoreBinding binding;
    private TextView DateRange, NumOfQuiz, NumOfCompletedQuiz, NumOfIncompletedQuiz;
    private Calendar calendar;
    private Date firstDayOfWeek, lastDayOfWeek;
    private ImageView MoveForwardWeekBtn, MoveBackwardWeekBtn;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String StudentUid;
    private FirebaseDatabase database;
    private DatabaseReference ClassKeyRef, CtgRef;
    private ArrayList<Quiz> quizRecyclerView;
    private int WeekFromToday, TotalQuiz = 0, TotalCompleted = 0, TotalIncompleted = 0;
    private Student_Quiz Student_Quiz_Adapter;
    private HashMap<String, ArrayList<String>> quizToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DateRange = findViewById(R.id.date_range);
        NumOfQuiz = findViewById(R.id.Number_Of_Quiz);
        NumOfCompletedQuiz = findViewById(R.id.Number_Completed_Quiz);
        NumOfIncompletedQuiz = findViewById(R.id.Number_Incomplete);
        MoveForwardWeekBtn = findViewById(R.id.ForwardWeekBtn);
        MoveBackwardWeekBtn = findViewById(R.id.BackwardWeekBtn);
        Bundle data = getIntent().getExtras();
        if (data != null) {

            StudentUid = data.getString("Student_UID");

        }

        quizRecyclerView = new ArrayList<>();
        quizToDo = new HashMap<>();
        WeekFromToday = 0;

        database = FirebaseDatabase.getInstance();
        ClassKeyRef = database.getReference().child("Student").child(StudentUid).child("quiz");
        CtgRef = database.getReference().child("Categories");

        RecyclerView recyclerView = findViewById(R.id.quizzes_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Student_Quiz_Adapter = new Student_Quiz(getApplicationContext(), quizRecyclerView);
        recyclerView.setAdapter(Student_Quiz_Adapter);

        formDate(0);

        MoveForwardWeekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeekFromToday--;
                formDate(WeekFromToday);
            }
        });

        MoveBackwardWeekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeekFromToday++;
                formDate(WeekFromToday);
                quizRecyclerView.clear();
                Student_Quiz_Adapter.notifyDataSetChanged();
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Parent_MainActivity.class);
                intent.putExtra("FRAGMENT_TO_LOAD", "home_Fragment"); // Pass the fragment tag or ID here
                startActivity(intent);

            }
        });
    }

    public void formDate(int selectedWeekFromToday) {
        calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_WEEK, -7 * selectedWeekFromToday);
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, -1);
        }
        firstDayOfWeek = calendar.getTime();
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        lastDayOfWeek = calendar.getTime();
        DateRange.setText(sdf.format(firstDayOfWeek) + "-" + sdf.format(lastDayOfWeek));
        notifyWeekChange();
    }

    public void notifyWeekChange() {
        checkWeek();
        loadQuizData();
    }

    public void checkWeek() {
        if (WeekFromToday == 0) {
            MoveForwardWeekBtn.setVisibility(View.GONE);
        } else {
            MoveForwardWeekBtn.setVisibility(View.VISIBLE);
        }
    }

    private void loadQuizData() {
        TotalCompleted = 0;
        TotalQuiz = 0;
        TotalIncompleted = 0;
        ClassKeyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    quizRecyclerView.clear();

                    long totalClassroom = snapshot.getChildrenCount();
                    int cntClassroom = 0;

                    for (DataSnapshot classKeySnapshot : snapshot.getChildren()) {
                        cntClassroom++;
                        int cntQuiz = 0;

                        int finalCntClassroom = cntClassroom;
                        ClassKeyRef.child(classKeySnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot categoriesSnapshot) {
                                if (categoriesSnapshot.exists()) {
                                    for (DataSnapshot postedQuizSnapshot : categoriesSnapshot.getChildren()) {
                                        String ctgKey = postedQuizSnapshot.child("ctgKey").getValue(String.class);
                                        HashMap<String, SetInfo> setInfo = (HashMap<String, SetInfo>) postedQuizSnapshot.child("setKeyInfo").getValue();
                                        ArrayList<String> setKeyList = new ArrayList<>(setInfo.keySet());
                                        int totalQuizinCurrentClass = setKeyList.size();
                                        final int[] cntQuiz = {0};
                                        if (setKeyList != null && setKeyList.size() > 0) {
                                            for (String setKey : setKeyList) {

                                                Long dueDate = postedQuizSnapshot.child("setKeyInfo").child(setKey).child("dueDate").getValue(Long.class);
                                                Long postedTime = postedQuizSnapshot.child("setKeyInfo").child(setKey).child("postedTime").getValue(Long.class);
                                                if(isBetween(new Date(postedTime), new Date(dueDate), firstDayOfWeek, lastDayOfWeek)) {
                                                    Quiz quizModel = new Quiz();
                                                    quizModel.setCategoryKey(ctgKey);
                                                    quizModel.setSetKey(setKey);
                                                    quizModel.setDueDate(dueDate);
                                                    quizModel.setPostedTime(postedTime);

                                                    CtgRef.child(quizModel.getCategoryKey()).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()) {
                                                                quizModel.setSubject(snapshot.child("categoryName").getValue(String.class));
                                                                quizModel.setCategoryImage(snapshot.child("categoryImage").getValue(String.class));

                                                                CtgRef.child(quizModel.getCategoryKey()).child("Sets").child(quizModel.getSetKey()).child("setName").addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        String setName = snapshot.getValue(String.class);
                                                                        quizModel.setSetName(setName);
                                                                        CtgRef.child(quizModel.getCategoryKey()).child("Sets").child(quizModel.getSetKey())
                                                                                .child("Answers").child(StudentUid).child("status")
                                                                                .addValueEventListener(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                        if (snapshot.exists()) {
                                                                                            String status = snapshot.getValue(String.class);
                                                                                            if (status.equals("Completed")) {
                                                                                                cntQuiz[0]++;
                                                                                                TotalCompleted++;
                                                                                                TotalQuiz++;
                                                                                                CtgRef.child(quizModel.getCategoryKey()).child("Sets").child(quizModel.getSetKey())
                                                                                                        .child("Answers").child(StudentUid).child("percentage").addValueEventListener(new ValueEventListener() {
                                                                                                            @Override
                                                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                                Integer score = snapshot.getValue(Integer.class);
                                                                                                                quizModel.setScore(score);
                                                                                                                quizRecyclerView.add(quizModel);
                                                                                                                Student_Quiz_Adapter.notifyItemInserted(quizRecyclerView.size());

                                                                                                            }

                                                                                                            @Override
                                                                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                                                                            }
                                                                                                        });
                                                                                            } else {
                                                                                                cntQuiz[0]++;
                                                                                                TotalCompleted++;
                                                                                                TotalQuiz++;
                                                                                            }

                                                                                        } else {
                                                                                            cntQuiz[0]++;
                                                                                            TotalCompleted++;
                                                                                            TotalQuiz++;
                                                                                        }

                                                                                        if (finalCntClassroom == totalClassroom && cntQuiz[0] == totalQuizinCurrentClass) {
                                                                                            updateUiElements();
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                                                        Toast.makeText(getApplicationContext(), "Error in retrieving quiz details", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                });
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });


                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                            Toast.makeText(getApplicationContext(), "Error in retrieving posted quiz model", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                } else {
                                                    cntQuiz[0]++;
                                                    updateUiElements();
                                                }


                                            }

                                        }

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getApplicationContext(), "Error in retrieving category model", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error in retrieving class key", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static boolean isBetween(Date postedTime, Date dueDate, Date startDate, Date endDate) {
        return postedTime.before(new Date()) && dueDate.after(startDate) && dueDate.before(endDate);
    }

    public int checkQuizStatus(Quiz quiz, boolean isSubmit) {
        Date dueDate = new Date(quiz.getDueDate());
        Date currentDate = new Date();

        // If due
        if (dueDate.before(currentDate)) {
            if (isSubmit) {
                this.TotalCompleted++;
                quizRecyclerView.add(quiz);
                Student_Quiz_Adapter.notifyItemInserted(quizRecyclerView.size());
                return 0;
            } else {
                this.TotalIncompleted++;
                return 2;
            }
        }
        // If not due
        else if (dueDate.after(currentDate)) {
            if (isSubmit) {
                this.TotalCompleted++;
                return 0;
            } else {
                return -1;
            }
        }

        return 0;
    }

    private void updateUiElements() {
        NumOfQuiz.setText(String.valueOf(TotalQuiz));
        NumOfCompletedQuiz.setText(String.valueOf(TotalCompleted));
        NumOfIncompletedQuiz.setText(String.valueOf(TotalIncompleted));
    }

}
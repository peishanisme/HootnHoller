package com.firstapp.hootnholler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firstapp.hootnholler.adapter.Student_Assignment;
import com.firstapp.hootnholler.adapter.Student_Quiz;
import com.firstapp.hootnholler.entity.Assignment;
import com.firstapp.hootnholler.entity.Quiz;
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
import java.util.Locale;

public class Quiz_Score_Activity extends AppCompatActivity {

//        private TextView DateRange;
//        private Calendar calendar;
//        private Date firstDayOfWeek, lastDayOfWeek;
//        private ImageView MoveForwardWeekBtn, MoveBackwardWeekBtn;
//        private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
//        private FirebaseAuth auth = FirebaseAuth.getInstance();
//        private String StudentUid;
//        private DatabaseReference StudentRef;
//        private ArrayList<Quiz> Quiz;
//        private ArrayList<String> CompletedList;
//        private Student_Quiz quizAdapter;
//        private int WeekFromToday, TotalQuiz= 0, TotalCompleted = 0, TotalIncompleted = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_score);
//        DateRange = findViewById(R.id.date_range);
//        MoveForwardWeekBtn = findViewById(R.id.ForwardWeekBtn);
//        MoveBackwardWeekBtn = findViewById(R.id.BackwardWeekBtn);
//        Bundle data = getIntent().getExtras();
//        StudentUid = data.get("Student_UID").toString();
//        StudentRef = FirebaseDatabase.getInstance().getReference("Student").child(StudentUid);
//
//        MoveForwardWeekBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                WeekFromToday--;
//                formDate(WeekFromToday);
//            }
//        });
//
//        MoveBackwardWeekBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                WeekFromToday++;
//                formDate(WeekFromToday);
//            }
//        });
//    }
//
//    public void formDate(int selectedWeekFromToday){
//        calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        calendar.add(Calendar.DAY_OF_WEEK, -7 * selectedWeekFromToday);
//        while(calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
//            calendar.add(Calendar.DAY_OF_WEEK, -1);
//        }
//        firstDayOfWeek = calendar.getTime();
//        calendar.add(Calendar.DAY_OF_WEEK, 6);
//        lastDayOfWeek = calendar.getTime();
//        DateRange.setText(sdf.format(firstDayOfWeek) + "-" + sdf.format(lastDayOfWeek));
//        notifyWeekChange();
//    }
//
//    public void notifyWeekChange(){
//        checkWeek();
//        loadQUizData();
//    }
//
//
//    public void checkWeek(){
//        if(WeekFromToday == 0){
//            MoveForwardWeekBtn.setVisibility(View.GONE);
//        }
//        else{
//            MoveForwardWeekBtn.setVisibility(View.VISIBLE);
//        }
//    }
//
//    private void loadQuizData() {
//        TotalCompleted = 0;
//        TotalQuiz = 0;
//        TotalIncompleted = 0;
//
//        CompletedList = new ArrayList<>();
//        // get the list of class code from student
//        this.StudentRef.child("quiz").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                final int[] numOfQuiz = {0};
//                Quiz.clear();
//                for (DataSnapshot dataSnapShot : snapshot.getChildren()) {
//                    String classCode = dataSnapShot.getKey();
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        String categoryKey = dataSnapshot.getKey();
//                        for (DataSnapshot dataSnapShot : snapshot.getChildren()) {
//                                String setKeyInfo = dataSnapshot.getKey();
//                                    setKeyInfo.child("setKeyInfo").addListenerForSingleValueEvent(new ValueEventListener() {
//
//
//                        }
//
//
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot classRoomSnapshot) {
//                            for (DataSnapshot assignmentSnapShot: classRoomSnapshot.getChildren()) {
//                                int TaskStatus = 0;
//                                Assignment assignment = assignmentSnapShot.getValue(Assignment.class);
//                                if(isBetween(new Date(assignment.getOpenDate())
//                                        ,new Date(assignment.getDueDate()),
//                                        firstDayOfWeek, lastDayOfWeek)){
//
//                                    // iterate submission inside the assignment
//                                    for (DataSnapshot submissionSnapShot : assignmentSnapShot.child("submission").getChildren()){
//                                        if(submissionSnapShot.getKey().equals(StudentUid)){
//                                            TaskStatus = checkQuizStatus(assignment, true);
//                                        }
//                                        else{
//                                            TaskStatus = checkQuizStatus(assignment, false);
//                                        }
//                                    }
//                                    assignment.setTaskStatus(TaskStatus);
//                                    assignment.setClassCode(classCode);
//                                    Assignment.add(assignment);
//                                    numOfTask[0]++;
//                                }
//                            }
//                            if(quizAdapter != null){
//                                quizAdapter.notifyDataSetChanged();
//                            }
//
//                            TotalQuiz = numOfTask[0];
//                            NumOfTask.setText(String.valueOf(Assignment.size()));
//                            NumOfCompletedTask.setText(String.valueOf(TotalCompleted));
//                            NumOfIncompletedTask.setText(String.valueOf(TotalIncompleted));
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//    }
//
//
//    public static boolean isBetween(Date openDate, Date dueDate, Date startDate, Date endDate){
//        if(openDate.before(new Date())){
//            if(dueDate.after(startDate) && dueDate.before(endDate)){
//                return true;
//            }
//            return false;
//        }
//        return false;
//    }
//
//    public int checkQuizStatus(Assignment assignment, boolean isSubmit){
//        Date dueDate = new Date(assignment.getDueDate());
//        Date currentDate = new Date();
//
//        // if due
//        if(dueDate.before(currentDate)){
//            if(isSubmit){
//                this.TotalCompleted++;
//                return 0;
//            }
//            else{
//                this.TotalIncompleted++;
//                return 2;
//            }
//        }
//        // if not due
//        else if(dueDate.after(currentDate)){
//            if(isSubmit){
//                this.TotalCompleted++;
//                return 0;
//            }
//            else{
//                this.TotalInProgress++;
//                return 1;
//            }
//        }
//
//        return -1;
    }

}
package com.firstapp.hootnholler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firstapp.hootnholler.adapter.Student_Quiz;
import com.firstapp.hootnholler.entity.Quiz;
import com.firstapp.hootnholler.entity.QuizInfoRetrieve;
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

    RecyclerView quizzes_list;
    private TextView DateRange, NumOfQuiz, NumOfCompletedQuiz, NumOfIncompletedQuiz;
    private Calendar calendar;
    private Date firstDayOfWeek, lastDayOfWeek;
    private ImageView MoveForwardWeekBtn, MoveBackwardWeekBtn;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String StudentUid, CategoryKey, SetKey;
    private FirebaseDatabase database;
    private DatabaseReference StudentRef, CategoryRef, ClassKeyRef, CtgRef;
    private ArrayList<Quiz> quizRecyclerView;
    private int scorePercentage;
    private ArrayList<Quiz> quiz = new ArrayList<>();
    private int WeekFromToday, TotalQuiz = 0, TotalCompleted = 0, TotalIncompleted = 0;

    private Student_Quiz Student_Quiz_Adapter;
    private HashMap<String, ArrayList<String>> quizToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_score);
        DateRange = findViewById(R.id.date_range);
        MoveForwardWeekBtn = findViewById(R.id.ForwardWeekBtn);
        MoveBackwardWeekBtn = findViewById(R.id.BackwardWeekBtn);
        Bundle data = getIntent().getExtras();
        if (data != null) {

            StudentUid = data.getString("Student_UID");
            if (StudentUid != null) {
                StudentRef = FirebaseDatabase.getInstance().getReference("Student").child(StudentUid);
            }

        }

        quiz = new ArrayList<>();
        quizRecyclerView = new ArrayList<>();
        quizToDo = new HashMap<>();
        WeekFromToday = 0;

        database = FirebaseDatabase.getInstance();
        ClassKeyRef = database.getReference().child("Student").child(StudentUid).child("quiz");
        CtgRef = database.getReference().child("Categories");

        RecyclerView recyclerView = findViewById(R.id.quizzes_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Student_Quiz_Adapter = new Student_Quiz(quizRecyclerView);
        recyclerView.setAdapter(Student_Quiz_Adapter);

        ClassKeyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    if (quiz != null) {
                        quiz.clear();
                    }

                    if (quizRecyclerView != null) {
                        quizRecyclerView.clear();
                    }

                    for (DataSnapshot classKeySnapshot : snapshot.getChildren()) {
                        ClassKeyRef.child(classKeySnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot categoriesSnapshot) {
                                if(categoriesSnapshot.exists()) {
                                    if (quiz == null) {
                                        quiz = new ArrayList<>();  // Initialize the list if it's null
                                    }
                                    quiz.clear();
                                    for(DataSnapshot postedQuizSnapshot : categoriesSnapshot.getChildren()) {
                                        QuizInfoRetrieve quizInfoRetrieve = postedQuizSnapshot.getValue(QuizInfoRetrieve.class);
                                        System.out.println("QuizRetrieved:" + quizInfoRetrieve);

                                        ArrayList<String> setKeyList = new ArrayList<>(quizInfoRetrieve.getSetKeyInfo().keySet());

                                        quizToDo.put(quizInfoRetrieve.getCtgKey(), setKeyList);
                                        for(String setKey : setKeyList) {
                                            Quiz model = new Quiz();
                                            model.setCategoryKey(quizInfoRetrieve.getCtgKey());
                                            model.setSetKey(setKey);
                                            SetInfo setInfo = quizInfoRetrieve.getSetKeyInfo().get(setKey);
                                            model.setPostedTime(setInfo.getPostedTime());
                                            model.setDueDate(setInfo.getDueDate());
                                            CtgRef.child(model.getCategoryKey()).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot categorySnapshot) {
                                                    if(categorySnapshot.exists()) {
                                                        model.setSubject(categorySnapshot.child("categoryName").getValue(String.class));
                                                        model.setCategoryImage(categorySnapshot.child("categoryImage").getValue(String.class));

                                                        String setName = categorySnapshot.child("Sets").child(model.getSetKey()).child("setName").getValue(String.class);
                                                        model.setSetName(setName);

                                                        String status = categorySnapshot.child("Sets").child(model.getSetKey()).child("Answers").child(StudentUid).child("status").getValue(String.class);
                                                        if(status != null && status.equals("Completed")) {
                                                            model.setQuizStatus(0);
                                                            Integer score = categorySnapshot.child("Sets").child(model.getSetKey()).child("Answers").child(StudentUid).child("percentage").getValue(Integer.class);
                                                            model.setScore(score);
                                                            boolean checkRepetition = false;
                                                            for(Quiz quizModel : quiz) {
                                                                if(quizModel.getCategoryKey().equals(model.getCategoryKey())) {
                                                                    checkRepetition = true;
                                                                }
                                                            }
                                                            if(!checkRepetition) {
                                                                quiz.add(model);
                                                            }
                                                        } else {
                                                            model.setQuizStatus(2);
                                                            boolean checkRepetition = false;
                                                            for(Quiz quizModel : quiz) {
                                                                if(quizModel.getCategoryKey().equals(model.getCategoryKey())) {
                                                                    checkRepetition = true;
                                                                }
                                                            }
                                                            if(!checkRepetition) {
                                                                quiz.add(model);
                                                            }
                                                        }

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Toast.makeText(Quiz_Score_Activity.this, "Error in retrieving category details", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(Quiz_Score_Activity.this, "Error in retrieving category model", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Quiz_Score_Activity.this, "Error in retrieving class key", Toast.LENGTH_SHORT).show();
            }
        });

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
        quizRecyclerView.clear();

        if(quiz.size() != 0) {
            for (Quiz currentQuiz : quiz) {
                int quizStatus;

                if(currentQuiz.getQuizStatus() == 0) {
                    quizStatus = checkQuizStatus(currentQuiz, true);
                } else {
                    quizStatus = checkQuizStatus(currentQuiz, false);
                }

                if (isBetween(new Date(currentQuiz.getPostedTime()),
                        new Date(currentQuiz.getDueDate()),
                        firstDayOfWeek, lastDayOfWeek)) {

                    // Update quiz status
                    currentQuiz.setQuizStatus(quizStatus);
                    quizRecyclerView.add(currentQuiz);
                    TotalQuiz++;

                    if (quizStatus == 0) {
                        TotalCompleted++;
                    } else {
                        TotalIncompleted++;
                    }
                }
            }

            // Notify the adapter
            if (Student_Quiz_Adapter != null) {
                Student_Quiz_Adapter.notifyDataSetChanged();
            }

            // Update UI elements
            NumOfQuiz.setText(String.valueOf(TotalQuiz));
            NumOfCompletedQuiz.setText(String.valueOf(TotalCompleted));
            NumOfIncompletedQuiz.setText(String.valueOf(TotalIncompleted));
        }

    }
//    private void loadQuizData() {
//        TotalCompleted = 0;
//        TotalQuiz = 0;
//        TotalIncompleted = 0;
//
//        CompletedList = new ArrayList<>();
//
//        // Load quiz score and set details
//        DatabaseReference quizScoreRef = FirebaseDatabase.getInstance().getReference("Category").child(CategoryKey)
//                                                                                        .child("Sets").child(SetKey)
//                                                                                         .child("Answer").child(StudentUid);
//        quizScoreRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if (snapshot.exists()) {
//                    //retrieve status
//                    String status = snapshot.getValue(String.class);
//
//                    if(status.equals("In progress")){
//
//                    }
//
//                    // Retrieve score,status and setName
//                    for (DataSnapshot quizSnapshot : snapshot.getChildren()) {
//                        Quiz score = quizSnapshot.child("Answers").child(StudentUid).getValue((Quiz.class));
//                        Quiz set = quizSnapshot.child("postedClassroom").getValue(Quiz.class);
//
//                        // Retrieve category Name
//                        retrieveCtgName();
//
//                        // Retrieve dueDate and posted Time
//                        DatabaseReference referenceStudentQuiz = StudentRef.child(StudentUid).child("quiz");
//
//                        referenceStudentQuiz.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                final int[] numOfQuiz = {0};
//                                Quiz.clear();
//                                if (snapshot.exists()) {
//                                    for (DataSnapshot classSnapshot : snapshot.getChildren()) {
//                                        String classCode = classSnapshot.getKey();
//                                        referenceStudentQuiz.child(classCode).child(CategoryKey).child("setKeyInfo").addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot studentSnapshot) {
//                                                for (DataSnapshot setKeyInfoSnapShot : studentSnapshot.getChildren()) {
//                                                    int QuizStatus = 0;
//                                                    Quiz quiz = setKeyInfoSnapShot.getValue(Quiz.class);
//                                                    if (isBetween(new Date(quiz.getPostedTime()),
//                                                            new Date(quiz.getDueDate()),
//                                                            firstDayOfWeek, lastDayOfWeek)) {
//                                                        // Haven't added logic for quizstatus
//
//                                                        quiz.setQuizStatus(QuizStatus);
//                                                        Quiz.add(quiz);
//                                                        numOfQuiz[0]++;
//                                                        if (QuizStatus == 0) {
//                                                            TotalCompleted++;
//                                                        } else {
//                                                            TotalIncompleted++;
//                                                        }
//                                                    }
//                                                }
//                                                if (Student_Quiz_Adapter != null) {
//                                                    Student_Quiz_Adapter.notifyDataSetChanged();
//                                                }
//                                                NumOfQuiz.setText(String.valueOf(Quiz.size()));
//                                                NumOfCompletedQuiz.setText(String.valueOf(TotalCompleted));
//                                                NumOfIncompletedQuiz.setText(String.valueOf(TotalIncompleted));
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError error) {
//                                                // Handle onCancelled for setKeyInfo
//                                            }
//                                        });
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                                // Handle onCancelled for referenceStudentQuiz
//                            }
//                        });
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle onCancelled for quizScoreRef
//            }
//        });
//    }

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

    private void retrieveCtgName() {
        CategoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    if (snapshot.exists()) {
                        String categoryName = categorySnapshot.child("categoryName").getValue(String.class);

                        // Update the subject for each quiz
                        for (Quiz quiz : quiz) {
                            quiz.setSubject(categoryName);
                        }
                    }

                    // Notify the adapter after updating subjects
                    if (Student_Quiz_Adapter != null) {
                        Student_Quiz_Adapter.notifyDataSetChanged();
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled for CategoryRef
            }
        });
    }

//    public String generateCategoryName(){
//        int leftLimit = 97;
//        int rightLimit = 122;
//        int targetStringLength = 7;
//        Random random = new Random();
//
//        String generatedName = random.ints(leftLimit,rightLimit + 1)
//                .limit(targetStringLength)
//                .collect(StringBuilder::new, StringBuilder::appendCodePoint,StringBuilder::append)
//                .toString();
//        return generatedName;
//
//
//
//    }
//
//    public String generateSetName(){
//        int leftLimit = 97;
//        int rightLimit = 122;
//        int targetStringLength = 7;
//        Random random = new Random();
//
//        String generateSetName = random.ints(leftLimit,rightLimit + 1)
//                .limit(targetStringLength)
//                .collect(StringBuilder::new, StringBuilder::appendCodePoint,StringBuilder::append)
//                .toString();
//        return generateSetName;
//    }
//
//    public static long generateRandomDueDate() {
//        Instant now = Instant.now();
//        long randomDays = new Random().nextInt(30); // Adjust the range as needed
//        Instant dueDate = now.plusSeconds(randomDays * 24 * 60 * 60);
//
//        return dueDate.toEpochMilli();
//    }
//
//    public static long generateRandomPostedDate() {
//        Instant now = Instant.now();
//        long randomDays = new Random().nextInt(30); // Adjust the range as needed
//        Instant dueDate = now.plusSeconds(randomDays * 24 * 60 * 60);
//
//        return dueDate.toEpochMilli();
//    }
//
//
//
//    public String generateScore(){
//        double generatedScore = (double) Math.floor(Math.random() * 20) + 1;
//
//        return generatedScore + "%";
//
//
//
//    }
//
//    public void generateEntities(){
//        for(int i=0 ; i<10 ; i++){
//           Quiz.add(new Quiz(generateCategoryName(),generateSetName(),generateRandomDueDate(),generateRandomPostedDate(),generateScore()));
//        }
//    }
//
//    public void updateUI (ArrayList<Quiz> quizzes){
//        Log.d("inside", "updateUI");
//        Student_Quiz studentQuiz = new Student_Quiz(quizzes);
//        quizzes_list.setHasFixedSize(true);
//        quizzes_list.setLayoutManager(new LinearLayoutManager(this));
//        quizzes_list.setAdapter(studentQuiz);
//    }
//
//    class waitTask extends AsyncTask<Void,Void,Void> {
//
//        ProgressDialog pd;
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd= new ProgressDialog((Quiz_Score_Activity.this));
//            pd.show();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            generateEntities();
//            try{
//                Thread.sleep(5000);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void unused) {
//            super.onPostExecute(unused);
//
//            //==after generate 10 entities update UI here
//            updateUI(quiz);
//            pd.dismiss();
//        }
//
//    }
}

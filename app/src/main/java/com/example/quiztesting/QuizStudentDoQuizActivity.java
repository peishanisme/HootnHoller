package com.example.quiztesting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.quiztesting.Models.AnswerModel;
import com.example.quiztesting.Models.QuestionModel;
import com.example.quiztesting.databinding.ActivityQuizStudentDoQuizBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuizStudentDoQuizActivity extends AppCompatActivity {

    ActivityQuizStudentDoQuizBinding binding;
    FirebaseDatabase database;
    DatabaseReference referenceSet, referenceStatus;
    String keyCtg, keySet, uid, setName;
    ArrayList<String> keySetList;
    int queIndex;
    ArrayList<QuestionModel> list;
    QuestionModel model;
    Button[] options;
    Button lastSelectedOption;

    AnswerModel answerModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizStudentDoQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = new ArrayList<>();

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        keyCtg = intent.getStringExtra("keyCtg");
        keySet = intent.getStringExtra("keySet");
        queIndex = intent.getIntExtra("queIndex", 0);
        list = intent.getParcelableArrayListExtra("questions");
        keySetList = intent.getStringArrayListExtra("keySetList");
        setName = intent.getStringExtra("setName");

        database = FirebaseDatabase.getInstance();
        referenceSet = database.getReference().child("Categories").child(keyCtg).child("Sets").child(keySet);
        referenceStatus = referenceSet.child("Answers").child(uid).child("status");

        model = list.get(queIndex);
        options = new Button[]{binding.answerA, binding.answerB, binding.answerC, binding.answerD};
        binding.questionTV.setText(model.getQuestion());
        options[0].setText(model.getOptionA());
        options[1].setText(model.getOptionB());
        options[2].setText(model.getOptionC());
        options[3].setText(model.getOptionD());

        binding.questionNo.setText((queIndex + 1) + "/" + list.size());

        if(list.size() == (queIndex + 1)) {
            binding.submit.setText("Submit");
        } else {
            binding.submit.setText("Next");
        }

        if((queIndex - 1) < 0) {
            binding.previous.setVisibility(View.GONE);
        }

        if((queIndex + 1) >= list.size()) {
            binding.next.setVisibility(View.GONE);
        }

        int index = checkCompletion();
        if(index != -1) {
            passToNextQue(index);
        }

        referenceSet.child("Answers").child(uid).child(model.getKeyQuestion()).child("optionSelected").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String optionSelected = snapshot.getValue(String.class);
                    for (Button option : options) {
                        if(optionSelected.equals(option.getText().toString())) {
                            lastSelectedOption = option;
                            System.out.println(lastSelectedOption.getText().toString());
                            updateButtonColors(lastSelectedOption);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuizStudentDoQuizActivity.this, "Fail to check database to restore answer", Toast.LENGTH_SHORT).show();
            }
        });

        /*final boolean[] completed = {true};
        final int[] i = {0};
        referenceSet.child("Answers").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean check = false;
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        for(QuestionModel question : list) {
                            if(question.getKeyQuestion().equals(dataSnapshot.getKey())) {
                                check = true;
                            }
                        }
                        if(!check) {
                            completed[0] = false;
                            break;
                        }
                        i[0]++;
                    }
                    if(completed[0]) {
                        Intent intent = new Intent(QuizStudentDoQuizActivity.this, QuizStudentReviewActivity.class);
                        intent.putExtra("uid", uid);
                        intent.putExtra("keyCtg", keyCtg);
                        intent.putExtra("keySetList", keySetList);
                        startActivity(intent);
                    } else {
                        referenceSet.child("Answers").child(uid).child(model.getKeyQuestion()).child("optionSelected").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()) {
                                    String optionSelected = snapshot.getValue(String.class);
                                    for (Button option : options) {
                                        if(optionSelected.equals(option.getText().toString())) {
                                            lastSelectedOption = option;
                                            System.out.println(lastSelectedOption.getText().toString());
                                            updateButtonColors(lastSelectedOption);
                                            break;
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(QuizStudentDoQuizActivity.this, "Fail to check database to restore answer", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuizStudentDoQuizActivity.this, "Fail to check completionStatus", Toast.LENGTH_SHORT).show();
                return;
            }
        });*/

        binding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastSelectedOption!=null) {
                    uploadAnswer();
                }
                passToNextQue(queIndex - 1);
            }
        });

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastSelectedOption!=null) {
                    uploadAnswer();
                }
                passToNextQue(queIndex + 1);
            }
        });

        options[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButtonColors(null);
                updateButtonColors(options[0]);
                lastSelectedOption = options[0];
            }
        });

        options[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButtonColors(options[1]);
                lastSelectedOption = options[1];
            }
        });

        options[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButtonColors(options[2]);
                lastSelectedOption = options[2];
            }
        });

        options[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButtonColors(options[3]);
                lastSelectedOption = options[3];
            }
        });

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastSelectedOption == null) {
                    Toast.makeText(QuizStudentDoQuizActivity.this, "Please select your answer.", Toast.LENGTH_SHORT).show();
                    return;
                }
                uploadAnswer();

                if(queIndex + 1 == list.size()) {
                    int index = checkCompletion();
                    if(index == -1) {
                        setCompletionStatus("Completed", index);
                        AlertDialog.Builder builder = new AlertDialog.Builder(QuizStudentDoQuizActivity.this);
                        builder.setTitle("Confirm Submission");
                        builder.setMessage("Are you sure you want to submit the current quiz?" + "\nOnce submitted, you won't be able to make any changes to your answers.");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setCompletionStatus("Completed", index);
                                Intent intent = new Intent(QuizStudentDoQuizActivity.this, QuizStudentLeaderboardActivity.class);
                                intent.putExtra("uid", uid);
                                intent.putExtra("keyCtg", keyCtg);
                                intent.putExtra("keySet", keySet);
                                intent.putExtra("keySetList", keySetList);
                                intent.putExtra("setName", setName);
                                intent.putParcelableArrayListExtra("questions", list);
                                startActivity(intent);
                            }
                        }).setNegativeButton("No", null);
                        builder.show();
                    } else {
                        Toast.makeText(QuizStudentDoQuizActivity.this, "Haven't completed your test", Toast.LENGTH_SHORT).show();
                        passToNextQue(index);
                    }
                } else {
                    passToNextQue(queIndex + 1);
                }
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastSelectedOption != null) {
                    AnswerModel newModel = new AnswerModel(model.getKeyQuestion(), lastSelectedOption.getText().toString(),
                            (lastSelectedOption.getText().toString().equals(model.getCorrectAns())));

                    referenceSet.child("Answers").child(uid).child(model.getKeyQuestion()).setValue(answerModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            int i = checkCompletion();
                            if(checkCompletion() == -1) {
                                askForSubmission();
                            } else {
                                setCompletionStatus("In progress", i);
                            }
                        }
                    });
                }

                Intent intent = new Intent(QuizStudentDoQuizActivity.this, QuizStudentSetActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("keyCtg", keyCtg);
                intent.putExtra("keySetList", keySetList);
                startActivity(intent);
            }
        });

    }

    public void askForSubmission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuizStudentDoQuizActivity.this);
        builder.setTitle("Confirm Submission");
        builder.setMessage("Do you want to submit the current quiz? Once submitted, you won't be able to make any changes to your answers.");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setCompletionStatus("Completed", 0);
            }
        }).setNegativeButton("No", null);
        builder.show();
    }

    private void uploadAnswer() {
        String option = lastSelectedOption.getText().toString();
        boolean correctness = false;
        if(option.equals(model.getCorrectAns())) {
            correctness = true;
        }
        answerModel = new AnswerModel(model.getKeyQuestion(), option, correctness);
        uploadAnswerToFirebase(answerModel);
    }

    private void uploadAnswerToFirebase(AnswerModel answerModel) {
        referenceSet.child("Answers").child(uid).child(model.getKeyQuestion()).setValue(answerModel).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuizStudentDoQuizActivity.this, "Fail to save answer", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void passToNextQue(int index) {
        Intent intent = new Intent(QuizStudentDoQuizActivity.this, QuizStudentDoQuizActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("setName", setName);
        intent.putExtra("keyCtg", keyCtg);
        intent.putExtra("keySet", keySet);
        intent.putExtra("queIndex", index);
        intent.putParcelableArrayListExtra("questions", list);
        intent.putStringArrayListExtra("keySetList", keySetList);
        startActivity(intent);
    }

    private void setCompletionStatus(String status, int index) {
        referenceStatus.setValue(status).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if(status.equals("In progress")) {
                    referenceStatus.child("currentProgress").setValue(index).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(QuizStudentDoQuizActivity.this, "Fail to upload current progress", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    referenceStatus.child("currentProgress").setValue(null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuizStudentDoQuizActivity.this, "Fail to upload current completion status", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public int checkCompletion() {
        final boolean[] completed = {true};
        final int[] index = {0};
        referenceSet.child("Answers").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean check = false;
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        for(QuestionModel question : list) {
                            if(question.getKeyQuestion().equals(dataSnapshot.getKey())) {
                                check = true;
                            }
                        }
                        if(!check) {
                            completed[0] = false;
                            break;
                        }
                        index[0]++;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuizStudentDoQuizActivity.this, "Fail to check completionStatus", Toast.LENGTH_SHORT).show();
                return;
            }
        });
        if(completed[0]) {
            return -1;
        } else {
            return index[0];
        }
    }

    public void updateButtonColors(Button selectedOption) {
        for (Button option : options) {
            if (option == selectedOption) {
                option.setBackgroundResource(R.drawable.selectedbtn);
            } else {
                option.setBackgroundResource(R.drawable.btn);
            }
        }
    }
}
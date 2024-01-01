package com.example.quiztesting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.quiztesting.Models.AnswerModel;
import com.example.quiztesting.Models.QuestionModel;
import com.example.quiztesting.Models.TaskStatus;
import com.example.quiztesting.Models.TaskToDo;
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
    DatabaseReference referenceSet, referenceStatus, referenceProgress;
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
        referenceProgress = referenceSet.child("Answers").child(uid).child("progress");

        if(keySetList == null) {
            keySetList = new ArrayList<>();
        }

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

        referenceSet.child("Answers").child(uid).child(model.getKeyQuestion()).child("optionSelected").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String optionSelected = snapshot.getValue(String.class);
                    for (Button option : options) {
                        if(optionSelected.equals(option.getText().toString())) {
                            lastSelectedOption = option;
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

        binding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastSelectedOption != null) {
                    uploadAnswer();
                }
                passToNextQue(queIndex - 1);
            }
        });

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastSelectedOption != null) {
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

                if (queIndex + 1 == list.size()) {
                    checkCompletion(new CompletionCheckListener() {
                        @Override
                        public void onComplete(int index) {
                            if (index == -1) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(QuizStudentDoQuizActivity.this);
                                builder.setTitle("Confirm Submission");
                                builder.setMessage("Are you sure you want to submit the current quiz?" +
                                        "\n\nOnce submitted, you won't be able to make any changes to your answers.");
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
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("No", null);
                                builder.show();
                            } else {
                                Toast.makeText(QuizStudentDoQuizActivity.this, "Haven't completed your test", Toast.LENGTH_SHORT).show();
                                passToNextQue(index);
                            }
                        }
                    });
                } else {
                    passToNextQue(queIndex + 1);
                }
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastSelectedOption != null) {
                    AnswerModel newModel = new AnswerModel(model.getKeyQuestion(), lastSelectedOption.getText().toString(),
                            (lastSelectedOption.getText().toString().equals(model.getCorrectAns())));

                    referenceSet.child("Answers").child(uid).child(model.getKeyQuestion()).setValue(newModel);
                }
                checkCompletion(new CompletionCheckListener() {
                    @Override
                    public void onComplete(int index) {
                        if(index == -1) {
                            setCompletionStatus("In progress", list.size() - 1);
                        } else {
                            setCompletionStatus("In progress", index);
                            navigateToQuizSetActivity();
                        }
                    }
                });
            }
        });
    }

    public void navigateToQuizSetActivity() {
        if(keySetList.isEmpty()) {
            Intent intent = new Intent(QuizStudentDoQuizActivity.this, QuizStudentCategoryActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(QuizStudentDoQuizActivity.this, QuizStudentSetActivity.class);
            intent.putExtra("uid", uid);
            intent.putExtra("keyCtg", keyCtg);
            intent.putExtra("keySetList", keySetList);
            startActivity(intent);
        }

    }

    public void askForSubmission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuizStudentDoQuizActivity.this);
        builder.setTitle("Confirm Submission");
        builder.setMessage("Do you want to submit the current quiz? Once submitted, you won't be able to make any changes to your answers.");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setCompletionStatus("Completed", 0);
                dialog.dismiss();
            }
        }).setNegativeButton("No", null);
        builder.show();
        navigateToQuizSetActivity();
    }

    private void uploadAnswer() {
        String option = lastSelectedOption.getText().toString();
        boolean correctness = option.equals(model.getCorrectAns());
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
                    referenceProgress.setValue(index);
                } else {
                    referenceProgress.setValue(null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuizStudentDoQuizActivity.this, "Fail to upload current completion status", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void checkCompletion(CompletionCheckListener listener) {
        final boolean[] foundIncomplete = {false};
        final int[] firstIncompleteIndex = {-1};

        for (int i = 0; i < list.size(); i++) {
            final int currentIndex = i;
            QuestionModel questionModel = list.get(i);

            referenceSet.child("Answers").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!foundIncomplete[0] && snapshot.exists()) {
                        boolean foundAnswer = false;

                        for (DataSnapshot answerDataSnapshot : snapshot.getChildren()) {
                            if (answerDataSnapshot.getKey().equals(questionModel.getKeyQuestion())) {
                                foundAnswer = true;
                                break;
                            }
                        }

                        if (!foundAnswer) {
                            foundIncomplete[0] = true;
                            firstIncompleteIndex[0] = currentIndex;
                            listener.onComplete(firstIncompleteIndex[0]);
                        }

                        if (foundIncomplete[0] && firstIncompleteIndex[0] != -1) {
                            return;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            if (foundIncomplete[0] && firstIncompleteIndex[0] != -1) {
                break;
            }
        }

        if (!foundIncomplete[0]) {
            listener.onComplete(-1);
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

    public interface CompletionCheckListener {
        void onComplete(int index); // Define the method signature for completion
    }

}
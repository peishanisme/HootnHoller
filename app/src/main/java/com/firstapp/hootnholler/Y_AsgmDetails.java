package com.firstapp.hootnholler;

import static com.firstapp.hootnholler.adapter.Student_Ass_Adapter.convertTimestampToDateTime;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.firstapp.hootnholler.databinding.ActivityStudentAsgnDetailsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Locale;

public class Y_AsgmDetails extends AppCompatActivity {

    private Button addSubmissionButton, uploadButton, cancelButton, dltButton, gradeButton;
    private ImageView backButton;
    private TextView title, openTime, dueTime, description, fileName, submissionStatus, timeRemaining, fileSubmission, gradingStatus, submissionTime, submissionComment;
    private EditText uploadFileName, assComment;
    ImageView addFile;
    private LinearLayout submitLayout, studentLayout, teacherGradeLayout, submissionTable;
    String assId, currentClassCode;
    String displayName = null;
    DatabaseReference assRef, databaseReference;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String uid = auth.getCurrentUser().getUid();
    String studentUID;
    CardView file;
    StorageReference storageReference;
    private Uri selectedFileUri;
    Dialog mdialog;
    private EditText grade, fileInfor;
    private ImageView close;
    private boolean isEducator;

    ActivityStudentAsgnDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentAsgnDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        title = findViewById(R.id.assTitle);
        openTime = findViewById(R.id.openTime);
        dueTime = findViewById(R.id.dueTime);
        description = findViewById(R.id.description);
        fileName = findViewById(R.id.fileName);
        file = findViewById(R.id.file);

        submissionStatus = findViewById(R.id.submissionStatus);
        gradingStatus = findViewById(R.id.gradingStatus);
        timeRemaining = findViewById(R.id.timeLeft);
        fileSubmission = findViewById(R.id.fileSubmission);
        submissionTime = findViewById(R.id.timeSubmission);
        submissionComment = findViewById(R.id.submissionComment);
        addSubmissionButton = findViewById(R.id.btnAddSubmission);
        addSubmissionButton.setVisibility(View.GONE);

        submitLayout = findViewById(R.id.submitLayout);
        uploadFileName = findViewById(R.id.submissionTitle);
        uploadButton=findViewById(R.id.uploadFile);
        addFile = findViewById(R.id.addFile);
        cancelButton = findViewById(R.id.cancelUpload);
        assComment = findViewById(R.id.Comment);
        teacherGradeLayout = findViewById(R.id.teacher_grade_layout);
        studentLayout = findViewById(R.id.student_layout);
        gradeButton = findViewById(R.id.btnGrade);
        dltButton = findViewById(R.id.btnDltAssgn);
        submissionTable = findViewById(R.id.submissionTable);

        submitLayout.setVisibility(View.GONE);


        assId = getIntent().getStringExtra("assID");
        currentClassCode = getIntent().getStringExtra("classCode");
        studentUID = getIntent().getStringExtra("studentUID");
        storageReference = FirebaseStorage.getInstance().getReference();
        checkRole();

        backButton = binding.back;



        gradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGradeDialog();
            }
        });

        dltButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleBackButtonClick();
            }
        });
    
    }

    private void handleBackButtonClick() {
        if (isEducator) {
            // User is an educator, navigate to Educator_Assignment activity
            Intent intent = new Intent(Y_AsgmDetails.this, Educator_Assignment.class);
            intent.putExtra("classCode", currentClassCode);
            startActivity(intent);
        } else {
            // User is a student, navigate to Student_Assignment activity
            Intent intent = new Intent(Y_AsgmDetails.this, Student_Assignment.class);
            intent.putExtra("classCode", currentClassCode);
            startActivity(intent);
        }
        finish(); // Finish the current activity to prevent going back to it on back press
    }

    public void showGradeDialog(){
        mdialog = new Dialog(this);
        mdialog.setContentView(R.layout.pop_out_ass_grading);
        ImageView addFile = mdialog.findViewById(R.id.addFile);
        grade = mdialog.findViewById(R.id.score);
        fileInfor = mdialog.findViewById(R.id.fileName);
        close=mdialog.findViewById(R.id.close);
        addFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select PDF Files"), 102);
            }
        });
        Button submitBtn = mdialog.findViewById(R.id.grade);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadGrade();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog.dismiss();
            }
        });
        // Show the dialog
        mdialog.show();
    }

    private void checkAssignmentStatus(DataSnapshot assignmentSnapshot){
        title.setText(assignmentSnapshot.child("title").getValue(String.class));
        String openTimestamp = assignmentSnapshot.child("uploadDate").getValue(String.class);
        openTime.setText("Opened: " + convertTimestampToDateTime(Long.parseLong(openTimestamp)));
        String dueTimestamp = assignmentSnapshot.child("dueDate").getValue(String.class);
        dueTime.setText("Due: " + convertTimestampToDateTime(Long.parseLong(dueTimestamp)));
        description.setText(assignmentSnapshot.child("description").getValue(String.class));
        fileName.setText(assignmentSnapshot.child("fileName").getValue(String.class));
        String fileUri = assignmentSnapshot.child("fileUri").getValue(String.class);

        long currentTimeMillis = System.currentTimeMillis();
        long dueTimeMillis = Long.parseLong(dueTimestamp);

        if (dueTimeMillis > currentTimeMillis) {
            long timeRemainingMillis = dueTimeMillis - currentTimeMillis;
            String timeRemainingString = formatTimeRemaining(timeRemainingMillis);
            timeRemaining.setText(timeRemainingString);
        } else {
            timeRemaining.setText("Time's up!");
        }
        fileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile(fileUri);
            }
        });
    }

    private void checkSubmissionStatus(DataSnapshot submissionSnapshot) {
        if (submissionSnapshot.exists()) {
            addSubmissionButton.setVisibility(View.GONE);

            // User has submitted the assignment
            submissionStatus.setText("Submitted");

            // You can also retrieve additional information if needed
            String submissionTimeStr = submissionSnapshot.child("submissionTime").getValue(String.class);
            String fileUri = submissionSnapshot.child("fileUri").getValue(String.class);
            String fileNameStr = submissionSnapshot.child("fileName").getValue(String.class);

            submissionTime.setText(convertTimestampToDateTime(Long.parseLong(submissionTimeStr)));
            fileSubmission.setText(fileNameStr);

            DataSnapshot commentSnapshot = submissionSnapshot.child("comment");
            if (commentSnapshot.exists()) {
                String commentText = commentSnapshot.getValue(String.class);
                submissionComment.setText(commentText);
            } else {
                submissionComment.setText("-");
            }

            DataSnapshot scoreSnapshot = submissionSnapshot.child("score");
            if (scoreSnapshot.exists()) {
                // Grading status is set to the value of the score node
                String score = scoreSnapshot.getValue(String.class);
                gradingStatus.setText("Graded: " + score);
            } else {
                // Score node doesn't exist, grading status is set to "Not graded"
                gradingStatus.setText("Not graded");
            }

            fileSubmission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openFile(fileUri);
                }
            });

        } else {
            // User has not attempted the assignment
            addSubmissionButton.setVisibility(View.VISIBLE);
            addSubmissionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitLayout.setVisibility(View.VISIBLE);
                    addSubmissionButton.setVisibility(View.GONE);
                    uploadAss();
                }
            });
            gradingStatus.setText("Not graded");
            submissionStatus.setText("No attempt");
            submissionTime.setText("--");
            fileSubmission.setText("--");
            submissionComment.setText("--");
        }
    }

    private void uploadAss() {
        uploadButton.setEnabled(false);
        addFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPDF();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitLayout.setVisibility(View.GONE);
                addSubmissionButton.setVisibility(View.VISIBLE);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedFileUri != null) {
                    uploadPDF(selectedFileUri);
                } else {
                    Toast.makeText(Y_AsgmDetails.this, "Please select a file", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openFile(String fileUri) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(fileUri));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String formatTimeRemaining(long timeRemainingMillis) {
        long days = timeRemainingMillis / (24 * 60 * 60 * 1000);
        long hours = (timeRemainingMillis % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
        long minutes = (timeRemainingMillis % (60 * 60 * 1000)) / (60 * 1000);

        return String.format(Locale.getDefault(), "%d days %02d hours %02d minutes", days, hours, minutes);
    }

    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF Files"), 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedFileUri = data.getData();
            String uriString = selectedFileUri.toString();
            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();


            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getContentResolver().query(selectedFileUri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        if (displayNameIndex != -1) {
                            displayName = cursor.getString(displayNameIndex);
                        }
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
            }

            uploadButton.setEnabled(true);
            uploadFileName.setText(displayName);
        }
        else if(requestCode == 102 && resultCode == RESULT_OK && data != null && data.getData() != null){
            selectedFileUri = data.getData();
            String uriString = selectedFileUri.toString();
            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();


            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getContentResolver().query(selectedFileUri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        if (displayNameIndex != -1) {
                            displayName = cursor.getString(displayNameIndex);
                        }
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
            }

            fileInfor.setText(displayName);
        }
    }

    private void uploadPDF(Uri data) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("File Uploading...");
        pd.show();

        final String timestamp = String.valueOf(System.currentTimeMillis());
        final StorageReference reference = storageReference.child("submission/" + timestamp + ".pdf");

        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        Uri uri = uriTask.getResult();

                        DatabaseReference submissionRef = assRef.child("Submission").child(uid);
                        submissionRef.child("fileName").setValue(displayName);
                        submissionRef.child("submissionTime").setValue(String.valueOf(System.currentTimeMillis()));
                        submissionRef.child("fileUri").setValue(uri.toString());
                        submissionRef.child("comment").setValue(assComment.getText().toString());


                        Toast.makeText(Y_AsgmDetails.this, "File Uploaded Successfully!!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        finish();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        pd.setMessage("Uploaded : " + (int) percent + "%");
                    }
                });
    }

    private void uploadGrade(){
        if(grade.getText().toString().isEmpty()){
            Toast.makeText(Y_AsgmDetails.this, "Please enter mark.", Toast.LENGTH_SHORT).show();
        }
        else{
            if (selectedFileUri != null) {
                uploadGradePdf(selectedFileUri);
            }
            assRef = FirebaseDatabase.getInstance().getReference("Classroom").child(currentClassCode).child("Assignment").child(assId);
            DatabaseReference submissionRef = assRef.child("Submission").child(studentUID);
            submissionRef.child("score").setValue(grade.getText().toString());
        }
    }

    private void uploadGradePdf(Uri data) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("File Uploading...");
        pd.show();

        final String timestamp = String.valueOf(System.currentTimeMillis());
        final StorageReference reference = storageReference.child("grade/" + studentUID + '/' + timestamp + ".pdf");

        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        Uri uri = uriTask.getResult();

                        assRef = FirebaseDatabase.getInstance().getReference("Classroom").child(currentClassCode).child("Assignment").child(assId);
                        DatabaseReference submissionRef = assRef.child("Submission").child(studentUID);
                        submissionRef.child("gradeFileName").setValue(displayName);
                        submissionRef.child("gradedTime").setValue(String.valueOf(System.currentTimeMillis()));
                        submissionRef.child("gradedFileUri").setValue(uri.toString());
                        submissionRef.child("gradedFileUri").setValue(uri.toString());

                        Toast.makeText(Y_AsgmDetails.this, "File Uploaded Successfully!!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        finish();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        pd.setMessage("Uploaded : " + (int) percent + "%");
                    }
                });
    }
    public void checkRole(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String role = snapshot.child("Users").child(uid).child("role").getValue(String.class);
                isEducator = role.equalsIgnoreCase("Educator");

                if (isEducator) {
                    DataSnapshot assignmentSnapshot = snapshot.child("Classroom").child(currentClassCode).child("Assignment").child(assId);
                    checkAssignmentStatus(assignmentSnapshot);
                    int numofcheck = 0;
                    for (DataSnapshot submissionSnapshot: assignmentSnapshot.child("Submission").getChildren()) {
                        if(submissionSnapshot.getKey().equals(studentUID)){
                            submissionTable.setVisibility(View.VISIBLE);
                            dltButton.setVisibility(View.GONE);
                            checkSubmissionStatus(assignmentSnapshot.child("Submission").child(studentUID));
                            // graded
                            if(submissionSnapshot.child("score").exists()){
                                gradeButton.setVisibility(View.GONE);
                                gradingStatus.setText(submissionSnapshot.child("score").getValue(String.class));
                            }
                            // ready for graded
                            else{
                                teacherGradeLayout.setVisibility(View.VISIBLE);
                                gradeButton.setVisibility(View.VISIBLE);
                            }
                            break;
                        }
                        numofcheck ++;
                    }
                    if(numofcheck == assignmentSnapshot.child("Submission").getChildrenCount()){
                        if(studentUID.isEmpty()){
                            teacherGradeLayout.setVisibility(View.VISIBLE);
                            submissionTable.setVisibility(View.GONE);
                            studentLayout.setVisibility(View.GONE);
                            dltButton.setVisibility(View.VISIBLE);
                            gradeButton.setVisibility(View.GONE);
                        }
                        else{
                            submissionTable.setVisibility(View.VISIBLE);
                            dltButton.setVisibility(View.GONE);
                            submissionStatus.setText("Not Attempt");
                        }
                    }
                } else {
                    submissionTable.setVisibility(View.VISIBLE);
                    studentLayout.setVisibility(View.VISIBLE);
                    teacherGradeLayout.setVisibility(View.GONE);

                    assRef = FirebaseDatabase.getInstance().getReference("Classroom").child(currentClassCode).child("Assignment").child(assId);
                    assRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            checkAssignmentStatus(snapshot);
                            checkSubmissionStatus(snapshot.child("Submission").child(uid));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle onCancelled
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }


}

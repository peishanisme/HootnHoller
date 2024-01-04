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

public class Student_AsgmDetails extends AppCompatActivity {

    private Button addSubmissionButton, uploadButton, cancelButton;
    private View backButton;
    private TextView title, openTime, dueTime, description, fileName, submissionStatus, timeRemaining, fileSubmission, gradingStatus, submissionTime, submissionComment;
    private EditText uploadFileName, assComment;
    ImageView addFile;
    private LinearLayout submitLayout;
    String assId, currentClassCode;
    String displayName = null;
    DatabaseReference assRef;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    String uid = auth.getCurrentUser().getUid();
    CardView file;

    StorageReference storageReference;
    private Uri selectedFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_asgn_details);

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
        submitLayout.setVisibility(View.GONE);

        assId = getIntent().getStringExtra("assID");
        currentClassCode = getIntent().getStringExtra("classCode");
        storageReference = FirebaseStorage.getInstance().getReference();

        assRef = FirebaseDatabase.getInstance().getReference("Classroom").child(currentClassCode).child("Assignment").child(assId);
        assRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                title.setText(snapshot.child("title").getValue(String.class));
                String openTimestamp = snapshot.child("uploadDate").getValue(String.class);
                openTime.setText("Opened: " + convertTimestampToDateTime(Long.parseLong(openTimestamp)));
                String dueTimestamp = snapshot.child("dueDate").getValue(String.class);
                dueTime.setText("Due: " + convertTimestampToDateTime(Long.parseLong(dueTimestamp)));
                description.setText(snapshot.child("description").getValue(String.class));
                fileName.setText(snapshot.child("fileName").getValue(String.class));
                String fileUri = snapshot.child("fileUri").getValue(String.class);

                long currentTimeMillis = System.currentTimeMillis();
                long dueTimeMillis = Long.parseLong(dueTimestamp);

                if (dueTimeMillis > currentTimeMillis) {
                    long timeRemainingMillis = dueTimeMillis - currentTimeMillis;
                    String timeRemainingString = formatTimeRemaining(timeRemainingMillis);
                    timeRemaining.setText(timeRemainingString);
                } else {
                    timeRemaining.setText("Time's up!");
                }

                checkSubmissionStatus(snapshot.child("Submission").child(uid));

                file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openFile(fileUri);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });

        backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                    Toast.makeText(Student_AsgmDetails.this, "Please select a file", Toast.LENGTH_SHORT).show();
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


                        Toast.makeText(Student_AsgmDetails.this, "File Uploaded Successfully!!", Toast.LENGTH_SHORT).show();
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
}

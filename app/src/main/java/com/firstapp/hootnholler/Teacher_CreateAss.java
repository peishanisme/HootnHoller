package com.firstapp.hootnholler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firstapp.hootnholler.entity.Assignment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Teacher_CreateAss extends AppCompatActivity {
    EditText dueDate, pdfName, assTitle, assDescription;
    Calendar myCalendar;
    DatabaseReference assDatabase;
    StorageReference storageReference;
    Button btnAssign;
    String currentClassCode, assId;
    TextView showDueDate,showDueTime, addTime;
    ImageButton buttonCalendar, backButton, btnAttachFile;
    long dueTimestamp;

    String date,time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_create_ass);

        currentClassCode = getIntent().getStringExtra("classCode");

        showDueDate = (TextView) findViewById(R.id.showDate);
        showDueTime = (TextView) findViewById(R.id.showTime);
        buttonCalendar = (ImageButton) findViewById(R.id.btnCalendar);
        addTime = (TextView) findViewById(R.id.addTime);
        backButton = (ImageButton) findViewById(R.id.backButton);
        btnAssign = (Button) findViewById(R.id.btnAssign);
        pdfName = (EditText) findViewById(R.id.pdfName);
        assTitle = (EditText) findViewById(R.id.assTitle);
        assDescription = (EditText) findViewById(R.id.assDescription);
        btnAttachFile = (ImageButton) findViewById(R.id.btnAttachFile);
        
        storageReference = FirebaseStorage.getInstance().getReference();
        assDatabase = FirebaseDatabase.getInstance().getReference("Classroom")
                .child(currentClassCode)
                .child("Assignment");


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Teacher_CreateAss.this, Teacher_UpcomingAsgm.class);
                startActivity(intent);
            }
        });

        buttonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateDialog();
            }
        });

        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeDialog();
            }
        });

        btnAssign.setEnabled(false);
        btnAttachFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFiles();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData()!=null) {
            Uri uri = data.getData();

            String uriString=uri.toString();
            File myFile=new File(uriString);
            String path=myFile.getAbsolutePath();
            String displayName = null;

            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = this.getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        if (displayNameIndex != -1) {
                            displayName = cursor.getString(displayNameIndex);
                        } else {

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
            btnAssign.setEnabled(true);
            pdfName.setText(displayName);
            btnAssign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(assTitle.getText().toString())) {
                        Toast.makeText(Teacher_CreateAss.this, "Please set assignment title", Toast.LENGTH_SHORT).show();
                        return;

                    } else if (TextUtils.isEmpty(showDueDate.getText().toString()) || TextUtils.isEmpty(showDueTime.getText().toString())) {
                        Toast.makeText(Teacher_CreateAss.this, "Please set due date and time", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else{
                        UploadFiles(data.getData());
                    }
                }

            });
        }
    }

    private void UploadFiles(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File Uploading...");
        progressDialog.show();

        final String timestamp = String.valueOf(System.currentTimeMillis());
        StorageReference reference = storageReference.child("Uploads/"+timestamp+".pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isComplete());
                        Uri url = uriTask.getResult();

                        Assignment assClass = new Assignment(
                                assTitle.getText().toString(),
                                assDescription.getText().toString(),
                                timestamp,
                                Long.toString(dueTimestamp),
                                pdfName.getText().toString(),
                                url.toString()
                        );
                        assDatabase.child(assDatabase.push().getKey()).setValue(assClass);

                        Toast.makeText(Teacher_CreateAss.this, "File Uploaded!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        finish();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100.0 * snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded: " + (int)progress + "%");
                    }
                });
    }



    private void selectFiles() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF Files"),1);
    }

    private void openTimeDialog() {
            TimePickerDialog timeDialog = new TimePickerDialog(this,new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                    int seconds = 0;
                    showDueTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hours, minutes));

                    updateDueTimestamp();
                }
            },15,00,true);

            timeDialog.show();
    }

    private void openDateDialog() {
        DatePickerDialog dateDialog = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                showDueDate.setText(String.valueOf(day)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year));

                updateDueTimestamp();
            }
        },2024,0,15);

        dateDialog.show();
    }

        public static long convertDateTimeToTimestamp(String inputDateTime) {
            try {
                // Define the format of your input date and time
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault());

                // Parse the input date and time string to get a Date object
                Date date = sdf.parse(inputDateTime);

                // Convert the Date object to a timestamp
                return date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
                // Handle the ParseException (invalid date format)
                return -1; // Return an error value
            }
        }

        public static String convertTimestampToDateTime(long timestamp){
            try{
                Date currentDate = (new Date(timestamp));
                SimpleDateFormat sfd = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss", Locale.getDefault());
                return sfd.format(currentDate);
            } catch(Exception e){
                return "date";
            }

        }

    private void updateDueTimestamp() {
        String dueDateTimeString = showDueDate.getText().toString() + " " + showDueTime.getText().toString();
        dueTimestamp = convertDateTimeToTimestamp(dueDateTimeString);
    }
}
package com.firstapp.hootnholler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Teacher_CreateAss extends AppCompatActivity {
    EditText dueDate, pdfName;
    Calendar myCalendar;
    DatabaseReference assDatabase;
    DatabaseReference uploadReference;
    StorageReference storageReference;
    Button btnAssign;
    String currentClassCode, assId;
    TextView showDueDate,showDueTime, addTime;
    ImageButton buttonCalendar, backButton, btnAttachFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_create_ass);

        //dueDate = (EditText) findViewById(R.id.dueDatePicker);
        Button btnAssign = (Button)findViewById(R.id.btnAssign);
        showDueDate = (TextView)findViewById(R.id.showDate);
        showDueTime = (TextView)findViewById(R.id.showTime);
        buttonCalendar = (ImageButton)findViewById(R.id.btnCalendar);
        addTime = (TextView)findViewById(R.id.addTime);
        backButton = (ImageButton)findViewById(R.id.backButton);
        btnAssign = (Button)findViewById(R.id.btnAssign);
        pdfName = (EditText)findViewById(R.id.pdfName);
        btnAttachFile = (ImageButton)findViewById(R.id.btnAttachFile);

        currentClassCode = getIntent().getStringExtra("classCode");

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

        btnAttachFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFiles();
            }
        });


        /*DatePickerDialog.OnDateSetListener dueDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        dueDate.setOnClickListener(view -> {
            new DatePickerDialog(Teacher_CreateAss.this,dueDateListener,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });*/


        btnAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assDatabase = FirebaseDatabase.getInstance().getReference()
                        .child("Classroom")
                        .child(currentClassCode)
                        .child("Assignment");

                EditText addAssTitle = (EditText) findViewById(R.id.assTitle);
                EditText addDescription = (EditText) findViewById(R.id.assDescription);
                //EditText addDueDate = (EditText) findViewById(R.id.dueDatePicker);
                long timestamp = System.currentTimeMillis();

                // Generate a unique key for assignment
                assId = assDatabase.push().getKey();

                // Perform a null check before using the key
                if (assId != null) {
                    DatabaseReference assRef = assDatabase.child(assId);

                    String AssTitle = addAssTitle.getText().toString();
                    String AssDescription = addDescription.getText().toString();
                    String dueDateString = showDueDate.getText().toString();
                    String dueTimeString = showDueTime.getText().toString();

                    // Concatenate date and time strings to create a DateTime string
                    String dueDateTimeString = dueDateString + " " + dueTimeString;

                    // Convert the DateTime string to a Date object
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
                    Date dueDate;
                    try {
                        dueDate = dateFormat.parse(dueDateTimeString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return;
                    }

                    // Convert the Date object to a Timestamp
                    //Timestamp dueTimeStamp = new Timestamp(dueDate);

                    // Convert the Date object to a Firebase Timestamp
                    com.google.firebase.Timestamp dueTimeStamp = new com.google.firebase.Timestamp(dueDate);

                    //long dueTimeStamp = getCurrentDateInTimestamp();

                    assRef.child("title").setValue(AssTitle);
                    assRef.child("description").setValue(AssDescription);
                    assRef.child("dueDate").setValue(dueTimeStamp);
                    assRef.child("openDate");
                    assRef.child("uploadTime").setValue(timestamp);
                    assRef.child("attachment");
                }
            }
        });

        //PDF Database
        storageReference = FirebaseStorage.getInstance().getReference();
        uploadReference = FirebaseDatabase.getInstance().getReference()
                .child("Classroom")
                .child(currentClassCode)
                .child("Assignment")
                .child(assId)
                .child("Attachment");

        // Generate a unique key for attachment
        /*String attachmentKey = uploadReference.push().getKey();

        if (attachmentKey != null) {
            DatabaseReference uploadId = uploadReference.child(attachmentKey);
            uploadId.child("url").setValue();
    }*/

    /*private void updateLabel() {
        String myFormat = "MM/dd/yy EEEE";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        dueDate.setText((dateFormat.format(myCalendar.getTime())));
    }

    private long getCurrentDateInTimestamp(){
        return Calendar.getInstance().getTimeInMillis();
    }

    private String[] longIntoString(long milliseconds){
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        return new String[]{
            dateFormat.format(milliseconds),timeFormat.format(milliseconds)
        };

    }*/
    }

    private void openTimeDialog() {
        TimePickerDialog timeDialog = new TimePickerDialog(this,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                int seconds = 0;
                showDueTime.setText(String.valueOf(hours)+":"+String.valueOf(minutes)+":"+String.valueOf(seconds));
            }
        },15,00,true);

        timeDialog.show();
    }

    private void openDateDialog() {
        DatePickerDialog dateDialog = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                showDueDate.setText(String.valueOf(year)+"/"+String.valueOf(month+1)+"/"+String.valueOf(day));
            }
        },2023,0,15);

        dateDialog.show();
    }

    private void selectFiles() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF Files"),1);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData()!=null) {
            UploadFiles(data.getData());
        }
    }

    private void UploadFiles(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference reference = storageReference.child("Uploads/"+System.currentTimeMillis()+".pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isComplete());
                        Uri url = uriTask.getResult();

                        pdfClass pdfClass = new pdfClass(pdfName.getText().toString(),url.toString());
                        uploadReference.child(uploadReference.push().getKey()).setValue(pdfClass);

                        Toast.makeText(Teacher_CreateAss.this, "File Uploaded!", Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100.0 * snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded: " + (int)progress + "%");
                    }
                });
    }
}
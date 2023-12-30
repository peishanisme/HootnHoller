package com.firstapp.hootnholler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Teacher_CreateAss extends AppCompatActivity {
    EditText dueDate;
    Calendar myCalendar;
    DatabaseReference assDatabase;

    TextView showDueDate,showDueTime, addTime;
    ImageButton buttonCalendar, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_create_ass);

        dueDate = (EditText) findViewById(R.id.dueDatePicker);
        Button btnAssign = (Button)findViewById(R.id.btnAssign);
        showDueDate = (TextView)findViewById(R.id.showDate);
        showDueTime = (TextView)findViewById(R.id.showTime);
        buttonCalendar = (ImageButton)findViewById(R.id.btnCalendar);
        addTime = (TextView)findViewById(R.id.addTime);
        backButton = (ImageButton)findViewById(R.id.backButton);

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
                assDatabase = FirebaseDatabase.getInstance().getReference("Assignment");

                EditText addAssTitle = (EditText) findViewById(R.id.assTitle);
                EditText addDescription = (EditText) findViewById(R.id.assDescription);
                EditText addDueDate = (EditText) findViewById(R.id.dueDatePicker);

                // Generate a unique key for your data
                String assId = assDatabase.push().getKey();

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
                    assRef.child("uploadTime");
                    assRef.child("attachment");
                }
            }
        });
    }

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

    int requestCode = 1;

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        Context context = getApplicationContext();
        if (requestCode == requestCode && resultCode == Activity.RESULT_OK){
            if (data == null){
                return;
            }
            Uri uri = data.getData();
            Toast.makeText(context, uri.getPath(),Toast.LENGTH_SHORT).show();
        }
    }

    public void openFilePicker(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent,requestCode);
    }

}
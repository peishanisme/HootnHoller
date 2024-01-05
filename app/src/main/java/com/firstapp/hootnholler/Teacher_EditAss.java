package com.firstapp.hootnholler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firstapp.hootnholler.adapter.LMAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Teacher_EditAss extends AppCompatActivity {
    ImageButton back;
    String assKey, currentClassCode;
    TextView title, description, openDate, dueDate, filename;
    CardView file;
    DatabaseReference assRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_edit_ass);

        assKey = getIntent().getStringExtra("assKey");
        currentClassCode = getIntent().getStringExtra("classCode");

        assRef = FirebaseDatabase.getInstance().getReference("Classroom")
                .child(currentClassCode)
                .child("Assignment")
                .child(assKey);

        back = (ImageButton) findViewById(R.id.backButton);
        title = (TextView) findViewById(R.id.assTitle);
        openDate = (TextView)findViewById(R.id.openDate);
        dueDate = (TextView)findViewById(R.id.dueDate);
        description = (TextView) findViewById(R.id.description);
        filename = (TextView) findViewById(R.id.assFileName);
        file = (CardView) findViewById(R.id.file);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Teacher_EditAss.this, Teacher_Assignment.class);
                startActivity(intent);
            }
        });

        assRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String asgmTitle = snapshot.child("title").getValue(String.class);
                    String asgmDescription = snapshot.child("description").getValue(String.class);
                    String asgmOpenDate = snapshot.child("uploadDate").getValue(String.class);
                    String readableOpenTime = LMAdapter.convertTimestampToDateTime(Long.parseLong(asgmOpenDate));
                    String asgmDueDate = snapshot.child("dueDate").getValue(String.class);
                    String readableDueTime = LMAdapter.convertTimestampToDateTime(Long.parseLong(asgmDueDate));
                    String fileName = snapshot.child("fileName").getValue(String.class);
                    String fileUri = snapshot.child("fileUri").getValue(String.class);

                    title.setText(asgmTitle);
                    description.setText(asgmDescription);
                    openDate.setText("Opened: " + readableOpenTime);
                    dueDate.setText("Due: " + readableDueTime);
                    filename.setText(fileName);

                    file.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openFile(fileUri);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

}
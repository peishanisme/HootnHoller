package com.firstapp.hootnholler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Teacher_CreateAss extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_create_ass);

    }

    public void buttonAssign(View v) {
        DatabaseReference assDatabase;
        assDatabase = FirebaseDatabase.getInstance().getReference("Assignment");

        EditText addAssTitle = (EditText) findViewById(R.id.assTitle);
        EditText addDescription = (EditText) findViewById(R.id.assDescription);
        EditText addDueDate = (EditText) findViewById(R.id.dueDate);

        String AssTitle = addAssTitle.getText().toString();
        String AssDescription = addDescription.getText().toString();
        String AssDueDate = addDueDate.getText().toString();

        // Generate a unique key for your data
        String uniqueId = assDatabase.push().getKey();

        // Perform a null check before using the key
        if (uniqueId != null) {
            DatabaseReference assRef = assDatabase.child(uniqueId);
            assRef.child("title").setValue(AssTitle);
            assRef.child("description").setValue(AssDescription);
            assRef.child("dueDate").setValue(AssDueDate);
        }
    }
}
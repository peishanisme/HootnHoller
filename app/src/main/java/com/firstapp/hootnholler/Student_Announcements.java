package com.firstapp.hootnholler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

public class Student_Announcements extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    ArrayList<Announcement_ArrayAdapter> arrayList = new ArrayList<Announcement_ArrayAdapter>();
    public CardView a1;
    public ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        recyclerView = findViewById(R.id.announcementList);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        arrayList.add(new Announcement_ArrayAdapter("Title", "Posted On"));
        Announcement_RecyclerViewAdapter recyclerViewAdapter = new Announcement_RecyclerViewAdapter(this,arrayList);
        recyclerView.setAdapter(recyclerViewAdapter);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_announcements);

        a1 = (CardView) findViewById(R.id.announcement1);
        backButton = (ImageButton) findViewById(R.id.btnBack);

        a1.setOnClickListener(this);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent i;

        if (v.getId() == R.id.announcement1) {
            i = new Intent(this, Student_AnnouncementDetails.class);
            startActivity(i);
        }
    }
}
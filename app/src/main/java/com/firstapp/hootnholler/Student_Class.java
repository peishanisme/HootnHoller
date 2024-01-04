package com.firstapp.hootnholler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

public class Student_Class extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    public CardView announcement, assignment, materials;
    public ImageButton btnPopUp, backButton;
    String currentClassCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_class);
        currentClassCode=getIntent().getStringExtra("classCode");
        announcement = (CardView) findViewById(R.id.announcement);
        assignment = (CardView) findViewById(R.id.TasksAssignment);
        materials = (CardView) findViewById(R.id.LearningMaterials);
        btnPopUp = (ImageButton) findViewById(R.id.menu);
        backButton = (ImageButton) findViewById(R.id.btnBack);

        System.out.println("student_class: "+currentClassCode);


//        btnPopUp.setOnClickListener(this);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Student_Class.this, Student_Announcements.class);
                intent.putExtra("classCode", currentClassCode);
                startActivity(intent);
            }
        });

        assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Student_Class.this, Student_Assignment.class);
                intent.putExtra("classCode", currentClassCode);
                startActivity(intent);
            }
        });

        materials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Student_Class.this, Student_LearningMaterials.class);
                intent.putExtra("classCode", currentClassCode);
                startActivity(intent);
            }
        });


    }



    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.student_popup_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.item1) {
            Intent i = new Intent(this, Student_People.class);
            startActivity(i);
        }
        return true;
    }
}

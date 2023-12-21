package com.example.classroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

public class Student_Class extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    public CardView announcement, assignment, materials;
    public ImageButton btnPopUp, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_class);
        announcement = (CardView) findViewById(R.id.announcement);
        assignment = (CardView) findViewById(R.id.TasksAssignment);
        materials = (CardView) findViewById(R.id.LearningMaterials);
        btnPopUp = (ImageButton) findViewById(R.id.menu);
        backButton = (ImageButton) findViewById(R.id.btnBack);

        announcement.setOnClickListener(this);
        assignment.setOnClickListener(this);
        materials.setOnClickListener(this);
        btnPopUp.setOnClickListener(this);
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

        if (v.getId() == R.id.announcement) {
            i = new Intent(this, Student_Announcements.class);
            startActivity(i);

        } else if (v.getId() == R.id.TasksAssignment) {
            i = new Intent(this, Student_UpcomingAssignment.class);
            startActivity(i);

        } else if (v.getId() == R.id.LearningMaterials) {
            i = new Intent(this, Student_LearningMaterials.class);
            startActivity(i);
        }

        else if (v.getId() == R.id.menu) {
            showPopup(v);
        }
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
package com.firstapp.hootnholler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firstapp.hootnholler.adapter.Announcement_Adapter;
import com.firstapp.hootnholler.entity.Announcement;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Teacher_Announcement extends AppCompatActivity {
    String currentClassCode;
    CardView announcement;
    Dialog mdialog;
    private RecyclerView announcementList;
    private Announcement_Adapter announcementAdapter;
    private ArrayList<Announcement> announcementDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_activity_announcement);
        currentClassCode = getIntent().getStringExtra("classCode");
        announcement = findViewById(R.id.AnnouncementWindow);

        // Initialize UI elements
        announcementList = findViewById(R.id.announcementList);

        // Set up RecyclerView and adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        announcementList.setLayoutManager(layoutManager);
        announcementDataList = new ArrayList<>(); // Initialize the list
        announcementAdapter = new Announcement_Adapter(Teacher_Announcement.this,announcementDataList,currentClassCode);
        announcementList.setAdapter(announcementAdapter);

        // Retrieve existing announcements from the database
        DatabaseReference AnnouncementRef = FirebaseDatabase.getInstance().getReference("Classroom")
                .child(currentClassCode)
                .child("Announcement");

        AnnouncementRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the existing list
                announcementDataList.clear();

                // Iterate through the dataSnapshot to retrieve announcements
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Announcement announcement = dataSnapshot.getValue(Announcement.class);
                    if (announcement != null) {
                        announcementDataList.add(announcement);
                    }
                }

                // Notify the adapter of the data set change
                announcementAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
                Toast.makeText(Teacher_Announcement.this, "Failed to retrieve announcements", Toast.LENGTH_SHORT).show();
            }
        });




        // Set a click listener for the card view
        announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display the pop-up window
                showAnnouncementPopup();
            }
        });
    }

    private void showAnnouncementPopup() {
                mdialog = new Dialog(Teacher_Announcement.this);
                mdialog.setContentView(R.layout.pop_out_make_announcement);

                DatabaseReference AnnouncementRef = FirebaseDatabase.getInstance().getReference("Classroom").child(currentClassCode).child("Announcement");
                EditText announcementTitle = mdialog.findViewById(R.id.announcementTitle);
                EditText announcementContent = mdialog.findViewById(R.id.announcementContent);
                Button submit = mdialog.findViewById(R.id.submit);
                ImageView close = mdialog.findViewById(R.id.close);
                long timestamp = System.currentTimeMillis();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String AnnoucementContent = announcementContent.getText().toString();
                        String AnnouncementTitle = announcementTitle.getText().toString();

                        // Add a null check for AnnouncementRef
                        if (AnnouncementRef != null) {
                            DatabaseReference newAnnouncementRef = AnnouncementRef.child(String.valueOf(timestamp));
                            newAnnouncementRef.child("announcementTitle").setValue(AnnouncementTitle);
                            newAnnouncementRef.child("announcementContent").setValue(AnnoucementContent);
                            newAnnouncementRef.child("timestamp").setValue(timestamp);
                            Toast.makeText(Teacher_Announcement.this, "Announcement created", Toast.LENGTH_SHORT).show();

                            mdialog.dismiss();
                        } else {
                            Toast.makeText(Teacher_Announcement.this, "Error creating announcement", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mdialog.dismiss();
                    }
                });
                mdialog.show();
            }


}

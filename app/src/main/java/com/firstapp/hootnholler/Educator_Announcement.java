package com.firstapp.hootnholler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.firstapp.hootnholler.adapter.Announcement_Adapter;
import com.firstapp.hootnholler.databinding.TeacherActivityAnnouncementBinding;
import com.firstapp.hootnholler.entity.Announcement;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Educator_Announcement extends AppCompatActivity {
    String currentClassCode,classOwner;
    CardView announcement;
    ImageView back;
    Dialog mdialog;
    private RecyclerView announcementList;
    private Announcement_Adapter announcementAdapter;
    private ArrayList<Announcement> announcementDataList;

    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    String uid=mAuth.getUid().toString();
    private boolean isCurrentUserClassOwner;
    TeacherActivityAnnouncementBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TeacherActivityAnnouncementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        currentClassCode = getIntent().getStringExtra("classCode");
        announcement = findViewById(R.id.AnnouncementWindow);
        back = binding.back;

        announcement.setVisibility(View.GONE);
        // Initialize UI elements
        announcementList = findViewById(R.id.announcementList);

        // Set up RecyclerView and adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        announcementList.setLayoutManager(layoutManager);
        announcementDataList = new ArrayList<>(); // Initialize the list

        announcementAdapter = new Announcement_Adapter(Educator_Announcement.this,announcementDataList,currentClassCode,isCurrentUserClassOwner);
        announcementList.setAdapter(announcementAdapter);

        // Retrieve existing announcements from the database
        DatabaseReference ClassroomRef = FirebaseDatabase.getInstance().getReference("Classroom")
                .child(currentClassCode);

        ClassroomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the existing list
                classOwner=snapshot.child("classOwner").getValue(String.class);

                if(uid.equals(classOwner)){
                    isCurrentUserClassOwner = true;
                    announcement.setVisibility(View.VISIBLE);
                    announcement.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Display the pop-up window
                            showAnnouncementPopup();
                        }
                    });}else{
                    isCurrentUserClassOwner=false;
                    announcement.setVisibility(View.GONE);
                }
                announcementAdapter.checkCurrentUserClassOwner(isCurrentUserClassOwner);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
                Toast.makeText(Educator_Announcement.this, "Failed to retrieve announcements", Toast.LENGTH_SHORT).show();
            }
        });

        ClassroomRef.child("Announcement").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                announcementDataList.clear();

                // Iterate through the dataSnapshot to retrieve announcements
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Announcement announcement = dataSnapshot.getValue(Announcement.class);
                    if (announcement != null) {
                        announcementDataList.add(announcement);
                    }
                }
                announcementDataList.sort((announcement1, announcement2) -> Long.compare(announcement2.getTimestamp(), announcement1.getTimestamp()));
                announcementAdapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Educator_Class.class);
                intent.putExtra("classCode", currentClassCode);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showAnnouncementPopup() {
                mdialog = new Dialog(Educator_Announcement.this);
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
                            Toast.makeText(Educator_Announcement.this, "Announcement created", Toast.LENGTH_SHORT).show();

                            mdialog.dismiss();
                        } else {
                            Toast.makeText(Educator_Announcement.this, "Error creating announcement", Toast.LENGTH_SHORT).show();
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

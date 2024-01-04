package com.firstapp.hootnholler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firstapp.hootnholler.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactInfo extends AppCompatActivity {

    private DatabaseReference contactReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        //Class dunno how to fetch
        contactReference = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());


        // Initialize views
        ImageView backButton = findViewById(R.id.backbutton);
        TextView roleShown = findViewById(R.id.role_shown);
        TextView classShown = findViewById(R.id.class_shown);
        Button messageButton = findViewById(R.id.message_button);

        // Fetch contact details from Firebase
        contactReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User contactDetails = dataSnapshot.getValue(User.class);

                    if (contactDetails != null) {
                        // Set contact details
                        roleShown.setText(contactDetails.getRole());
//                        Not sure how to get the class bcuz of db structure
//                        classShown.setText(contactDetails.getUserClass());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });

        // Back button click listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the ContactInfo activity and go back to the previous activity
            }
        });

        // Message button click listener
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactInfo.this, ChatWindow.class);
                getIntent().getStringExtra("contactUid");
                getIntent().getStringExtra("contactName");
                startActivity(intent);
            }
        });
    }
}

package com.firstapp.hootnholler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firstapp.hootnholler.adapter.LMAdapter;
import com.firstapp.hootnholler.databinding.TeacherActivityLmDetailsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Y_LM_Details extends AppCompatActivity {

    private String LMid, currentClassCode;
    TextView title, description, time, filename;
    ImageView backButton;
    CardView file;
    DatabaseReference LMRef;
    TeacherActivityLmDetailsBinding binding;
    private Button btnDeleteLM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TeacherActivityLmDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LMid = getIntent().getStringExtra("LMid");
        currentClassCode = getIntent().getStringExtra("classCode");

        LMRef = FirebaseDatabase.getInstance().getReference("Classroom").child(currentClassCode).child("Learning Materials").child(LMid);
        title = findViewById(R.id.lmTitle);
        description = findViewById(R.id.lmDescription);
        time = findViewById(R.id.lmTime);
        filename = findViewById(R.id.lmFileName);
        file=findViewById(R.id.file);
        btnDeleteLM = findViewById(R.id.btnDeleteLM);
        backButton=binding.back;

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Y_LM_Details.this, Y_LearningMaterials.class);
                intent.putExtra("classCode", currentClassCode);
                startActivity(intent);
            }
        });


        LMRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String filetitle=snapshot.child("title").getValue(String.class);
                    String filedescription=snapshot.child("description").getValue(String.class);
                    String Time=snapshot.child("timestamp").getValue(String.class);
                    String readableTime= LMAdapter.convertTimestampToDateTime(Long.parseLong(Time));
                    String fileName=snapshot.child("fileName").getValue(String.class);
                    String fileUri=snapshot.child("fileUri").getValue(String.class);

                    title.setText(filetitle);
                    description.setText(filedescription);
                    time.setText(readableTime);
                    filename.setText(fileName);
                    file.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Open the file using the file URI
                            openFile(fileUri);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });

        btnDeleteLM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LMRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Delete successful
                            Toast.makeText(Y_LM_Details.this, "Learning Material deleted", Toast.LENGTH_SHORT).show();
                            // Optionally, you may want to notify your RecyclerView of the change
                        } else {
                            // Delete failed
                            Toast.makeText(Y_LM_Details.this, "Failed to delete learning material", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                });
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













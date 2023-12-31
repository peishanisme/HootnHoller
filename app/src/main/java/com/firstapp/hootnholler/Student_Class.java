package com.firstapp.hootnholler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firstapp.hootnholler.databinding.ActivityStudentClassBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Student_Class extends AppCompatActivity {

    public CardView announcement, assignment, materials;
    TextView className, classSession, description, educatorName;
    public ImageView backButton;
    ImageView overflowMenu;
    String currentClassCode;
    DatabaseReference classroom;
    ActivityStudentClassBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentClassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        className = findViewById(R.id.className);
        classSession = findViewById(R.id.classSession);
        description = findViewById(R.id.description);
        educatorName = findViewById(R.id.educatorName);

        currentClassCode = getIntent().getStringExtra("classCode");
        classroom = FirebaseDatabase.getInstance().getReference("Classroom").child(currentClassCode);
        announcement = (CardView) findViewById(R.id.announcement);
        assignment = (CardView) findViewById(R.id.TasksAssignment);
        materials = (CardView) findViewById(R.id.LearningMaterials);
        overflowMenu = (ImageView) findViewById(R.id.menu);
        backButton = binding.back;


//        btnPopUp.setOnClickListener(this);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Student_MainActivity.class);
                intent.putExtra("FRAGMENT_TO_LOAD", "student_Class_Fragment");
                startActivity(intent);
                finish();
            }
        });
        overflowMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(Student_Class.this, view);
                popupMenu.inflate(R.menu.educator_overflow_menu);

                // Set the menu item click listener
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        return onOptionsItemSelected(menuItem);
                    }
                });

                // Show the popup menu
                popupMenu.show();
            }

        });
        classroom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String ClassName = snapshot.child("className").getValue(String.class);
                    String ClassDescription = snapshot.child("classDescription").getValue(String.class);
                    String ClassSessiom = snapshot.child("classSession").getValue(String.class);
                    String educatorUID = snapshot.child("classOwner").getValue(String.class);


                    className.setText(ClassName);
                    description.setText(ClassDescription);
                    classSession.setText(ClassSessiom);

                    DatabaseReference user = FirebaseDatabase.getInstance().getReference("Users").child(educatorUID);
                    user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                educatorName.setText(snapshot.child("fullname").getValue(String.class) + " -- Educator");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Student_Class.this, Y_Announcement.class);
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
                Intent intent = new Intent(Student_Class.this, Y_LearningMaterials.class);
                intent.putExtra("classCode", currentClassCode);
                startActivity(intent);
            }
        });

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.educator_overflow_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.viewPeople) {
            Intent intent = new Intent(Student_Class.this, Y_View_People.class);
            intent.putExtra("classCode", currentClassCode);
            startActivity(intent);


        } else if (id == R.id.viewClassCode) {
            String classCode = currentClassCode;
            Dialog dialog = new Dialog(Student_Class.this);
            dialog.setContentView(R.layout.pop_out_classcode);

            TextView ClassCode = dialog.findViewById(R.id.classCode);
            ImageView close_button = dialog.findViewById(R.id.close);
            View copyButton = dialog.findViewById(R.id.copy);

            ClassCode.setText(classCode);
            copyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Implement the logic to copy the connection key to the clipboard
                    ClipboardManager clipboard = (ClipboardManager) Student_Class.this.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Class Code", classCode);
                    clipboard.setPrimaryClip(clip);

                    // Show a toast indicating that the key has been copied
                    Toast.makeText(Student_Class.this, "Class code copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            });
            close_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            // Show the dialog
            dialog.show();



        }

        return super.onOptionsItemSelected(item);
    }
}

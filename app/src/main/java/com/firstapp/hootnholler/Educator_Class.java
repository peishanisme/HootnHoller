

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

import com.firstapp.hootnholler.databinding.TeacherActivityClassBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Educator_Class extends AppCompatActivity {
    String currentClassCode;
    DatabaseReference classroom;
    TextView className, classSession, classDescription, numberofStudents;
    CardView classDetails, announcement, taskAssignment, learningMaterials, feedback;
    ImageView overflowMenu;
    ImageView back;
    TeacherActivityClassBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TeacherActivityClassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentClassCode = getIntent().getStringExtra("classCode");
        classroom = FirebaseDatabase.getInstance().getReference("Classroom").child(currentClassCode);
        className = findViewById(R.id.className);
        classDescription = findViewById(R.id.classDescription);
        classSession = findViewById(R.id.classSession);
        numberofStudents = findViewById(R.id.studentOfClass);

        classDetails = findViewById(R.id.classDetails);
        announcement = findViewById(R.id.announcement);
        taskAssignment = findViewById(R.id.TasksAssignment);
        learningMaterials = findViewById(R.id.LearningMaterials);
        feedback = findViewById(R.id.Feedback);
        back = binding.back;
        overflowMenu = findViewById(R.id.overflow);

        overflowMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(Educator_Class.this, view);
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
                    long studentNum=snapshot.child("StudentsJoined").getChildrenCount();

                    className.setText(ClassName);
                    classDescription.setText(ClassDescription);
                    classSession.setText(ClassSessiom);
                    numberofStudents.setText(String.valueOf(studentNum)+" students");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Educator_Class.this, Y_Announcement.class);
                intent.putExtra("classCode", currentClassCode);
                startActivity(intent);
            }
        });

        taskAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Educator_Class.this, Educator_Assignment.class);
                intent.putExtra("classCode", currentClassCode);
                startActivity(intent);
            }
        });

        learningMaterials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Educator_Class.this, Y_LearningMaterials.class);
                intent.putExtra("classCode", currentClassCode);
                startActivity(intent);
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Educator_Class.this, Educator_FeedbackStudentList.class);
                intent.putExtra("classCode", currentClassCode);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Educator_Main_Activity.class);
                intent.putExtra("FRAGMENT_TO_LOAD", "educator_Classroom_Fragment");
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.educator_overflow_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.viewPeople) {
        Intent intent=new Intent(Educator_Class.this, Y_View_People.class);
        intent.putExtra("classCode",currentClassCode);
        startActivity(intent);

        } else if (id == R.id.viewClassCode) {
            String classCode = currentClassCode;
            Dialog dialog=new Dialog(Educator_Class.this);
            dialog.setContentView(R.layout.pop_out_classcode);

            TextView ClassCode=dialog.findViewById(R.id.classCode);
            ImageView close_button = dialog.findViewById(R.id.close);
            View copyButton = dialog.findViewById(R.id.copy);

            ClassCode.setText(classCode);
            copyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Implement the logic to copy the connection key to the clipboard
                    ClipboardManager clipboard = (ClipboardManager) Educator_Class.this.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Class Code", classCode);
                    clipboard.setPrimaryClip(clip);

                    // Show a toast indicating that the key has been copied
                    Toast.makeText(Educator_Class.this, "Class code copied to clipboard", Toast.LENGTH_SHORT).show();
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


        }            return super.onOptionsItemSelected(item);
    }

}

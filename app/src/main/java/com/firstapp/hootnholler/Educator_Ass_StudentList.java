package com.firstapp.hootnholler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firstapp.hootnholler.adapter.Ass_StudentList_Adapter;
import com.firstapp.hootnholler.databinding.TeacherActivityAssStudentlsitBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Educator_Ass_StudentList extends AppCompatActivity {
    String key,currentClassCode,assID;
    TextView assTitle,dueTime;
    RecyclerView recylerview;
    Ass_StudentList_Adapter Adapter;
    ArrayList<String>studentList=new ArrayList<>();
    ImageView back_button;
    DatabaseReference database= FirebaseDatabase.getInstance().getReference();
    TeacherActivityAssStudentlsitBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TeacherActivityAssStudentlsitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        key=getIntent().getStringExtra("key");
        currentClassCode=getIntent().getStringExtra("classCode");
        assID=getIntent().getStringExtra("assKey");

        back_button=binding.back;
        assTitle=findViewById(R.id.assTitle);
        dueTime=findViewById(R.id.dueTime);
        recylerview=findViewById(R.id.studentList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recylerview.setLayoutManager(layoutManager);
        Adapter = new Ass_StudentList_Adapter(Educator_Ass_StudentList.this,studentList,currentClassCode,key,assID);
        recylerview.setAdapter(Adapter);

                DatabaseReference classroomRef = FirebaseDatabase.getInstance().getReference("Classroom").child(currentClassCode);
        DatabaseReference User = FirebaseDatabase.getInstance().getReference("Users");

        classroomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    DataSnapshot assignment=snapshot.child("Assignment").child(assID);
                    assTitle.setText(assignment.child("title").getValue(String.class));
                    dueTime.setText(Educator_CreateAss.convertTimestampToDateTime(Long.parseLong(assignment.child("uploadDate").getValue(String.class))));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        classroomRef.child("StudentsJoined").addListenerForSingleValueEvent(new ValueEventListener() {

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the list before adding new data
                studentList.clear();

                for (DataSnapshot studentSnapshot : snapshot.getChildren()) {
                    String studentKey = studentSnapshot.getKey();
                    studentList.add(studentKey);
                    // Notify the adapter that the data set has changed
                    Adapter.notifyDataSetChanged();
                }
            }

            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Educator_Ass_StudentList.this, Educator_Assignment.class);
                intent.putExtra("classCode",currentClassCode);
                startActivity(intent);
            }
        });


    }
}
package com.firstapp.hootnholler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firstapp.hootnholler.adapter.TeacherStudentFeedbackPager_Adapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FeedbackList extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 container;
    private TextView studentName;
    private FloatingActionButton createFeedback;
    private TeacherStudentFeedbackPager_Adapter pageAdapter;
    private String studentUID, currentClassCode, role, UID;
    private DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference("Users");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_list);

        tabLayout = findViewById(R.id.tabTeacherStudentFeedback);
        container = findViewById(R.id.TeacherStudentFeedbackList);
        studentName = findViewById(R.id.TeacherStudentNameFeedback);
        studentUID = getIntent().getExtras().get("studentUID").toString();
        currentClassCode = getIntent().getExtras().get("classCode").toString();
        pageAdapter = new TeacherStudentFeedbackPager_Adapter(this, currentClassCode, studentUID);
        container.setAdapter(pageAdapter);
        createFeedback = findViewById(R.id.createFeedback);
        getStudentDetails();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                container.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        container.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        createFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FeedbackList.this, Teacher_CreateFeedback.class);
                intent.putExtra("studentUID", studentUID);
                intent.putExtra("classCode", currentClassCode);
                startActivity(intent);
            }
        });
    }

    public void getStudentDetails(){
        this.UserRef.child(studentUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentName.setText(snapshot.child("fullname").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void checkRole(){
        UID = mAuth.getUid();
        UserRef.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                role = snapshot.child("role").getValue(String.class);
                if(role.equalsIgnoreCase("Educator")){
                    createFeedback.setVisibility(View.VISIBLE);
                }
                else{
                    createFeedback.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
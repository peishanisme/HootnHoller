package com.firstapp.hootnholler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditAccount_Activity extends AppCompatActivity  {
//    implements View.OnClickListener

    ImageView back_button;
    EditText fullname,birthday,phonenumber,school,student_class;
    LinearLayout layoutList1,layoutList2;
    View school_layout,studentLevel_layout,studentClass_layout,parentConnectionkey_layout,eduSubject_layout;
    Button submit,addSubject,addConnectionKey;
    RadioGroup gender;
    Spinner level;
    RadioButton genderSelection,female,male;
    String student_level,role;
    ArrayList<String> schoolLevel;
    DatabaseReference UserRef,StudentRef,ParentRef,EduRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        //Edit Text
        fullname=findViewById(R.id.fullname);
        birthday=findViewById(R.id.birthday);
        phonenumber=findViewById(R.id.phonenumber);
        school=findViewById(R.id.school);
        student_class=findViewById(R.id.student_class);

        //radio button
        gender=(RadioGroup) findViewById(R.id.gender);
        genderSelection=(RadioButton)findViewById(gender.getCheckedRadioButtonId()) ;
        male=findViewById(R.id.male);
       female=findViewById(R.id.female);

        //button
        addSubject=findViewById(R.id.addSubject);
        addConnectionKey=findViewById(R.id.addKey);
        back_button=findViewById(R.id.back_button);
        submit=findViewById(R.id.SubmitButton);

        //dynamic view for subject and connection key
        layoutList1 = findViewById(R.id.key_layout);
        addConnectionKey = findViewById(R.id.addKey);
//        addConnectionKey.setOnClickListener(this);

        layoutList2 = findViewById(R.id.subject_layout);
        addSubject = findViewById(R.id.addSubject);
//        addSubject.setOnClickListener(this);

        //Spinner
        level=findViewById(R.id.student_level);
        schoolLevel=new ArrayList<>();
        schoolLevel.add("Standard 1");
        schoolLevel.add("Standard 2");
        schoolLevel.add("Standard 3");
        schoolLevel.add("Standard 4");
        schoolLevel.add("Standard 5");
        schoolLevel.add("Standard 6");

        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,schoolLevel);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        level.setAdapter(adapter);

        //layout
        school_layout=findViewById(R.id.school_layout);
        studentLevel_layout=findViewById(R.id.student_level_layout);
        studentClass_layout=findViewById(R.id.student_class_layout);
        parentConnectionkey_layout=findViewById(R.id.parent_connectionkey_layout);
        eduSubject_layout=findViewById(R.id.educator_subject_layout);

        //Visibility
        school_layout.setVisibility(View.GONE);
        studentLevel_layout.setVisibility(View.GONE);
        studentClass_layout.setVisibility(View.GONE);
        parentConnectionkey_layout.setVisibility(View.GONE);
        eduSubject_layout.setVisibility(View.GONE);

        // Initialize Firebase authentication, retrieve the current user ID, and get the database reference
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserID = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
         StudentRef= FirebaseDatabase.getInstance().getReference().child("Student").child(currentUserID);
         ParentRef= FirebaseDatabase.getInstance().getReference().child("Parent").child(currentUserID);
         EduRef= FirebaseDatabase.getInstance().getReference().child("Educator").child(currentUserID);


        UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    role = snapshot.child("role").getValue(String.class);
                    fullname.setText(snapshot.child("fullname").getValue(String.class));
                    birthday.setText(snapshot.child("birthday").getValue(String.class));
                    phonenumber.setText(snapshot.child("phone_number").getValue(String.class));
                    String genderFromDatabase = snapshot.child("gender").getValue(String.class);
                    // Set the corresponding radio button as checked
                    if ("Male".equalsIgnoreCase(genderFromDatabase)) {
                        male.setChecked(true);
                    } else if ("Female".equalsIgnoreCase(genderFromDatabase)) {
                        female.setChecked(true);
                    }
                    checkRole(role);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkRole(String role) {
        if(role.equalsIgnoreCase("student")){
            school_layout.setVisibility(View.VISIBLE);
            studentLevel_layout.setVisibility(View.VISIBLE);
            studentClass_layout.setVisibility(View.VISIBLE);
            StudentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        school.setText(snapshot.child("school").getValue(String.class));
                        student_class.setText(snapshot.child("student_class").getValue(String.class));
                        // Get the student level from the database
                        String studentLevelFromDatabase = snapshot.child("level").getValue(String.class);
                        int position = schoolLevel.indexOf(studentLevelFromDatabase);
                        level.setSelection(position);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }else if(role.equalsIgnoreCase("parent")){
            parentConnectionkey_layout.setVisibility(View.VISIBLE);

        }
    }


}
package com.firstapp.hootnholler;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firstapp.hootnholler.adapter.Educator_Subject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Profile_Fragment extends Fragment {

    // Declare views
    private TextView Fullname, Role, Gender,Birthday,PhoneNumber,School,Level,Class;
    private View Gender_layout,Birthday_layout,phoneNum_layout,School_layout,Level_layout,Class_layout,Subject_layout,ParentMonitored_layout, ConnectionKey_layout,EditAccount_layout,Logout_layout;
    ArrayList<String> EducatorSubject;
    Educator_Subject Educator_Subject;
    RecyclerView recyclerViewSubject;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    String uid = auth.getCurrentUser().getUid();
    DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
    DatabaseReference StudentRef = FirebaseDatabase.getInstance().getReference("Student").child(uid);
    DatabaseReference EduRef = FirebaseDatabase.getInstance().getReference("Educator").child(uid);
    DatabaseReference ParentRef = FirebaseDatabase.getInstance().getReference("Parent").child(uid);


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_, container, false);

        // Find views by ID
        Fullname = view.findViewById(R.id.fullname_shown);
        Role = view.findViewById(R.id.role_shown);
        Gender = view.findViewById(R.id.gender_shown);
        Birthday = view.findViewById(R.id.birthday_shown);
        PhoneNumber = view.findViewById(R.id.phoneNum_shown);
        School = view.findViewById(R.id.school_shown);
        Level = view.findViewById(R.id.studentLevel_shown);
        Class = view.findViewById(R.id.studentClass_shown);

        Gender_layout=view.findViewById(R.id.gender_layout);
        Birthday_layout=view.findViewById(R.id.birthday_layout);
        phoneNum_layout=view.findViewById(R.id.phoneNum_layout);
        School_layout=view.findViewById(R.id.school_layout);
        Level_layout=view.findViewById(R.id.studentLevel_layout);
        Class_layout=view.findViewById(R.id.studentClass_layout);
        Subject_layout=view.findViewById(R.id.educatorSubject_layout);
        ParentMonitored_layout=view.findViewById(R.id.parentMonitored);
        ConnectionKey_layout=view.findViewById(R.id.studentConnectionKey);
        EditAccount_layout=view.findViewById(R.id.editAccount);
        Logout_layout=view.findViewById(R.id.logout_layout);

        recyclerViewSubject = view.findViewById(R.id.educatorSubject_shown);
        EducatorSubject = new ArrayList<>();
        Educator_Subject = new Educator_Subject(EducatorSubject);
        recyclerViewSubject.setAdapter(Educator_Subject);
        recyclerViewSubject.setLayoutManager(new LinearLayoutManager(getActivity()));


        // info
        School_layout.setVisibility(View.GONE);
        Level_layout.setVisibility(View.GONE);
        Class_layout.setVisibility(View.GONE);
        Subject_layout.setVisibility(View.GONE);
        //button
        ParentMonitored_layout.setVisibility(View.GONE);
        ConnectionKey_layout.setVisibility(View.GONE);


        UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                   Fullname.setText(snapshot.child("fullname").getValue(String.class));
                   String role=snapshot.child("role").getValue(String.class);
                   Role.setText("("+role+")");
                   Gender.setText(snapshot.child("gender").getValue(String.class));
                   Birthday.setText(snapshot.child("birthday").getValue(String.class));
                   PhoneNumber.setText(snapshot.child("phone_number").getValue(String.class));
                   EditAccount_layout.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           Intent intent = new Intent(getActivity(), EditAccount_Activity.class);
                           startActivity(intent);
                       }
                   });
                    checkRole(role);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    Logout_layout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), Starting_Activity.class);
            startActivity(intent);
        }
    });


        return view;
    }

    private void checkRole(String role) {
        if(role.equalsIgnoreCase("student")){
            School_layout.setVisibility(View.VISIBLE);
            Level_layout.setVisibility(View.VISIBLE);
            Class_layout.setVisibility(View.VISIBLE);

            ConnectionKey_layout.setVisibility(View.VISIBLE);
            ConnectionKey_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });

            StudentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    School.setText(snapshot.child("school").getValue(String.class));
                    Level.setText(snapshot.child("level").getValue(String.class));
                    Class.setText(snapshot.child("student_class").getValue(String.class));


                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if(role.equalsIgnoreCase("parent")){
            ParentMonitored_layout.setVisibility(View.VISIBLE);

            ParentMonitored_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent=new Intent(getActivity(),Parent_MonitoredStudents_Activty);
//                    startActivity(intent);
                }
            });
        }else{
            School_layout.setVisibility(View.VISIBLE);
            Subject_layout.setVisibility(View.VISIBLE);


            EduRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    School.setText(snapshot.child("school").getValue(String.class));
                    for (DataSnapshot dataSnapshot : snapshot.child("Subject").getChildren()) {
                        String subjects = dataSnapshot.getValue(String.class);
                        EducatorSubject.add(subjects);
                    }
                    if (Educator_Subject != null) {
                        Educator_Subject.notifyDataSetChanged();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }



}
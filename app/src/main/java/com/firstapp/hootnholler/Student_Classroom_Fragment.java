package com.firstapp.hootnholler;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firstapp.hootnholler.adapter.Student_Classroom_RecyclerViewAdapter;
import com.firstapp.hootnholler.entity.Classroom;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Student_Classroom_Fragment extends Fragment {
    FloatingActionButton joinClass;
    RecyclerView recyclerView;
    ArrayList<Classroom> classroomList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    Student_Classroom_RecyclerViewAdapter recyclerViewAdapter;
    FirebaseAuth auth= FirebaseAuth.getInstance();
    private FirebaseUser currentUser=auth.getCurrentUser();

    String uid=currentUser.getUid().toString();
    DatabaseReference classroomRef, studentClass, student, user, database;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Button JoinClass;
    EditText classCode;
    View close_button;
    String classcode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student__classroom_, container, false);

        joinClass = view.findViewById(R.id.joinClass);
        recyclerView = view.findViewById(R.id.classList);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        recyclerViewAdapter = new Student_Classroom_RecyclerViewAdapter(getActivity(), classroomList);
        recyclerView.setAdapter(recyclerViewAdapter);

        if (currentUser != null) {
            studentClass = FirebaseDatabase.getInstance().getReference("Student").child(uid).child("JoinedClass");
            classroomRef=FirebaseDatabase.getInstance().getReference("Classroom");
            database = FirebaseDatabase.getInstance().getReference();
            // Query the classrooms where classOwner is the UID of the current user
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    classroomList.clear();
                    for (DataSnapshot classCodeSnapshot:snapshot.child("Student").child(uid).child("JoinedClass").getChildren()) {
                        if(classCodeSnapshot.exists()){
                            classcode = classCodeSnapshot.getKey();
                            Classroom classroom = snapshot.child("Classroom").child(classcode).getValue(Classroom.class);
                            classroom.setClassCode(classcode);
                            classroomList.add(classroom);
                            recyclerViewAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        joinClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinClassPopOutWindow();
            }
        });

        return view;
    }

    private void joinClassPopOutWindow() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.pop_up_student_join_class, null);
        builder.setView(dialogView);

        classroomRef = FirebaseDatabase.getInstance().getReference("Classroom");
        student = FirebaseDatabase.getInstance().getReference("Student");
        database = FirebaseDatabase.getInstance().getReference();

        JoinClass = dialogView.findViewById(R.id.JoinClass);
        classCode = dialogView.findViewById(R.id.classCode);
        close_button = dialogView.findViewById(R.id.close);

        AlertDialog dialog = builder.create();
        dialog.show();

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        JoinClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ClassCode = classCode.getText().toString();
                DatabaseReference classCodeReference = classroomRef.child(ClassCode);
                user = FirebaseDatabase.getInstance().getReference("Users");
                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DataSnapshot classCodeSnapshot = snapshot.child("Classroom").child(ClassCode);
                        if (classCodeSnapshot.exists()) {
                            DatabaseReference classroomReference= classroomRef.child(ClassCode).child("StudentsJoined").child(uid);
                            classroomReference.setValue(true);
                            DatabaseReference studentReference = student.child(uid).child("JoinedClass").child(ClassCode);
                            studentReference.setValue(true);
                            String grpChatKeyWifStudent = classCodeSnapshot.child("groupChat").child("student").getValue(String.class);
                            String grpChatKeyWifParent = classCodeSnapshot.child("groupChat").child("parent").getValue(String.class);
                            String connectionKey = snapshot.child("Student").child(uid).child("connection_key").getValue(String.class);
                            if(grpChatKeyWifStudent != null){
                                user.child(uid).child("joinedGrpChatKey").child(grpChatKeyWifStudent).setValue(true);
                            }
                            for (DataSnapshot parentSnapshot : snapshot.child("Parent").getChildren()) {
                                for (DataSnapshot connectionKeySnapshot : parentSnapshot.child("ConnectionKey").getChildren()) {
                                    if(connectionKeySnapshot.getKey().equals(connectionKey)){
                                        String parentUID = parentSnapshot.getKey();
                                        if(grpChatKeyWifParent != null){
                                            user.child(parentUID).child("joinedGrpChatKey").child(grpChatKeyWifParent).setValue(true);
                                        }
                                    }
                                }
                            }
                            Toast.makeText(getActivity(), "Joined class", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getActivity(), "Invalid class code. Please enter a valid class code.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), "Error checking class code. Please try again.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
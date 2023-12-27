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

import com.firstapp.hootnholler.adapter.Classroom_ArrayAdapter;
import com.firstapp.hootnholler.adapter.Classroom_RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Student_Classroom_Fragment extends Fragment {
    FloatingActionButton joinClass;
    RecyclerView recyclerView;
    ArrayList<Classroom_ArrayAdapter> arrayList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    Classroom_RecyclerViewAdapter recyclerViewAdapter;
    DatabaseReference classroom, student;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String uid = mAuth.getUid().toString();
    EditText classCode;
    Button JoinClass;
    View close_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student__classroom_, container, false);

        joinClass = view.findViewById(R.id.joinClass);
        recyclerView = view.findViewById(R.id.classList);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        arrayList.add(new Classroom_ArrayAdapter("Title", "Session", "Day & Time", "Educator Name"));
        recyclerViewAdapter = new Classroom_RecyclerViewAdapter(getActivity(), arrayList);
        recyclerView.setAdapter(recyclerViewAdapter);

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

        classroom = FirebaseDatabase.getInstance().getReference("Classroom");
        student = FirebaseDatabase.getInstance().getReference("Student");

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
                DatabaseReference classCodeReference = classroom.child(ClassCode);
                classCodeReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            DatabaseReference classroomReference=classroom.child(ClassCode).child("StudentsJoined").child(uid);
                            classroomReference.setValue(true);
                            DatabaseReference studentReference=student.child(uid).child("JoinedClass").child(ClassCode);
                            studentReference.setValue(true);
                            Toast.makeText(getActivity(), "Joined cLass", Toast.LENGTH_SHORT).show();
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
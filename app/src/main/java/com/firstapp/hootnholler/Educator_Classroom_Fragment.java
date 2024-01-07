package com.firstapp.hootnholler;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firstapp.hootnholler.adapter.Classroom_RecyclerViewAdapter;
import com.firstapp.hootnholler.entity.Classroom;
import com.firstapp.hootnholler.entity.Conversation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Educator_Classroom_Fragment extends Fragment {
    FloatingActionButton createClass;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 5;
    DatabaseReference classroom;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth auth= FirebaseAuth.getInstance();
    private FirebaseUser currentUser=auth.getCurrentUser();
    String uid=currentUser.getUid().toString();
    EditText addClassName,addClassSession,addClassDescription;
    View close_button;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private List<Classroom>  classroomList = new ArrayList<>(); // Your list of classrooms
    private Classroom_RecyclerViewAdapter recyclerViewAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_educator__classroom_, container, false);
        createClass=view.findViewById(R.id.createClass);
        recyclerView=view.findViewById(R.id.classList);
        layoutManager=new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);

        List<String> colorCodes = Arrays.asList("#B3F4DD", "#F4E2B3", "#F4B3EA", "#FDCFD2", "#D8F4B3", "#EBF4B3", "#EECFFD", "#B3F4F4", "#C0B3F4", "#A0C1FF");
        recyclerViewAdapter = new Classroom_RecyclerViewAdapter(getActivity(), classroomList,colorCodes,uid);
        recyclerView.setAdapter(recyclerViewAdapter);

        if (currentUser != null) {
            classroom = FirebaseDatabase.getInstance().getReference("Classroom");

            // Query the classrooms where classOwner is the UID of the current user
            classroom.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            classroomList.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (snapshot.child("classOwner").getValue(String.class).equals(uid)) {

                                    Classroom classroom = snapshot.getValue(Classroom.class);
                                    classroom.classCode= snapshot.getKey();

                                    if (classroom != null) {
                                        classroomList.add(classroom);
                                    }

                                }
                            }
                            recyclerViewAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle error
                        }
                    });
        }

        createClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateClassDialog();
            }
        });

                return view;
    }

    private void showCreateClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.pop_out_create_class, null);
        builder.setView(dialogView);

         addClassName = dialogView.findViewById(R.id.className);
         addClassSession = dialogView.findViewById(R.id.classSession);
         addClassDescription = dialogView.findViewById(R.id.classDescription);
         close_button=dialogView.findViewById(R.id.close);
        Button dialogCreateClass = dialogView.findViewById(R.id.createClass);

        AlertDialog dialog = builder.create();
        dialog.show();

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialogCreateClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ClassName = addClassName.getText().toString();
                String ClassSession = addClassSession.getText().toString();
                String ClassDescription = addClassDescription.getText().toString();

                if (ClassName.isEmpty() || ClassSession.isEmpty()) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Error")
                            .setMessage("Class title and session are required")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                } else {

                    // Generate class code
                    String classCode = generateRandomClassCode();

                    // automatically create grp chat with student and parents
                    DatabaseReference grpChatWifStudentDatabaseRef = database.child("Conversation").child("GroupChat").push();
                    grpChatWifStudentDatabaseRef.child("Name").setValue(ClassName + " [Collaborative Classroom]");
                    String grpChatWifStudentKey = grpChatWifStudentDatabaseRef.getKey();

                    DatabaseReference grpChatWifParentDatabaseRef = database.child("Conversation").child("GroupChat").push();
                    grpChatWifParentDatabaseRef.child("Name").setValue(ClassName + " [Parent-Teacher Forum]");
                    String grpChatWifParentKey = grpChatWifParentDatabaseRef.getKey();

                    // Use class code as the key to save data in the Firebase Realtime Database
                    DatabaseReference classReference = classroom.child(classCode);
                    classReference.child("className").setValue(ClassName);
                    classReference.child("classSession").setValue(ClassSession);
                    classReference.child("classDescription").setValue(ClassDescription);
                    classReference.child("classOwner").setValue(uid);
                    classReference.child("groupChat").child("student").setValue(grpChatWifStudentKey);
                    classReference.child("groupChat").child("parent").setValue(grpChatWifParentKey);
                    Classroom classroom1 = new Classroom();
                    classroom1.setClassCode(classCode);
                    classroom1.setClassName(ClassName);
                    classroom1.setClassDescription(ClassDescription);
                    classroom1.setClassOwner(uid);
                    classroom1.setClassSession(ClassSession);
                    classroomList.add(classroom1);
                    recyclerViewAdapter.notifyItemInserted(classroomList.size());

                    database.child("Users").child(uid).child("joinedGrpChatKey").child(grpChatWifStudentKey).setValue(true);
                    database.child("Users").child(uid).child("joinedGrpChatKey").child(grpChatWifParentKey).setValue(true);

                    dialog.dismiss();
                }
            }
        });
    }
    public static String generateRandomClassCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    }

package com.firstapp.hootnholler;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class Educator_Classroom_Fragment extends Fragment {
    FloatingActionButton createClass;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 5;
    DatabaseReference classroom;
    FirebaseAuth auth= FirebaseAuth.getInstance();
    String uid=auth.getUid().toString();
    EditText className,classSession,classDescription;
    View close_button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_educator__classroom_, container, false);

        createClass=view.findViewById(R.id.createClass);

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

        classroom= FirebaseDatabase.getInstance().getReference("Classroom");

         className = dialogView.findViewById(R.id.className);
         classSession = dialogView.findViewById(R.id.classSession);
         classDescription = dialogView.findViewById(R.id.classDescription);
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
                String ClassName = className.getText().toString();
                String ClassSession = classSession.getText().toString();
                String ClassDescription = classDescription.getText().toString();

                // Generate class code
                String classCode = generateRandomClassCode();

                // Use class code as the key to save data in the Firebase Realtime Database
                DatabaseReference classReference = classroom.child(classCode);
                classReference.child("ClassName").setValue(ClassName);
                classReference.child("ClassSession").setValue(ClassSession);
                classReference.child("ClassDescription").setValue(ClassDescription);
                classReference.child("ClassOwner").setValue(uid);


                dialog.dismiss();
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

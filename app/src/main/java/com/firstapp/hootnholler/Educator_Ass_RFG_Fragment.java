package com.firstapp.hootnholler;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firstapp.hootnholler.adapter.Asgm_ArrayAdapter;
import com.firstapp.hootnholler.entity.Assignment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//public class Teacher_Ass_RFG_Fragment extends Fragment {
//    Asgm_ArrayAdapter Adapter;
//    ArrayList<Assignment> asgmList= new ArrayList<>();;
//
//    //    private ArrayList<String> StudentList = new ArrayList<>();
//    RecyclerView recyclerView;
//    String currentClassCode;
//    DatabaseReference database;
//    TextView noAss;
//    FirebaseAuth mauth = FirebaseAuth.getInstance();
//    String uid = mauth.getUid().toString();
//
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_teacher__ass__r_f_g_, container, false);
//        Bundle arguments = getArguments();
//        if (arguments != null) {
//            currentClassCode = arguments.getString("classCode");
//        }
//
//        recyclerView = view.findViewById(R.id.rfgAssList);
//        noAss = view.findViewById(R.id.TVnoAss);
//        database = FirebaseDatabase.getInstance().getReference();
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        Adapter = new Asgm_ArrayAdapter(getActivity(), asgmList, currentClassCode);
//        recyclerView.setAdapter(Adapter);
//
//        DatabaseReference classroomRef = FirebaseDatabase.getInstance().getReference("Classroom").child(currentClassCode);
//        DatabaseReference User = FirebaseDatabase.getInstance().getReference("Users");

//        classroomRef.child("StudentsJoined").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // Clear the list before adding new data
//                StudentList.clear();
//
//                for (DataSnapshot studentSnapshot : snapshot.getChildren()) {
//                    String studentKey = studentSnapshot.getKey();
//                    StudentList.add(studentKey);
//                }
//
//                // Notify the adapter that the data set has changed
//                Adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle database error
//            }
//        });

//public class Teacher_Ass_RFG_Fragment extends Fragment {
//    Asgm_ArrayAdapter Adapter;
//    ArrayList<Assignment> asgmList = new ArrayList<>();
//    RecyclerView recyclerView;
//    String currentClassCode;
//    DatabaseReference database;
//    TextView noAss;
//    FirebaseAuth mauth = FirebaseAuth.getInstance();
//    String uid = mauth.getUid().toString();
//
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_teacher__ass__r_f_g_, container, false);
//        Bundle arguments = getArguments();
//        if (arguments != null) {
//            currentClassCode = arguments.getString("classCode");
//        }
//
//        recyclerView = view.findViewById(R.id.rfgAssList);
//        noAss = view.findViewById(R.id.TVnoAss);
//        database = FirebaseDatabase.getInstance().getReference();
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        Adapter = new Asgm_ArrayAdapter(getActivity(), asgmList, currentClassCode);
//        recyclerView.setAdapter(Adapter);
//
//        fetchAssignmentsForGrading();
//
//        return view;
//    }
//
//    private void fetchAssignmentsForGrading() {
//        DatabaseReference assignmentsRef = database.child("Classroom")
//                .child(currentClassCode)
//                .child("Assignment");
//
//        assignmentsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                asgmList.clear();
//
//                for (DataSnapshot assignmentSnapshot : snapshot.getChildren()) {
//                    Assignment assignment = assignmentSnapshot.getValue(Assignment.class);
//
//                    if (assignment != null) {
//                        // Check if submission exists under the assignment
//                        if (assignmentSnapshot.child("Submission").exists()) {
//                            boolean readyForGrading = true;
//
//                            // Check if each student's submission has a "score" key
//                            for (DataSnapshot submissionSnapshot : assignmentSnapshot.child("Submission").getChildren()) {
//                                if (!submissionSnapshot.child("score").exists()) {
//                                    // If any student's submission doesn't have a "score" key, it's not ready for grading
//                                    readyForGrading = false;
//                                    break;
//                                }
//                            }
//
//                            if (readyForGrading) {
//                                asgmList.add(assignment);
//                            }
//                        }
//                    }
//                }
//
//                if (asgmList.isEmpty()) {
//                    noAss.setVisibility(View.VISIBLE);
//                } else {
//                    noAss.setVisibility(View.GONE);
//                }
//
//                Adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle onCancelled
//            }
//        });
//    }
//package com.firstapp.hootnholler;


public class Educator_Ass_RFG_Fragment extends Fragment {
    Asgm_ArrayAdapter adapter;
    ArrayList<Assignment> asgmList = new ArrayList<>();
    RecyclerView recyclerView;
    String currentClassCode;
    DatabaseReference database;
    TextView noAss;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String uid = auth.getUid();
    final String key = "2";
    long numOfStudent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher__ass__r_f_g_, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            currentClassCode = arguments.getString("classCode");
        }

        recyclerView = view.findViewById(R.id.rfgAssList);
        noAss = view.findViewById(R.id.TVnoAss);
        database = FirebaseDatabase.getInstance().getReference();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new Asgm_ArrayAdapter(getActivity(), asgmList, currentClassCode, key);
        recyclerView.setAdapter(adapter);

        database.child("Classroom").child(currentClassCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                numOfStudent = snapshot.child("StudentsJoined").getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.child("Classroom").child(currentClassCode).child("Assignment").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot assignmentSnapshot : dataSnapshot.getChildren()) {
                    Assignment assignment = assignmentSnapshot.getValue(Assignment.class);
                    assignment.setAssKey(assignmentSnapshot.getKey());

                    // Check if the assignment is ready for grading
                    if (isReadyForGrading(assignmentSnapshot)) {
                        asgmList.add(assignment);
                    }
                }

                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled
            }
        });

        return view;
    }

    private boolean isReadyForGrading(DataSnapshot dataSnapshot) {
        DataSnapshot submissionSnapshot = dataSnapshot.child("Submission");

        Assignment assignment = dataSnapshot.getValue(Assignment.class);
        if (assignment != null) {
            long currentTimeMillis = System.currentTimeMillis();
            long dueTimeMillis = Long.parseLong(assignment.getDueDate());

            if (dueTimeMillis <= currentTimeMillis) {
                return false; // Assignment is already due, not ready for grading
            }

            if (submissionSnapshot.exists()) {
                int submittedStudentsCount = (int) submissionSnapshot.getChildrenCount();
                int joinedStudentsCount = (int) numOfStudent; // Using the previously obtained count

                if (submittedStudentsCount > 0 && submittedStudentsCount == joinedStudentsCount) {
                    for (DataSnapshot studentSnapshot : submissionSnapshot.getChildren()) {
                        boolean isScored = studentSnapshot.child("score").exists();

                        if (!isScored) {
                            // If at least one student has not been scored, the assignment is ready for grading
                            return true;
                        }
                    }
                } else {
                    // If not all students have submitted, the assignment is ready for grading
                    return true;
                }
            }

            // No student without a "score" key found, not ready for grading

        }return false;
    }


        private void updateUI() {
            if (asgmList.isEmpty()) {
                noAss.setVisibility(View.VISIBLE);
            } else {
                noAss.setVisibility(View.GONE);
            }

            adapter.notifyDataSetChanged();
        }

}








package com.firstapp.hootnholler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.adapter.Student_Ass_Adapter;
import com.firstapp.hootnholler.entity.Assignment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//public class Student_OngoingAss_Fragment extends Fragment {
//    Student_Ass_Adapter Student_Ass_Adapter;
//    ArrayList<Assignment> asgmList;
//    RecyclerView recyclerView;
//    String currentClassCode;
//    DatabaseReference database;
//    TextView noAss;
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_student__ongoing_ass_, container, false);
//        Bundle arguments = getArguments();
//        if (arguments != null) {
//            currentClassCode = arguments.getString("classCode");
//        }
//
//        recyclerView = view.findViewById(R.id.ongoingAssList);
//        noAss = view.findViewById(R.id.TVnoAss);
//
//        database = FirebaseDatabase.getInstance().getReference();
//
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // Use getActivity() instead of this
//
//        asgmList = new ArrayList<>();
//        Student_Ass_Adapter = new Student_Ass_Adapter(getActivity(), asgmList,currentClassCode); // Use getActivity() instead of this
//        recyclerView.setAdapter(Student_Ass_Adapter);
//        database.child("Classroom").child(currentClassCode).child("Assignment").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Assignment asgm = dataSnapshot.getValue(Assignment.class);
//                    asgm.setAssKey(dataSnapshot.getKey());
//                    if (asgm != null) {
//                        asgmList.add(asgm);
//                    }
//                }
//                if (asgmList.isEmpty()) {
//                    noAss.setVisibility(View.VISIBLE);
//                } else {
//                    noAss.setVisibility(View.GONE);
//                }
//
//                Student_Ass_Adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        return view;
//    }
public class Student_OngoingAss_Fragment extends Fragment {
    Student_Ass_Adapter studentAssAdapter;
    ArrayList<Assignment> asgmList;
    RecyclerView recyclerView;
    String currentClassCode;
    DatabaseReference database;
    TextView noAss;
    FirebaseAuth mauth=FirebaseAuth.getInstance();
    String uid= mauth.getUid().toString();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student__ongoing_ass_, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            currentClassCode = arguments.getString("classCode");
        }

        recyclerView = view.findViewById(R.id.ongoingAssList);
        noAss = view.findViewById(R.id.TVnoAss);

        database = FirebaseDatabase.getInstance().getReference();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        asgmList = new ArrayList<>();
        studentAssAdapter = new Student_Ass_Adapter(getActivity(), asgmList, currentClassCode,uid);
        recyclerView.setAdapter(studentAssAdapter);

        database.child("Classroom").child(currentClassCode).child("Assignment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                asgmList.clear(); // Clear the list before adding assignments

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Assignment asgm = dataSnapshot.getValue(Assignment.class);
                    asgm.setAssKey(dataSnapshot.getKey());

                    if (asgm != null) {
                        long currentTimeMillis = System.currentTimeMillis();
                        long dueTimeMillis = Long.parseLong(asgm.getDueDate());

                        // Check if the assignment is ongoing based on the due date
                        if (dueTimeMillis > currentTimeMillis) {
                            // Check if the student has not submitted the assignment
                            if (!dataSnapshot.child("Submission").hasChild(uid)) {
                                asgmList.add(asgm);
                            }
                        }
                    }
                }
                // Sort the list by due date in ascending order
                Collections.sort(asgmList, new Comparator<Assignment>() {
                    @Override
                    public int compare(Assignment a1, Assignment a2) {
                        long dueDateA = Long.parseLong(a1.getDueDate());
                        long dueDateB = Long.parseLong(a2.getDueDate());
                        return Long.compare(dueDateA, dueDateB);
                    }
                });



                if (asgmList.isEmpty()) {
                    noAss.setVisibility(View.VISIBLE);
                } else {
                    noAss.setVisibility(View.GONE);
                }

                studentAssAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });

        return view;
    }
}

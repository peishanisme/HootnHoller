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
import java.util.Collections;
import java.util.Comparator;


public class Teacher_Ass_Upcoming_Fragment extends Fragment {

    Asgm_ArrayAdapter AssAdapter;
    ArrayList<Assignment> asgmList;
    RecyclerView recyclerView;
    String currentClassCode;
    DatabaseReference database;
    TextView noAss;
    FirebaseAuth mauth=FirebaseAuth.getInstance();
    String uid= mauth.getUid().toString();
    final String key="1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher__ass__upcoming_, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            currentClassCode = arguments.getString("classCode");
        }

        recyclerView = view.findViewById(R.id.upcomingAssList);
        noAss = view.findViewById(R.id.TVnoAss);

        database = FirebaseDatabase.getInstance().getReference();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        asgmList = new ArrayList<>();
        AssAdapter = new Asgm_ArrayAdapter(getActivity(), asgmList, currentClassCode,key);
        recyclerView.setAdapter(AssAdapter);

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
                            if (!dataSnapshot.child("Submission").hasChildren()) {
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

                AssAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });

        return view;
    }
}
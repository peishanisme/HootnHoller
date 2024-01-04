package com.firstapp.hootnholler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firstapp.hootnholler.adapter.ContactItemAdapter;
import com.firstapp.hootnholler.models.ContactItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class SearchContact extends Fragment {
    private RecyclerView recyclerView;
    private ContactItemAdapter contactAdapter;
    private List<ContactItem> contactList;
    private FirebaseAuth auth;
    private DatabaseReference classroomReference, studentReference, parentReference;
    private FirebaseUser currentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        classroomReference = FirebaseDatabase.getInstance().getReference("Classroom");
        studentReference = FirebaseDatabase.getInstance().getReference("Student");
        parentReference = FirebaseDatabase.getInstance().getReference("Parent");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search_contact, container, false);
        recyclerView = view.findViewById(R.id.contactRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        contactList = new ArrayList<>();
        contactAdapter = new ContactItemAdapter(getActivity(), contactList);
        recyclerView.setAdapter(contactAdapter);
        fetchContacts();
        return view;
    }

    private void fetchContacts() {
        // Fetch connection key of the current parent
        parentReference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot parentSnapshot) {
                if (parentSnapshot.exists()) {
                    String connectionKey = parentSnapshot.child("connectionKey").getValue(String.class);

                    // Fetch student class based on connection key
                    studentReference.orderByChild("connection_key").equalTo(connectionKey)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot studentSnapshot) {
                                    if (studentSnapshot.exists()) {
                                        for (DataSnapshot studentData : studentSnapshot.getChildren()) {
                                            String studentClass = studentData.child("student_class").getValue(String.class);

                                            // Fetch class owner based on student class
                                            classroomReference.orderByChild("classOwner").equalTo(studentClass)
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot classroomSnapshot) {
                                                            if (classroomSnapshot.exists()) {
                                                                for (DataSnapshot classData : classroomSnapshot.getChildren()) {
                                                                    // Assuming you have a method to convert classData to ContactItem
                                                                    ContactItem contactItem = convertClassDataToContactItem(classData);
                                                                    contactList.add(contactItem);
                                                                }
                                                                contactAdapter.notifyDataSetChanged();
                                                            }
                                                        }


                                                        @Override
                                                        public void onCancelled(DatabaseError error) {
                                                            // Handle error
                                                        }
                                                    });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Handle error
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });
    }

    // Method to convert DataSnapshot from Classroom to ContactItem
    private ContactItem convertClassDataToContactItem(DataSnapshot classData) {
        // Extract relevant data from classData and create a ContactItem
        // Example:
        int profileImage = R.drawable.profile_circle_svgrepo_com;
        String fullName = classData.child("classOwnerFullName").getValue(String.class);
        String role = classData.child("classOwnerRole").getValue(String.class);

        return new ContactItem(profileImage, fullName, role);
    }
}

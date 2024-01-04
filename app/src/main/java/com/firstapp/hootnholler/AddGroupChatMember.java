package com.firstapp.hootnholler;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.adapter.GroupContactAdapter;
import com.firstapp.hootnholler.models.ContactItem;
import com.firstapp.hootnholler.models.ContactItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddGroupChatMember extends AppCompatActivity {

    private EditText searchView;
    private TextView newGroupTitle, classNameTitle;
    private Button selectedGroupButt, proceedButton;
    private RecyclerView groupContactRecyclerView;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String selectedClassName;
    private Button addContactButton;

    private ArrayList<ContactItem> contactList;
    private GroupContactAdapter groupContactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_chat_member);

        searchView = findViewById(R.id.searchView);
        newGroupTitle = findViewById(R.id.newgrouptitle);
        classNameTitle = findViewById(R.id.classNameTitle);
        selectedGroupButt = findViewById(R.id.selectedGroupButt);
        proceedButton = findViewById(R.id.proceedbutton);
        groupContactRecyclerView = findViewById(R.id.groupContactRecyclerView);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        contactList = new ArrayList<>();
        groupContactAdapter = new GroupContactAdapter(AddGroupChatMember.this, contactList);


        groupContactRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        groupContactRecyclerView.setAdapter(groupContactAdapter);

        // Fetch class name from Firebase and display
        fetchClassName();

        Button selectedGroupButt = findViewById(R.id.selectedGroupButt);
        selectedGroupButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click event for selectedGroupButt
                //then need to deselect the group of ppl from grouplist
                selectedGroupButt.setVisibility(View.INVISIBLE);
            }
        });

        CardView proceedButton = findViewById(R.id.proceedbutton);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click event, navigate to NewGroupChatDetails activity
                Intent intent = new Intent(AddGroupChatMember.this, NewGroupChatDetails.class);
                startActivity(intent);
            }
        });

        // Set up search functionality
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchContact(newText);
                return false;
            }
        });
    }

    private void fetchClassName() {
        String currentUserId = firebaseAuth.getUid();
        if (currentUserId != null) {
            DatabaseReference userRef = databaseReference.child("users").child(currentUserId);
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        selectedClassName = snapshot.child("className").getValue(String.class);
                        classNameTitle.setText(selectedClassName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle errors
                }
            });
        }
    }

    private void searchContact(String query) {
        // Clear the previous list
        contactList.clear();

        // Fetch contacts from Firebase based on the search query
        DatabaseReference contactsRef = databaseReference.child("contacts");
        contactsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot contactSnapshot : snapshot.getChildren()) {
                    ContactItem contact = contactSnapshot.getValue(ContactItem.class);
                    if (contact != null) {
                        // Check if the contact matches the search query
                        if (TextUtils.isEmpty(query) || contact.getUsername().toLowerCase().contains(query.toLowerCase())) {
                            contactList.add(contact);
                        }
                    }
                }
                groupContactAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }
}

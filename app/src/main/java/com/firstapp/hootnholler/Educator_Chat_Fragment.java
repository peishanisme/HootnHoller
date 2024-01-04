package com.firstapp.hootnholler;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.adapter.Chat_UserAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Educator_Chat_Fragment extends Fragment {

    private RecyclerView mainChatRecyclerView;
    private Button searchContactButton;
    private Button newGroupButton;
    private Chat_UserAdapter userAdapter;
    private DatabaseReference usersRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_educator__chat_, container, false);

        // Initialize RecyclerView and user list
        mainChatRecyclerView = view.findViewById(R.id.mainChatRecyclerView);
        mainChatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<ChatUser> userList = new ArrayList<>();
        userAdapter = new Chat_UserAdapter(userList);
        mainChatRecyclerView.setAdapter(userAdapter);

        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        usersRef = firebaseDatabase.getReference("users");

        // Retrieve user data from Firebase
        retrieveUserData();

        // Initialize the button and set click listener
        searchContactButton = view.findViewById(R.id.search_contact_butt);
        searchContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the SearchContact activity when the button is clicked
                Log.d("SearchContact", "Search button clicked!");
                Intent intent = new Intent(getActivity(), SearchContact.class);
                startActivity(intent);
            }
        });

        // Handle item click events if need
        userAdapter.setOnItemClickListener(position -> {
            // Handle item click
            Toast.makeText(getActivity(), "Item clicked at position " + position, Toast.LENGTH_SHORT).show();
        });

        newGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddGroupChatMember.class);
                startActivity(intent);
            }
        });

        setHasOptionsMenu(true);

        return view;
    }

    private void retrieveUserData() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ChatUser> userList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatUser chatUser = snapshot.getValue(ChatUser.class);
                    if (chatUser != null) {
                        userList.add(chatUser);
                    }
                }
                userAdapter.updateUsers(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(getActivity(), "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.student_bottom_nav_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) requireActivity().getSystemService(getActivity().SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.searchView).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().getComponentName()));

        // Set the query listener for search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query submission if needed
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the user list based on the search query
                userAdapter.filterList(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.searchView) {
            // Open the activity_search_contact when the search icon is clicked
            Intent intent = new Intent(getActivity(), SearchContact.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

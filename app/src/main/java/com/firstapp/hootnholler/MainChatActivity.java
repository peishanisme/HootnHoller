package com.firstapp.hootnholler;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class MainChatActivity extends AppCompatActivity {

    private LinearLayout educatorLayout;
    private LinearLayout parentLayout;
    private LinearLayout studentLayout;

    private RecyclerView mainChatRecyclerView;
    private SearchView searchView;
    private Chat_UserAdapter userAdapter;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_parent__chat_);

//        educatorLayout = findViewById(R.id.EducatorLayout);
//        parentLayout = findViewById(R.id.ParentLayout);
//        studentLayout = findViewById(R.id.StudentLayout);
//
//        educatorLayout.setVisibility(View.GONE);
//        parentLayout.setVisibility(View.GONE);
//        studentLayout.setVisibility(View.GONE);

        String roleType = "educator"; // Replace this with the actual role type

        if (roleType.equalsIgnoreCase("educator")) {
            educatorLayout.setVisibility(View.VISIBLE);
        } else if (roleType.equalsIgnoreCase("parent")) {
            parentLayout.setVisibility(View.VISIBLE);
        } else if (roleType.equalsIgnoreCase("student")) {
            studentLayout.setVisibility(View.VISIBLE);
        }

        // Initialize RecyclerView and user list
        mainChatRecyclerView = findViewById(R.id.mainChatRecyclerView);
        mainChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<ChatUser> userList = new ArrayList<>();
        userAdapter = new Chat_UserAdapter(userList);
        mainChatRecyclerView.setAdapter(userAdapter);

        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        usersRef = firebaseDatabase.getReference("users");

        // Retrieve user data from Firebase
        retrieveUserData();

        // Handle item click events if needed
        userAdapter.setOnItemClickListener(new Chat_UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle item click
                Toast.makeText(MainChatActivity.this, "Item clicked at position " + position, Toast.LENGTH_SHORT).show();
            }
        });
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
                Toast.makeText(MainChatActivity.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.parent_bottom_nav_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.searchView).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.searchView) {
            // Open the activity_search_contact when the search icon is clicked
            Intent intent = new Intent(this, SearchContact.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.firstapp.hootnholler;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firstapp.hootnholler.adapter.Conversation_ArrayAdapter;
import com.firstapp.hootnholler.entity.Conversation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Y_ChatFragment extends Fragment {

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private RecyclerView conversationList;
    private Conversation_ArrayAdapter adapter;
    private ArrayList<Conversation> conversations;
    private String userUID;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public Y_ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        conversationList = view.findViewById(R.id.conversationlist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        conversationList.setLayoutManager(layoutManager);
        conversations = new ArrayList<>();
        adapter = new Conversation_ArrayAdapter(getActivity(), conversations);
        conversationList.setAdapter(adapter);
        userUID = mAuth.getUid();
        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search submission if needed
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle search text change
                filterConversations(newText);
                return true;
            }
        });
        getConversation();
        return view;
    }

    private void filterConversations(String searchText) {
        ArrayList<Conversation> filteredList = new ArrayList<>();
        for (Conversation conversation : conversations) {
            // Add your search logic here, for example, by checking conversation name
            if (conversation.getName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(conversation);
            }
        }
        adapter.updateConversation(filteredList);
        adapter.notifyDataSetChanged();
    }

    public void getConversation(){
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // loop joined chat
                conversations.clear();
                for (DataSnapshot grpChatKeySnapshot : snapshot.child("Users").child(userUID).child("joinedGrpChatKey").getChildren()) {
                    if(grpChatKeySnapshot.getValue(Boolean.class)){
                        String grpChatKey = grpChatKeySnapshot.getKey();
                        if(grpChatKey != null){
                            DataSnapshot conversationSnapshot = snapshot.child("Conversation")
                                    .child("GroupChat").child(grpChatKey);
                            Conversation conversation = conversationSnapshot.getValue(Conversation.class);
                            if(conversation != null){
                                conversation.setConversationID(grpChatKey);
                                conversations.add(conversation);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
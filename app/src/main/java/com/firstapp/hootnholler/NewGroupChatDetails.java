package com.firstapp.hootnholler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.adapter.GroupMemberAdapter;
import com.firstapp.hootnholler.models.GroupMemberModel;

import java.util.ArrayList;

public class NewGroupChatDetails extends AppCompatActivity {

    private EditText groupChatNameEditText;
    private TextView membersNumTextView;
    private RecyclerView selectedGroupContactRecyclerView;
    private GroupMemberAdapter groupMemberAdapter;
    private ArrayList<GroupMemberModel> groupMembersList;
    private Button proceedButtonNewGroupDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group_chat_details);

        groupChatNameEditText = findViewById(R.id.groupchatname);
        membersNumTextView = findViewById(R.id.membersNum);
        selectedGroupContactRecyclerView = findViewById(R.id.selectedGroupContactRecyclerView);

        // Initialize RecyclerView and adapter
        groupMembersList = new ArrayList<>();
        groupMemberAdapter = new GroupMemberAdapter(groupMembersList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        selectedGroupContactRecyclerView.setLayoutManager(layoutManager);
        selectedGroupContactRecyclerView.setAdapter(groupMemberAdapter);

        // Update membersNumTextView based on the number of group members
        updateMembersNum();

        // Example: Adding a member to the list
        GroupMemberModel member = new GroupMemberModel("Member Name");
        groupMembersList.add(member);
        groupMemberAdapter.notifyDataSetChanged();

        proceedButtonNewGroupDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewGroupChatDetails.this, GroupChatActivity.class));
                // mayb pass additional data to GroupChatActivity using Intent.putExtra ?
            }
        });
    }

    private void updateMembersNum() {
        int membersCount = groupMembersList.size();
        membersNumTextView.setText(String.valueOf(membersCount));
    }
}

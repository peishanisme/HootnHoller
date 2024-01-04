package com.firstapp.hootnholler;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.adapter.GroupMessageAdapter;
import com.firstapp.hootnholler.models.GroupMessageModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {
    String groupName;
    String senderUid;
    String receiverUid;
    String receiverName;
    RecyclerView groupMessageRecyclerView;
    ArrayList<GroupMessageModel> groupMessagesArrayList;
    GroupMessageAdapter groupMessageAdapter;
    EditText groupTextMessage;
    ImageView sendGroupMessageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        getSupportActionBar().hide();

        groupName = getIntent().getStringExtra("groupName"); // nid get from firebase
        setTitle(groupName);

        groupMessageRecyclerView = findViewById(R.id.groupchatrecyclerview);
        groupTextMessage = findViewById(R.id.textmsg);
        sendGroupMessageButton = findViewById(R.id.sendbutton);

        senderUid = FirebaseAuth.getInstance().getUid();

        groupMessagesArrayList = new ArrayList<>();
        groupMessageAdapter = new GroupMessageAdapter(GroupChatActivity.this, groupMessagesArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        groupMessageRecyclerView.setLayoutManager(linearLayoutManager);
        groupMessageRecyclerView.setAdapter(groupMessageAdapter);

        DatabaseReference groupMessageReference = FirebaseDatabase.getInstance().getReference()
                .child("groupMessages")
                .child(groupName);

        groupMessageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                groupMessagesArrayList.clear();
                for (com.google.firebase.database.DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GroupMessageModel groupMessageModel = dataSnapshot.getValue(GroupMessageModel.class);
                    groupMessagesArrayList.add(groupMessageModel);
                }
                groupMessageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        sendGroupMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = groupTextMessage.getText().toString();
                if (message.isEmpty()) {
                    Toast.makeText(GroupChatActivity.this, "Enter the message first", Toast.LENGTH_SHORT).show();
                    return;
                }
                groupTextMessage.setText("");
                Date date = new Date();
                GroupMessageModel groupMessageModel = new GroupMessageModel(message, senderUid, receiverUid, receiverName, date.getTime());

                DatabaseReference messagePushRef = groupMessageReference.push();
                String pushId = messagePushRef.getKey();

                if (pushId != null) {
                    groupMessageReference.child(pushId).setValue(groupMessageModel)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // Handle completion
                                }
                            });
                }
            }
        });
    }
}

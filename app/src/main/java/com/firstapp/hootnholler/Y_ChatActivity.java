package com.firstapp.hootnholler;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firstapp.hootnholler.adapter.Chat_ArrayAdapter;
import com.firstapp.hootnholler.databinding.ActivityChatBinding;
import com.firstapp.hootnholler.entity.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Y_ChatActivity extends AppCompatActivity {

    private String conversationKey, uid;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Conversation");
    private RecyclerView chatList;
    private Chat_ArrayAdapter adapter;
    private ArrayList<Message> messages = new ArrayList<>();
    private EditText msg;
    private ImageView sendMsg;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private TextView chatName;
    private ActivityChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        conversationKey = getIntent().getStringExtra("conversationKey");
        adapter = new Chat_ArrayAdapter(messages);
        chatList = findViewById(R.id.chatList);
        chatList.setAdapter(adapter);
        chatList.setLayoutManager(new LinearLayoutManager(this));
        msg = findViewById(R.id.message);
        sendMsg = findViewById(R.id.sendIcon);
        chatName = findViewById(R.id.chatName);
        sendMsg.setVisibility(View.GONE);
        uid = mAuth.getUid();
        getConversationDetails();
        msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkMessage();
            }
        });

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        getMessage();

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getConversationDetails(){
        databaseReference.child("GroupChat").child(conversationKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String conversationName = snapshot.child("name").getValue(String.class);
                chatName.setText(conversationName != null ? conversationName : "Chat");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void sendMessage(){
        long currentTime = System.currentTimeMillis();
        databaseReference.child("GroupChat")
                .child(conversationKey)
                .child("message")
                .child(String.valueOf(currentTime))
                .child("content")
                .setValue(msg.getText().toString());
        databaseReference.child("GroupChat")
                .child(conversationKey)
                .child("message")
                .child(String.valueOf(currentTime))
                .child("senderUID")
                .setValue(uid);
        msg.setText("");
    }

    public void checkMessage(){
        if(msg.getText().toString().isEmpty()){
            sendMsg.setVisibility(View.GONE);
        }
        else{
            sendMsg.setVisibility(View.VISIBLE);
        }
    }

    public void getMessage(){
        databaseReference.child("GroupChat").child(conversationKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot msgSnapshot : snapshot.child("message").getChildren()) {
                    Message message = msgSnapshot.getValue(Message.class);
                    message.setTimestamp(Long.parseLong(msgSnapshot.getKey()));
                    messages.add(message);
                    adapter.notifyItemChanged(adapter.getItemCount() - 1);
                    chatList.smoothScrollToPosition(adapter.getItemCount() - 1);
                    readMessage(message);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readMessage(Message message){
        // if not the user msg
        if(message.getSenderUID() != null){
            if(!message.getSenderUID().equals(uid)){
                Map<String, Boolean> readStatus;
                if(message.getReadStatus() != null){
                    readStatus = message.getReadStatus();
                    readStatus.put(uid, true);
                }
                else{
                    readStatus = new HashMap<>();
                    readStatus.put(uid, true);
                }
                message.setReadStatus(readStatus);
                databaseReference.child("GroupChat").child(conversationKey).child("message").child(String.valueOf(message.getTimestamp())).child("readStatus").setValue(readStatus);
            }
        }
    }
}
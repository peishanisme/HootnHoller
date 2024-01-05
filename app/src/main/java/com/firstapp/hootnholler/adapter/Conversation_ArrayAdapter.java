package com.firstapp.hootnholler.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.app.AppCompatActivity;
import com.firstapp.hootnholler.ChatActivity;
import com.firstapp.hootnholler.R;
import com.firstapp.hootnholler.entity.Conversation;
import com.firstapp.hootnholler.entity.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Conversation_ArrayAdapter extends RecyclerView.Adapter<Conversation_ArrayAdapter.ViewHolder>{

    private ArrayList<Conversation> conversations;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String uid = mAuth.getUid();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private Context context;
    public Conversation_ArrayAdapter(Context context, ArrayList<Conversation> conversations){
        this.conversations = conversations;
        this.context = context;
    }

    @NonNull
    @Override
    public Conversation_ArrayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversationlist_item, parent, false);
        return new Conversation_ArrayAdapter.ViewHolder(view);
    }

    public void updateConversation(ArrayList<Conversation> conversations){
        this.conversations = conversations;
    }

    @Override
    public void onBindViewHolder(@NonNull Conversation_ArrayAdapter.ViewHolder holder, int position) {
        Conversation conversation = conversations.get(position);
        holder.ContactName.setText(conversation.getName());
        int numOfUnread = 0;
        String content = "";
        Message message;
        long timestamp = 0;
        if(conversation.getMessage() != null){
            for(Map.Entry<String, Message> messageEntry : conversation.getMessage().entrySet()) {
                message =  messageEntry.getValue();
                // find last message
                if(timestamp < Long.parseLong(messageEntry.getKey())){
                    timestamp = Long.parseLong(messageEntry.getKey());
                    content = messageEntry.getValue().getContent();
                }
                if(message.getSenderUID() != null){
                    if(!message.getSenderUID().equals(uid)){
                        if(message.getReadStatus() != null){
                            if(!message.getReadStatus().containsKey(uid)||!message.getReadStatus().get(uid)){
                                numOfUnread ++;
                            }
                        }
                        else {
                            numOfUnread ++;
                        }
                    }
                }
            }
            holder.Content.setText(content);
            String interval = getInterval(timestamp);
            holder.LastMsgTime.setText(interval);
            if(numOfUnread != 0){
                holder.UnreadMsgContainer.setVisibility(View.VISIBLE);
                holder.NumOfUnread.setText(String.valueOf(numOfUnread));
            }
            else{
                holder.UnreadMsgContainer.setVisibility(View.INVISIBLE);
            }
        }
        holder.ConversationBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("conversationKey", conversation.getConversationID());
                context.startActivity(intent);
            }
        });
    }

    public String getInterval(long lastMsgTime){
        long currentTime = System.currentTimeMillis();
        long duration = Math.abs(currentTime - lastMsgTime);
        if(duration < TimeUnit.MINUTES.toMillis(1)){
            return "just now";
        }
        if(duration < TimeUnit.HOURS.toMillis(1)){
            return TimeUnit.MILLISECONDS.toMinutes(duration) + "min";
        }
        if(duration < TimeUnit.DAYS.toMillis(1)){
            return TimeUnit.MILLISECONDS.toHours(duration) + "hr";
        }
        if (duration < TimeUnit.DAYS.toMillis(7)) {
            return getDayOfWeek(duration);
        }
        return sdf.format(new Date(lastMsgTime));
    }

    private static String getDayOfWeek(long timeInMillis) {
        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

        int dayIndex = (int) ((timeInMillis / TimeUnit.DAYS.toMillis(1) + 4) % 7); // Adding 4 to start from Monday
        return daysOfWeek[dayIndex];
    }

    @Override
    public int getItemCount() {
        return this.conversations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ContactName, Content, NumOfUnread, LastMsgTime;
        private LinearLayout ConversationBackground, UnreadMsgContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ContactName = itemView.findViewById(R.id.contactName);
            Content = itemView.findViewById(R.id.content);
            NumOfUnread = itemView.findViewById(R.id.num_of_unread);
            LastMsgTime = itemView.findViewById(R.id.last_message_timestamp);
            ConversationBackground = itemView.findViewById(R.id.conversationBackground);
            UnreadMsgContainer = itemView.findViewById(R.id.unreadMsgContainer);
        }
    }
}

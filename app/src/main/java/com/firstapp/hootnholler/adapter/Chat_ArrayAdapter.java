package com.firstapp.hootnholler.adapter;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.R;
import com.firstapp.hootnholler.entity.Message;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Chat_ArrayAdapter extends RecyclerView.Adapter<Chat_ArrayAdapter.ViewHolder> {
    private ArrayList<Message> messages;
    private DatabaseReference users = FirebaseDatabase.getInstance().getReference("Users");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String uid = mAuth.getUid();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    // constructor
    public Chat_ArrayAdapter(ArrayList<Message> messages){
        this.messages = messages;
    }

    //
    @NonNull
    @Override
    public Chat_ArrayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatlist_item, parent, false);
        return new Chat_ArrayAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Chat_ArrayAdapter.ViewHolder holder, int position) {
        Message message = messages.get(position);
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(message.getSenderUID() != null){
                    if(message.getSenderUID().equals(uid)){
                        holder.SenderName.setText("You");
                        holder.MsgBackground.setBackgroundResource(R.drawable.me_chat);
                        holder.Message.setTextColor(Color.parseColor("#ffffff"));
                        ((LinearLayout.LayoutParams) holder.SendTime.getLayoutParams()).gravity = Gravity.END;
                        ((LinearLayout.LayoutParams) holder.Message.getLayoutParams()).gravity = Gravity.END;
                        ((LinearLayout.LayoutParams) holder.SenderName.getLayoutParams()).gravity = Gravity.END;
                        ((LinearLayout.LayoutParams) holder.MsgBackground.getLayoutParams()).gravity = Gravity.END;
                    }
                    else{
                        holder.SenderName.setText(snapshot.child(message.getSenderUID()).child("fullname").getValue(String.class));
                        holder.MsgBackground.setBackgroundResource(R.drawable.other_chat);
                        holder.Message.setTextColor(Color.parseColor("#000000"));
                        ((LinearLayout.LayoutParams) holder.SendTime.getLayoutParams()).gravity = Gravity.START;
                        ((LinearLayout.LayoutParams) holder.Message.getLayoutParams()).gravity = Gravity.START;
                        ((LinearLayout.LayoutParams) holder.SenderName.getLayoutParams()).gravity = Gravity.START;
                        ((LinearLayout.LayoutParams) holder.MsgBackground.getLayoutParams()).gravity = Gravity.START;
                    }
                }
                holder.SendTime.setText(getInterval(message.getTimestamp()));
                holder.Message.setText(message.getContent());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
            return TimeUnit.MILLISECONDS.toMinutes(duration) + "min ago";
        }
        if(duration < TimeUnit.DAYS.toMillis(1)){
            return TimeUnit.MILLISECONDS.toHours(duration) + "hr ago";
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
        return this.messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView SenderName, Message, SendTime;
        private LinearLayout MsgBackground;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            SenderName = itemView.findViewById(R.id.senderName);
            Message = itemView.findViewById(R.id.msg);
            SendTime = itemView.findViewById(R.id.sendTime);
            MsgBackground = itemView.findViewById(R.id.msgBackground);
        }
    }

}

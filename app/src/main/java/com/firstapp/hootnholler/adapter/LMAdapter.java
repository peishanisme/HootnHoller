package com.firstapp.hootnholler.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.R;
import com.firstapp.hootnholler.Teacher_LM_Details;
import com.firstapp.hootnholler.entity.Announcement;
import com.firstapp.hootnholler.entity.Learning_Materials;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LMAdapter extends RecyclerView.Adapter<LMAdapter.ViewHolder>{

        private List<Learning_Materials> LMList;
        private Context context;
        private String currentClassCode;


        public LMAdapter(){

}
        public LMAdapter(Context context,List<Learning_Materials> LMlist,String CurrentClassCode) {
            this.context = context;
            this.LMList = LMlist;
            this.currentClassCode=CurrentClassCode;
        }

    public LMAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lm_item, parent, false);
        return new LMAdapter.ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Learning_Materials LMItem = LMList.get(position);
        holder.bind(LMItem);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, Teacher_LM_Details.class);
                intent.putExtra("LMid",LMItem.getLMid());
                intent.putExtra("classCode", currentClassCode);
                context.startActivity(intent);
            }
        });
    }


        @Override
        public int getItemCount() {
            return LMList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            private TextView titleTextView;
            private TextView dateTimeTextView;
            private CardView card;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                titleTextView = itemView.findViewById(R.id.lm_title);
                dateTimeTextView = itemView.findViewById(R.id.lm_time);
                card = itemView.findViewById(R.id.card);
            }

            public void bind(Learning_Materials lmItem) {
                titleTextView.setText(lmItem.getTitle());
                // Convert timestamp to human-readable time
                long timestamp=Long.parseLong(lmItem.getTimestamp());
                String formattedDateTime = convertTimestampToDateTime(timestamp);
                dateTimeTextView.setText("Posted on " + formattedDateTime);
                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        }

    public static String convertTimestampToDateTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss a", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

}
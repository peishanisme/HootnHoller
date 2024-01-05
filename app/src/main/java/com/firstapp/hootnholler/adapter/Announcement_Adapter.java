package com.firstapp.hootnholler.adapter;
import android.app.Dialog;
import android.content.Context;
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
import com.firstapp.hootnholler.entity.Announcement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Announcement_Adapter extends RecyclerView.Adapter<Announcement_Adapter.ViewHolder> {

    private List<Announcement> announcementList;
    private Context context;
    private String currentClassCode;

    public Announcement_Adapter(Context context,List<Announcement> announcementList,String CurrentClassCode) {
        this.context = context;
        this.announcementList = announcementList;
        this.currentClassCode=CurrentClassCode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Announcement announcementItem = announcementList.get(position);
        holder.bind(announcementItem);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAnnouncementPopup(view, announcementItem);

            }
        });
    }
    private void showAnnouncementPopup(View card,Announcement announcementItem) {
        Dialog dialog = new Dialog(card.getContext());
        dialog.setContentView(R.layout.pop_out_announcement_details);

        // Set content in the dialog
        TextView annTitle = dialog.findViewById(R.id.annTitle);
        TextView annTime = dialog.findViewById(R.id.annTime);
        TextView annContent = dialog.findViewById(R.id.annContent);
        View closeButton=dialog.findViewById(R.id.close);
        Button editButton = dialog.findViewById(R.id.editButton);
        Button deleteButton = dialog.findViewById(R.id.deleteButton);

        annTitle.setText(announcementItem.getAnnouncementTitle());
        annTime.setText("Posted on " + ViewHolder.convertTimestampToDateTime(announcementItem.getTimestamp()));
        annContent.setText(announcementItem.getAnnouncementContent());

        // Set up click listeners for buttons in the dialog
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit button click
                // You can implement the edit functionality here
                // You may want to dismiss the dialog after handling the click
                dialog.dismiss();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                DatabaseReference announcementRef = FirebaseDatabase.getInstance().getReference("Classroom")
                                                        .child(currentClassCode)
                                                        .child("Announcement")
                                                        .child(String.valueOf(announcementItem.getTimestamp()));

                                                // Remove the announcement from the database
                                                announcementRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            // Delete successful
                                                            Toast.makeText(context, "Announcement deleted", Toast.LENGTH_SHORT).show();
                                                            // Optionally, you may want to notify your RecyclerView of the change
                                                            notifyDataSetChanged();
                                                        } else {
                                                            // Delete failed
                                                            Toast.makeText(context, "Failed to delete announcement", Toast.LENGTH_SHORT).show();
                                                        }
                                                        dialog.dismiss();
                                                    }
                                                });
                                            }
                                        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public int getItemCount() {
        return announcementList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView dateTimeTextView;
        private CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.announcement_title);
            dateTimeTextView = itemView.findViewById(R.id.dateTime);
            card = itemView.findViewById(R.id.announcementView);
        }

        public void bind(Announcement announcementItem) {
            titleTextView.setText(announcementItem.getAnnouncementTitle());
            // Convert timestamp to human-readable time
            String formattedDateTime = convertTimestampToDateTime(announcementItem.getTimestamp());
            dateTimeTextView.setText("Posted on " + formattedDateTime);
        }

        public static String convertTimestampToDateTime(long timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss a", Locale.getDefault());
            return sdf.format(new Date(timestamp));
        }
    }
}



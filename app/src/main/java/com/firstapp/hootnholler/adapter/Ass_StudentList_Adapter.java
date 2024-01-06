package com.firstapp.hootnholler.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.R;
import com.firstapp.hootnholler.Student_AsgmDetails;
import com.firstapp.hootnholler.entity.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Random;

public class Ass_StudentList_Adapter extends RecyclerView.Adapter<Ass_StudentList_Adapter.ViewHolder> {

    private List<String> StudentList;
    private String currentClassCode;
    private String key,assID;
    User user;
    private Context context;
    private DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference("Users");
    private int[] drawableResources = {
            R.drawable.user1,
            R.drawable.user2,
            R.drawable.user3,
            R.drawable.user4,
            R.drawable.user5,
            R.drawable.user6,
            R.drawable.user7,
    };


    public Ass_StudentList_Adapter(Context context, List<String>StudentList, String currentClassCode,String key,String assID) {
        this.context=context;
        this.StudentList=StudentList;
        this.currentClassCode=currentClassCode;
        this.key=key;
        this.assID=assID;
    }

    @NonNull
    @Override
    public Ass_StudentList_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_item, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String studentUID = StudentList.get(position);

        int randomIndex = new Random().nextInt(drawableResources.length);
        int randomDrawableId = drawableResources[randomIndex];
        holder.image.setImageResource(randomDrawableId);

        if(assID != null){
            DatabaseReference submissionRef = FirebaseDatabase.getInstance().getReference("Classroom")
                    .child(currentClassCode).child("Assignment").child(assID).child("Submission").child(studentUID);

            if(key.equals("2")){
                submissionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {;
                        if (snapshot.exists()) {
                            if (snapshot.child("score").exists()){
                                holder.image.setVisibility(View.GONE);
                                holder.condition.setVisibility(View.GONE);
                                holder.peopleLayout.setVisibility(View.GONE);
                                holder.StudentName.setVisibility(View.GONE);
                            }
                            else{
                                // If the student UID exists under submission, set text as "Submitted"
                                holder.condition.setText("Submitted");
                            }
                        }
                        else {
                            // If the student UID does not exist under submission, set text as "No attempt"
                            holder.condition.setText("No attempt");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled
                    }
                });
            }else{
                submissionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("score").exists()){
                            holder.condition.setText(snapshot.child("score").getValue(String.class));
                        }
                        else{
                            holder.peopleLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }

        holder.peopleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Student_AsgmDetails.class);
                intent.putExtra("classCode", currentClassCode);
                intent.putExtra("assID", assID);
                intent.putExtra("studentUID", studentUID);
                context.startActivity(intent);
            }
        });


        UserRef.child(studentUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("fullname").getValue(String.class);
                holder.StudentName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }



    @Override
    public int getItemCount() {
        return StudentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView StudentName,condition;
        private ImageView image;
        private LinearLayout peopleLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            StudentName=itemView.findViewById(R.id.name);
            image=itemView.findViewById(R.id.image);
            condition=itemView.findViewById(R.id.condition);
            peopleLayout=itemView.findViewById(R.id.peopleLayout);
        }
    }
}


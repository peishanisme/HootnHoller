package com.firstapp.hootnholler.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.R;
import com.firstapp.hootnholler.Educator_Class;
import com.firstapp.hootnholler.Student_Class;
import com.firstapp.hootnholler.entity.Classroom;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Random;

public class Classroom_RecyclerViewAdapter extends RecyclerView.Adapter<Classroom_RecyclerViewAdapter.MyViewHolder> {

    private List<Classroom> classroomList;
    private Context context;
    DatabaseReference classroomRef;
    private List<String> colorCodes;
    String uid;


    public Classroom_RecyclerViewAdapter(Context context, List<Classroom> classroomList,List<String> colorCodes,String uid) {
        this.context = context;
        this.classroomList = classroomList;
        this.colorCodes = colorCodes;
        this.uid=uid;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Classroom classroom = classroomList.get(position);
        holder.className.setText(classroom.getClassName());
        holder.classSession.setText(classroom.getClassSession());
        holder.classDescription.setText(classroom.getClassDescription());
        int randomColor = Color.parseColor(getRandomColorCode());
        holder.card.setCardBackgroundColor(randomColor);
        classroomRef= FirebaseDatabase.getInstance().getReference("Classroom").child(classroom.getClassCode());
        classroomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    long studentNum=snapshot.child("StudentsJoined").getChildrenCount();
                    holder.numStudents.setText(String.valueOf(studentNum)+" students");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference user=FirebaseDatabase.getInstance().getReference("Users").child(uid);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String role=snapshot.child("role").getValue(String.class);
                if(role.equalsIgnoreCase("student")){
                    holder.card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, Student_Class.class);
                            intent.putExtra("classCode", classroom.getClassCode());
                            context.startActivity(intent);
                        }
                    });
                }else{
                    holder.card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, Educator_Class.class);
                            intent.putExtra("classCode", classroom.getClassCode());
                            context.startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private String getRandomColorCode() {
        Random random = new Random();
        int index = random.nextInt(colorCodes.size());
        return colorCodes.get(index);
    }

    @Override
    public int getItemCount() {
        return classroomList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView className;
        private TextView classSession;
        private TextView classDescription;
        private TextView numStudents;
        private CardView card;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.ClassTitle);
            classSession = itemView.findViewById(R.id.CLassSession);
            classDescription = itemView.findViewById(R.id.ClassDescription);
            card=itemView.findViewById(R.id.Class);
            numStudents = itemView.findViewById(R.id.numStu);
        }


    }
}

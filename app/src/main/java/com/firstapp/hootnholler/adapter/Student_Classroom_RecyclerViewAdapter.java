package com.firstapp.hootnholler.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.R;
import com.firstapp.hootnholler.Student_Class;
import com.firstapp.hootnholler.entity.Classroom;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Student_Classroom_RecyclerViewAdapter extends RecyclerView.Adapter<Student_Classroom_RecyclerViewAdapter.MyViewHolder> {

    private List<Classroom> classroomList;
    private Context context;
    private DatabaseReference user;

    public Student_Classroom_RecyclerViewAdapter(Context context, List<Classroom> classroomList) {
        this.context = context;
        this.classroomList = classroomList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_class_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Classroom classroom = classroomList.get(position);
        holder.className.setText(classroom.getClassName());
        holder.classSession.setText(classroom.getClassSession());
        holder.classDescription.setText(classroom.getClassDescription());
        String educatorUid=classroom.getClassOwner();
        user=FirebaseDatabase.getInstance().getReference("Users");
        user.child(educatorUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name=snapshot.child("fullname").getValue(String.class);
                    holder.educatorName.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Student_Class.class);
                intent.putExtra("classCode", classroom.getClassCode());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return classroomList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView className;
        private TextView classSession;
        private TextView classDescription;
        private TextView educatorName;
        private CardView card;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.ClassTitle);
            classSession = itemView.findViewById(R.id.CLassSession);
            classDescription = itemView.findViewById(R.id.ClassDescription);
            educatorName=itemView.findViewById(R.id.educatorName);
            card=itemView.findViewById(R.id.Class);
//            numStudents = itemView.findViewById(R.id.numStu);
        }


    }
}

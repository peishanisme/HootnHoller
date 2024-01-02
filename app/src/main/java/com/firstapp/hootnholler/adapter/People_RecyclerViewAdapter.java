package com.firstapp.hootnholler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.R;
import com.firstapp.hootnholler.entity.Student;
import com.firstapp.hootnholler.entity.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Random;

public class People_RecyclerViewAdapter extends RecyclerView.Adapter<People_RecyclerViewAdapter.ViewHolder> {

    private List<String> StudentList;
    private String currentClassCode;
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


    public People_RecyclerViewAdapter(Context context,List<String>StudentList,String currentClassCode) {
        this.context=context;
        this.StudentList=StudentList;
        this.currentClassCode=currentClassCode;
    }

    @NonNull
    @Override
    public People_RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull People_RecyclerViewAdapter.ViewHolder holder, int position) {
        String studentUID = StudentList.get(position);
        int randomIndex = new Random().nextInt(drawableResources.length);
        int randomDrawableId = drawableResources[randomIndex];
        holder.image.setImageResource(randomDrawableId);
        UserRef.child(studentUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name=snapshot.child("fullname").getValue(String.class);
                holder.StudentName.setText(name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

            @Override
            public int getItemCount() {
                return StudentList.size();
            }

            public static class ViewHolder extends RecyclerView.ViewHolder {
                private TextView StudentName;
                private ImageView image;

                public ViewHolder(@NonNull View itemView) {
                    super(itemView);
                    StudentName=itemView.findViewById(R.id.name);
                    image=itemView.findViewById(R.id.image);
                }
            }
        }

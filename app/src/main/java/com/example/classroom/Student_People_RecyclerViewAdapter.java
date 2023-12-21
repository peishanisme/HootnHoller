package com.example.classroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Student_People_RecyclerViewAdapter extends RecyclerView.Adapter<Student_People_RecyclerViewAdapter.Viewholder> {
    Context context;
    ArrayList<Student_People_ArrayAdapter> arrayList = new ArrayList<>();

    public Student_People_RecyclerViewAdapter(Context context, ArrayList<Student_People_ArrayAdapter> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.people_item,parent,false);
        Student_People_RecyclerViewAdapter.Viewholder viewholder = new Student_People_RecyclerViewAdapter.Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Student_People_RecyclerViewAdapter.Viewholder holder, int position) {
        holder.image.setImageResource(arrayList.get(position).image);
        holder.name.setText(arrayList.get(position).name);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
        }
    }
}

package com.firstapp.hootnholler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Announcement_RecyclerViewAdapter extends RecyclerView.Adapter<Announcement_RecyclerViewAdapter.Viewholder> {
    Context context;
    ArrayList<Announcement_ArrayAdapter> arrayList = new ArrayList<>();

    public Announcement_RecyclerViewAdapter(Context context, ArrayList<Announcement_ArrayAdapter> arrayList){
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.announcement_item,parent,false);
        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.title.setText(arrayList.get(position).title);
        holder.dateTime.setText(arrayList.get(position).dateTime);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView title, dateTime;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.announcement_title);
            dateTime = itemView.findViewById(R.id.dateTime);
        }
    }
}

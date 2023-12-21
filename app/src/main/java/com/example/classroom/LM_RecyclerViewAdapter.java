package com.example.classroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LM_RecyclerViewAdapter extends RecyclerView.Adapter<LM_RecyclerViewAdapter.Viewholder> {
    Context context;
    ArrayList<LM_ArrayAdapter> arrayList = new ArrayList<>();

    public LM_RecyclerViewAdapter(Context context, ArrayList<LM_ArrayAdapter> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lm_item,parent,false);
        LM_RecyclerViewAdapter.Viewholder viewholder = new LM_RecyclerViewAdapter.Viewholder(view);
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
            title = itemView.findViewById(R.id.lm_title);
            dateTime = itemView.findViewById(R.id.dateTime);
        }
    }
}

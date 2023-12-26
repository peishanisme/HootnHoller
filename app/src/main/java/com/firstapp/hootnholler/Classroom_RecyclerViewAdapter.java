package com.firstapp.hootnholler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Classroom_RecyclerViewAdapter extends RecyclerView.Adapter <Classroom_RecyclerViewAdapter.ViewHolder>{
    Context context;
    ArrayList<Classroom_ArrayAdapter> arrayList = new ArrayList<>();

    public Classroom_RecyclerViewAdapter(Context context, ArrayList<Classroom_ArrayAdapter> arrayList){
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.class_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(arrayList.get(position).title);
        holder.session.setText(arrayList.get(position).session);
        holder.daytime.setText(arrayList.get(position).daytime);
        holder.educatorName.setText(arrayList.get(position).educatorName);
    }


    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, session, daytime, educatorName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.classroomTitle);
            session = itemView.findViewById(R.id.session);
            daytime = itemView.findViewById(R.id.dayTime);
            educatorName = itemView.findViewById(R.id.educatorName);
        }
    }
}

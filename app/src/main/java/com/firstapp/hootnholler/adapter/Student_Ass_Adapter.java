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
import com.firstapp.hootnholler.Student_AsgmDetails;
import com.firstapp.hootnholler.Teacher_CreateAss;
import com.firstapp.hootnholler.entity.Assignment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Student_Ass_Adapter extends RecyclerView.Adapter<Student_Ass_Adapter.MyViewHolder> {
    Context context;
    ArrayList<Assignment> asgmList;
    String currentClassCode;

    public Student_Ass_Adapter(Context context, ArrayList<Assignment> asgmList,String currentClassCode) {
        this.context = context;
        this.asgmList = asgmList;
        this.currentClassCode=currentClassCode;
    }

    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.asgm_item, parent, false);
        return new MyViewHolder(v);
    }

    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Assignment asgm = asgmList.get(position);
        holder.dueMsg.setVisibility(View.GONE);
        holder.asgmTitle.setText(asgm.getTitle());

        if (asgm.getDueDate() != null) {
            try {
                long dueDateTimestamp = Long.parseLong(asgm.getDueDate());
                holder.asgmDueDate.setText("Due " + convertTimestampToDateTime(dueDateTimestamp));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            holder.asgmDueDate.setText("No Due Date");
        }
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, Student_AsgmDetails.class);
                intent.putExtra("classCode",currentClassCode);
                intent.putExtra("assID",asgm.getAssKey());
                context.startActivity(intent);
            }
        });
    }



    public int getItemCount() {
        return asgmList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView asgmTitle,asgmDueDate,dueMsg;
        CardView card;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            asgmTitle = itemView.findViewById(R.id.asgmTitle);
            asgmDueDate = itemView.findViewById(R.id.dateTime);
            dueMsg=itemView.findViewById(R.id.dueMsg);
            card=itemView.findViewById(R.id.card);
        }
    }
    public static String convertTimestampToDateTime(long timestamp){
        try{
            Date currentDate = (new Date(timestamp));
            SimpleDateFormat sfd = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss", Locale.getDefault());
            return sfd.format(currentDate);
        } catch(Exception e){
            return "date";
        }

    }
}

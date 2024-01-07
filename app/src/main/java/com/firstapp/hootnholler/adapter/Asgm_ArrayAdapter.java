package com.firstapp.hootnholler.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.R;
import com.firstapp.hootnholler.Educator_Ass_StudentList;
import com.firstapp.hootnholler.Educator_CreateAss;
import com.firstapp.hootnholler.Y_AsgmDetails;
import com.firstapp.hootnholler.entity.Assignment;

public class Asgm_ArrayAdapter extends RecyclerView.Adapter <Asgm_ArrayAdapter.MyViewHolder>{
    private ArrayList<Assignment> asgmList;
    private Context context;
    private String currentClassCode;
    private String key;

    public Asgm_ArrayAdapter(Context context, ArrayList<Assignment> asgmList,String currentClassCode,String key) {
        this.context = context;
        this.asgmList = asgmList;
        this.currentClassCode = currentClassCode;
        this.key=key;
    }

    @NonNull
    @Override
    public Asgm_ArrayAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.assignment_item,parent,false);
        return new Asgm_ArrayAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Assignment asgm = asgmList.get(position);
        //holder.bind(asgm);
        //holder.label.setText("• Ready For Grading");
        holder.label.setVisibility(View.GONE);
        holder.asgmTitle.setText(asgm.getTitle());
        // Check if dueDate is not null before parsing
        if (asgm.getDueDate() != null) {
            try {
                long dueDateTimestamp = Long.parseLong(asgm.getDueDate());
                holder.asgmDueDate.setText("Due " + Educator_CreateAss.convertTimestampToDateTime(dueDateTimestamp));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // Handle the exception or log the error
            }
        } else {
            // Handle the case where dueDate is null
            holder.asgmDueDate.setText("No Due Date");
        }

        if(key=="1"){
            holder.card.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Y_AsgmDetails.class);
                    intent.putExtra("classCode", currentClassCode);
                    intent.putExtra("assID", asgm.getAssKey());
                    intent.putExtra("studentUID", "");
                    context.startActivity(intent);
                }
            });
        }else {
            holder.card.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                Intent intent=new Intent(context, Educator_Ass_StudentList.class);
                intent.putExtra("assKey",asgm.getAssKey());
                intent.putExtra("classCode", currentClassCode);
                intent.putExtra("key",key);
                context.startActivity(intent);
        }
        });
        }
    }


    @Override
    public int getItemCount() {
        return asgmList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView label,asgmTitle,asgmOpenDate,asgmDueDate,assDescription;
        private CardView card;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.RFGlabel);
            asgmTitle = itemView.findViewById(R.id.asgmTitle);
            asgmOpenDate = itemView.findViewById(R.id.openDate);
            asgmDueDate = itemView.findViewById(R.id.asgmDueDate);
            assDescription = itemView.findViewById(R.id.description);
            card = itemView.findViewById(R.id.card);
        }


    }
}

package com.firstapp.hootnholler.adapter;

import static com.firstapp.hootnholler.adapter.LMAdapter.convertTimestampToDateTime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.R;
import com.firstapp.hootnholler.Teacher_CreateAss;
import com.firstapp.hootnholler.entity.Assignment;

public class Asgm_ArrayAdapter extends RecyclerView.Adapter <Asgm_ArrayAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<Assignment> asgmList;

    public Asgm_ArrayAdapter(Context context, ArrayList<Assignment> asgmList) {
        this.context = context;
        this.asgmList = asgmList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.assignment_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Assignment asgm = asgmList.get(position);
        holder.label.setText("• Ready For Grading");
        holder.asgmTitle.setText(asgm.getTitle());
        // Check if dueDate is not null before parsing
        if (asgm.getDueDate() != null) {
            try {
                long dueDateTimestamp = Long.parseLong(asgm.getDueDate());
                holder.asgmDueDate.setText("Due " + Teacher_CreateAss.convertTimestampToDateTime(dueDateTimestamp));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // Handle the exception or log the error
            }
        } else {
            // Handle the case where dueDate is null
            holder.asgmDueDate.setText("No Due Date");
        }
    }

    @Override
    public int getItemCount() {
        return asgmList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView label,asgmTitle,asgmDueDate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.RFGlabel);
            asgmTitle = itemView.findViewById(R.id.asgmTitle);
            asgmDueDate = itemView.findViewById(R.id.asgmDueDate);
        }
    }
}

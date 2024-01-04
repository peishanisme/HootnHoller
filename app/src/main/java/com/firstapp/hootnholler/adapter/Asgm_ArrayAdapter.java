package com.firstapp.hootnholler.adapter;

import static com.firstapp.hootnholler.adapter.LMAdapter.convertTimestampToDateTime;

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
import com.firstapp.hootnholler.Teacher_CreateAss;
import com.firstapp.hootnholler.Teacher_EditAss;
import com.firstapp.hootnholler.Teacher_LM_Details;
import com.firstapp.hootnholler.entity.Assignment;

public class Asgm_ArrayAdapter extends RecyclerView.Adapter <Asgm_ArrayAdapter.MyViewHolder>{
    private ArrayList<Assignment> asgmList;
    private Context context;
    private String currentClassCode;

    public Asgm_ArrayAdapter() {
    }

    public Asgm_ArrayAdapter(Context context, ArrayList<Assignment> asgmList,String currentClassCode) {
        this.context = context;
        this.asgmList = asgmList;
        this.currentClassCode = currentClassCode;
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

        holder.card.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Teacher_EditAss.class);
                intent.putExtra("assKey",asgm.getAssKey());
                intent.putExtra("classCode", currentClassCode);
                context.startActivity(intent);
            }
        });
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

            //label = itemView.findViewById(R.id.RFGlabel);
            asgmTitle = itemView.findViewById(R.id.asgmTitle);
            asgmOpenDate = itemView.findViewById(R.id.openDate);
            asgmDueDate = itemView.findViewById(R.id.asgmDueDate);
            assDescription = itemView.findViewById(R.id.description);
            card = itemView.findViewById(R.id.card);
        }

//        public void bind(Assignment asgm) {
//            asgmTitle.setText(asgm.getTitle());
////            if (asgm.getUploadDate() != null) {
////                try {
////                    long openDateTimestamp = Long.parseLong(asgm.getUploadDate());
////                    asgmOpenDate.setText("Due " + LMAdapter.convertTimestampToDateTime(openDateTimestamp));
////                } catch (NumberFormatException e) {
////                    e.printStackTrace();
////                    // Handle the exception or log the error
////                }
////            } else {
////                // Handle the case where dueDate is null
////                asgmDueDate.setText("No Open Date");
////            }
//            long openDateTimestamp = Long.parseLong(asgm.getUploadDate());
//            asgmOpenDate.setText("Opened: " + LMAdapter.convertTimestampToDateTime(openDateTimestamp));
//            if (asgm.getDueDate() != null) {
//                try {
//                    long dueDateTimestamp = Long.parseLong(asgm.getDueDate());
//                    asgmDueDate.setText("Due " + LMAdapter.convertTimestampToDateTime(dueDateTimestamp));
//                } catch (NumberFormatException e) {
//                    e.printStackTrace();
//                    // Handle the exception or log the error
//                }
//            } else {
//                // Handle the case where dueDate is null
//                asgmDueDate.setText("No Due Date");
//            }
//            if (asgm.getDescription() != null) {
//                assDescription.setText(asgm.getDescription());
//            }else {
//                assDescription.setText("null");
//            }
//            card.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//        }
    }
}

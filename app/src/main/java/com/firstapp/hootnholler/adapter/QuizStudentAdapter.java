package com.firstapp.hootnholler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.Models.QuizStudentModel;
import com.firstapp.hootnholler.R;
import com.firstapp.hootnholler.RecyViewInterface;

import java.util.ArrayList;

public class QuizStudentAdapter extends RecyclerView.Adapter<QuizStudentAdapter.viewHolder> {

    private final RecyViewInterface recyViewInterface;
    Context context;
    ArrayList<QuizStudentModel> list;

    public QuizStudentAdapter(RecyViewInterface recyViewInterface, Context context, ArrayList<QuizStudentModel> list) {
        this.recyViewInterface = recyViewInterface;
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_educator_review_list, parent, false);
        return new viewHolder(view, recyViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        QuizStudentModel model = list.get(position);

        holder.studentName.setText(model.getStudentName());
        holder.status.setText(model.getStudentQuizStatus());
        if(model.getStudentQuizStatus().equals("Completed")) {
            holder.score.setText(model.getScore());
        } else {
            holder.score.setText("-");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView studentName = itemView.findViewById(R.id.setStudentName);
        TextView status = itemView.findViewById(R.id.setStudentStatus);
        TextView score = itemView.findViewById(R.id.setStudentScore);
        public viewHolder(@NonNull View itemView, RecyViewInterface recyViewInterface) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyViewInterface != null) {
                        int pos = getBindingAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION) {
                            recyViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

        }
    }
}
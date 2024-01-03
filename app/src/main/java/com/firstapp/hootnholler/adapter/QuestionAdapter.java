package com.firstapp.hootnholler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.Models.QuestionModel;
import com.firstapp.hootnholler.R;
import com.firstapp.hootnholler.RecyViewInterface;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.viewHolder> {

    private final RecyViewInterface recyViewInterface;
    Context context;
    ArrayList<QuestionModel> list;

    public QuestionAdapter(Context context, ArrayList<QuestionModel> list, RecyViewInterface recyViewInterface) {
        this.context = context;
        this.list = list;
        this.recyViewInterface = recyViewInterface;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false);
        return new viewHolder(view, recyViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        QuestionModel model = list.get(position);
        holder.question.setText(model.getQuestion());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView question = itemView.findViewById(R.id.questionSetTV);
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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(recyViewInterface != null) {
                        int pos = getBindingAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION) {
                            recyViewInterface.onItemLongClick(pos);
                        }
                    }
                    return true;
                }
            });
        }
    }
}

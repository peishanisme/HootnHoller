package com.example.quiztesting.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiztesting.Models.QuizModel;
import com.example.quiztesting.R;
import com.example.quiztesting.RecyViewInterface;

import java.util.ArrayList;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.viewHolder> {

    private final RecyViewInterface recyViewInterface;
    Context context;
    ArrayList<QuizModel> list;

    public QuizAdapter(RecyViewInterface recyViewInterface, Context context, ArrayList<QuizModel> list) {
        this.recyViewInterface = recyViewInterface;
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quiz_set, parent, false);
        return new viewHolder(view, recyViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        QuizModel model = list.get(position);
        holder.title.setText(model.getTitle());
        holder.status.setText(model.getStatus());
        holder.dueDate.setText(model.getDueDate());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView status;
        TextView dueDate;
        public viewHolder(@NonNull View itemView, RecyViewInterface recyViewInterface) {
            super(itemView);

            title = itemView.findViewById(R.id.setQuizTitle);
            status = itemView.findViewById(R.id.setQuizStatus);
            dueDate = itemView.findViewById(R.id.setQuizDueDate);
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

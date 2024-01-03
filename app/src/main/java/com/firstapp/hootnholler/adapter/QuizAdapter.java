package com.firstapp.hootnholler.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.Models.QuizModel;
import com.firstapp.hootnholler.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.viewHolder> {

    private final RecyQuizInterface recyViewInterface;
    Context context;
    ArrayList<QuizModel> list;

    public QuizAdapter(RecyQuizInterface recyViewInterface, Context context, ArrayList<QuizModel> list) {
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

        Date currentDate = new Date(); // Current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            Date dateToCompare = sdf.parse(model.getDueDate()); // Parsing the string date to a Date object

            // Comparing dates
            if (currentDate.compareTo(dateToCompare) > 0) {
                holder.dueDate.setTextColor(Color.RED);
            } else {
                holder.dueDate.setTextColor(Color.BLACK);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.title.setText(model.getTitle());
        holder.status.setText(model.getStatus());
        holder.dueDate.setText(model.getDueDate());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView status;
        TextView dueDate;
        public viewHolder(@NonNull View itemView, RecyQuizInterface recyViewInterface) {
            super(itemView);

            title = itemView.findViewById(R.id.setQuizTitle);
            status = itemView.findViewById(R.id.setQuizStatus);
            dueDate = itemView.findViewById(R.id.setQuizDueDate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyViewInterface != null) {
                        int pos = getBindingAdapterPosition();
                        String ctgKey = list.get(pos).getCtgKey();

                        if(pos != RecyclerView.NO_POSITION && ctgKey != null) {
                            recyViewInterface.onItemClick(pos, ctgKey);
                        }
                    }
                }
            });

        }
    }
}

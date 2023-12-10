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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
        View view = LayoutInflater.from(context).inflate(R.layout.item_quiz, parent, false);
        return new viewHolder(view, recyViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        QuizModel model = list.get(position);

        holder.title.setText(model.getTitle());
        if(model.isCompleted()) {
            holder.status.setText("Completed");
        }
        else {
            holder.status.setText("Incomplete");
        }

        Picasso.get()
                .load(model.getImage())
                .placeholder(R.drawable.loading)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView title = itemView.findViewById(R.id.quizTitle);
        TextView status = itemView.findViewById(R.id.quizStatus);
        CircleImageView image = itemView.findViewById(R.id.quizImage);
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

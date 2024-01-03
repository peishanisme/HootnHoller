package com.firstapp.hootnholler.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.R;

import java.util.ArrayList;

public class Educator_Subject extends RecyclerView.Adapter<Educator_Subject.ViewHolder> {

    private ArrayList<String> subject;

    public Educator_Subject(ArrayList<String> subject) {
        this.subject = subject;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String Subject = subject.get(position);
        holder.subjectTextView.setText(Subject);
    }

    @Override
    public int getItemCount() {
        return subject.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectTextView = (TextView)itemView.findViewById(R.id.recyclerContent);
        }
    }
}

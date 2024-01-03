package com.firstapp.hootnholler.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.hootnholler.R;
import com.firstapp.hootnholler.entity.Quiz;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Student_Quiz extends RecyclerView.Adapter<Student_Quiz.MyViewHolder>{
    private ArrayList<Quiz> quizList;
    Context context;


    public Student_Quiz(ArrayList<Quiz> quizList) {
        this.quizList = quizList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.student_quiz,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);
        holder.subject.setText(quiz.getSubject());
        holder.setNum.setText(quiz.getSetName());

        holder.scorePercentage.setText(quiz.getScore());

        Picasso.get()
                .load(quiz.getCategoryImage())
                .placeholder(R.drawable.loading)
                .into(holder.quizSubjectImage);

    }


    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView subject,setNum,scorePercentage;
        ImageView quizSubjectImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            subject = itemView.findViewById(R.id.subject);
            setNum = itemView.findViewById(R.id.setNum);
            scorePercentage = itemView.findViewById(R.id.score);
            quizSubjectImage = (ImageView) itemView.findViewById(R.id.quiz_image);
        }
    }


}

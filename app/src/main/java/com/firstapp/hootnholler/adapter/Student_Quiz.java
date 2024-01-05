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
import com.firstapp.hootnholler.entity.Assignment;
import com.firstapp.hootnholler.entity.Quiz;

import java.util.ArrayList;

public class Student_Quiz extends RecyclerView.Adapter<Student_Quiz.MyViewHolder>{
    private ArrayList<Quiz> quizList;
    Context context;


    public Student_Quiz(Context context,ArrayList<Quiz> quizList) {
        this.quizList = quizList;
        this.context=context;
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
        holder.setNum.setText(quiz.getSet());

        double num = quiz.getScore();
        String formattedScore = calculatedScore(num);
        holder.score.setText(formattedScore);

        //set subject image
        if(quiz.getSubject()=="bm"){
            holder.quizSubjectImage.setImageResource(R.drawable.bm_subject);
        }
        else if (quiz.getSubject() == "science"){
            holder.quizSubjectImage.setImageResource((R.drawable.science_subject));
        }
        else if (quiz.getSubject() == "math"){
            holder.quizSubjectImage.setImageResource((R.drawable.mathematics_subject));
        }
        else if (quiz.getSubject() == "eng"){
            holder.quizSubjectImage.setImageResource((R.drawable.eng_subject));
        }
        else if (quiz.getSubject() == "moral"){
            holder.quizSubjectImage.setImageResource((R.drawable.moral_subject));
        }
        else if (quiz.getSubject() == "geo"){
            holder.quizSubjectImage.setImageResource((R.drawable.geo_subject));
        }


    }

    //calculate final score of quiz
    private String calculatedScore(double num) {
        double percentage = (num / 20) * 100;
        // Format the percentage to show two decimal places
        return String.format("%.2f%%", percentage);
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView subject,setNum,score;
        ImageView quizSubjectImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            subject = itemView.findViewById(R.id.subject);
            setNum = itemView.findViewById(R.id.setNum);
            score = itemView.findViewById(R.id.score);
            quizSubjectImage = (ImageView) itemView.findViewById(R.id.quiz_image);
        }
    }


}

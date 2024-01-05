package com.firstapp.hootnholler.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskToDo implements Parcelable {

    HashMap<String, ArrayList<String>> quizToDo;

    public TaskToDo(HashMap<String, ArrayList<String>> quizToDo) {
        this.quizToDo = quizToDo;
    }

    protected TaskToDo(Parcel in) {
        quizToDo = (HashMap<String, ArrayList<String>>) in.readSerializable();
    }

    public static final Creator<TaskToDo> CREATOR = new Creator<TaskToDo>() {
        @Override
        public TaskToDo createFromParcel(Parcel in) {
            return new TaskToDo(in);
        }

        @Override
        public TaskToDo[] newArray(int size) {
            return new TaskToDo[size];
        }
    };

    public HashMap<String, ArrayList<String>> getQuizToDo() {
        return quizToDo;
    }

    public void setQuizToDo(HashMap<String, ArrayList<String>> quizToDo) {
        this.quizToDo = quizToDo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeSerializable(quizToDo);
    }
}

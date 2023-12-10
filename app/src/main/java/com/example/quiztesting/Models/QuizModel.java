package com.example.quiztesting.Models;

import java.util.ArrayList;

public class QuizModel {

    ArrayList<QuestionModel> list;
    String title, image;
    boolean completionStatus;

    public QuizModel(ArrayList<QuestionModel> list, String title, String image, boolean completionStatus) {
        this.list = list;
        this.title = title;
        this.image = image;
        this.completionStatus = completionStatus;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public QuizModel() {
    }

    public ArrayList<QuestionModel> getList() {
        return list;
    }

    public void setList(ArrayList<QuestionModel> list) {
        this.list = list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completionStatus;
    }

    public void setCompletionStatus(boolean completionStatus) {
        this.completionStatus = completionStatus;
    }
}

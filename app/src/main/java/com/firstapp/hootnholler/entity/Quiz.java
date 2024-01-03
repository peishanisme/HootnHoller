package com.firstapp.hootnholler.entity;

import java.util.ArrayList;

public class Quiz {
    private String subject, setName, categoryKey, categoryImage, setKey;
    private int score;
    private long dueDate, postedTime, quizStatus;


    public Quiz() {
    }

    public String getCategoryKey() {
        return categoryKey;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public String getSetKey() {
        return setKey;
    }

    public void setSetKey(String setKey) {
        this.setKey = setKey;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

    public String getSubject() {
        return subject;
    }

    public String getSetName() {
        return setName;
    }

    public long getDueDate() {
        return dueDate;
    }

    public long getPostedTime() {
        return postedTime;
    }

    public long getQuizStatus() {
        return quizStatus;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public void setPostedTime(long postedtime) {
        this.postedTime = postedTime;
    }


    public void setQuizStatus(long quizStatus) {
        this.quizStatus = quizStatus;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

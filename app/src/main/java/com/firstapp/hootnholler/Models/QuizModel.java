package com.firstapp.hootnholler.Models;

import java.util.ArrayList;

public class QuizModel {

    String title, dueDate, status, setKey, ctgKey;
    int progress;

    public QuizModel() {
    }

    public String getSetKey() {
        return setKey;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setSetKey(String setKey) {
        this.setKey = setKey;
    }

    public String getCtgKey() {
        return ctgKey;
    }

    public void setCtgKey(String ctgKey) {
        this.ctgKey = ctgKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

package com.firstapp.hootnholler.entity;

public class Feedback {
    private String content, classCode;
    private boolean positive;
    private long timeStamp;

    public Feedback(String content, boolean positive) {
        this.content = content;
        this.positive = positive;
    }

    public Feedback(){

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isPositive() {
        return positive;
    }

    public void setPositive(boolean positive) {
        this.positive = positive;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }
}

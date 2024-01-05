package com.firstapp.hootnholler.entity;

public class Quiz {
    private String subject,set;
    private double score;

    private long dueDate, openDate, uploadDate, quizStatus;



    public Quiz(String subject, String set, long dueDate, long openDate, long uploadDate, long quizStatus,double score) {
        this.subject = subject;
        this.set = set;
        this.dueDate = dueDate;
        this.openDate = openDate;
        this.uploadDate = uploadDate;
        this.quizStatus = quizStatus;
        this.score = score;
    }


    public String getSubject() {
        return subject;
    }

    public String getSet() {
        return set;
    }

    public long getDueDate() {
        return dueDate;
    }

    public long getOpenDate() {
        return openDate;
    }

    public long getUploadDate() {
        return uploadDate;
    }

    public long getQuizStatus() {
        return quizStatus;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public void setOpenDate(long openDate) {
        this.openDate = openDate;
    }

    public void setUploadDate(long uploadDate) {
        this.uploadDate = uploadDate;
    }

    public void setQuizStatus(long quizStatus) {
        this.quizStatus = quizStatus;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}

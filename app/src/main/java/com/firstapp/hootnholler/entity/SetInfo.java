package com.firstapp.hootnholler.entity;

public class SetInfo {
    private long postedTime;
    private long dueDate;

    public SetInfo(long postedTime, long dueDate) {
        this.postedTime = postedTime;
        this.dueDate = dueDate;
    }

    public SetInfo() {
    }

    public long getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(long postedTime) {
        this.postedTime = postedTime;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }
}

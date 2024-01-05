package com.firstapp.hootnholler.entity;

public class Assignment {

    private String title, description, classCode;

    private long dueDate, openDate, uploadDate, taskStatus;

    public Assignment(){

    }

    public Assignment(String title, String description, long dueDate, long openDate, long uploadDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.openDate = openDate;
        this.uploadDate = uploadDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public long getOpenDate() {
        return openDate;
    }

    public void setOpenDate(long openDate) {
        this.openDate = openDate;
    }

    public long getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(long uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public long getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(long taskStatus) {
        this.taskStatus = taskStatus;
    }
}

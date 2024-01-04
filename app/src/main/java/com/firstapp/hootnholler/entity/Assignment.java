package com.firstapp.hootnholler.entity;

public class Assignment {
    private String title;
    private String description;
    private String uploadDate;
    private String dueDate;
    private String fileName;
    private String fileUri;
    private String assKey;

    public Assignment() {
    }

    public Assignment(String title, String description, String uploadDate, String dueDate, String fileName, String fileUri) {
        this.title = title;
        this.description = description;
        this.uploadDate = uploadDate;
        this.dueDate = dueDate;
        this.fileName = fileName;
        this.fileUri = fileUri;
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

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    public String getAssKey() {
        return assKey;
    }

    public void setAssKey(String assKey) {
        this.assKey = assKey;
    }
}
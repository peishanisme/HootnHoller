package com.firstapp.hootnholler.entity;

public class Assignment {
    private String timestamp;
    private String title;
    private String description;
    private String fileName;
    private String fileUri;
    private String assKey;

    public Assignment() {
    }

    public Assignment(String timestamp, String title, String description, String fileName, String fileUri) {
        this.timestamp = timestamp;
        this.title = title;
        this.description = description;
        this.fileName = fileName;
        this.fileUri = fileUri;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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

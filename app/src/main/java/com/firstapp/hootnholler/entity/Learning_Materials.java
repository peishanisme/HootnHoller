package com.firstapp.hootnholler.entity;

public class Learning_Materials {
    private String timestamp;
    private String title;
    private String description;
    private String fileName;
    private String fileUri;
    private String LMid;

    public Learning_Materials() {
        // Default constructor required for calls to DataSnapshot.getValue(Learning_Materials.class)
    }

    public Learning_Materials(String timestamp, String title, String description, String fileName, String fileUri) {
        this.timestamp = timestamp;
        this.title = title;
        this.description = description;
        this.fileName = fileName;
        this.fileUri = fileUri;
    }

    // Getters and setters for each field


    public String getLMid() {
        return LMid;
    }

    public void setLMid(String LMid) {
        this.LMid = LMid;
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
}

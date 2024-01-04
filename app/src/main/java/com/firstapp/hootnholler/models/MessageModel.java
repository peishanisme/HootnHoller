package com.firstapp.hootnholler.models;

public class MessageModel {
    private String messageText;
    private String senderId;
    private String receiverId;
    private long timestamp;

    public MessageModel() {
        // Default constructor required for Firebase
    }

    public MessageModel(String messageText, String senderId, String receiverId, long timestamp) {
        this.messageText = messageText;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.timestamp = timestamp;
    }
    public String getMessage() {
        return messageText;
    }

    public void setMessage(String message) {
        this.messageText = messageText;
    }

    public String getSenderid() {
        return senderId;
    }

    public void setSenderid(String senderid) {
        this.senderId = senderId;
    }

    public long getTimeStamp() {
        return timestamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timestamp = timestamp;
    }
}


package com.firstapp.hootnholler.models;

public class GroupMessageModel {
    private String messageText;
    private String senderUid;
    private String receiverUid;
    private String receiverName;
    private long timestamp;

    public GroupMessageModel() {
        // Default constructor required for Firebase
    }

    public GroupMessageModel(String messageText, String senderUid, String receiverUid, String receiverName, long timestamp) {
        this.messageText = messageText;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.receiverName = receiverName;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return messageText;
    }

    public void setMessage(String message) {
        this.messageText = message;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}


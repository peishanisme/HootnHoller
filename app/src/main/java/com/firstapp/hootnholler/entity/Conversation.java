package com.firstapp.hootnholler.entity;

public class Conversation {
    private String content,senderUID, timestamp ;

    public Conversation(String content, String senderUID) {
        this.content = content;
        this.senderUID = senderUID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderUID() {
        return senderUID;
    }

    public void setSenderUID(String senderUID) {
        this.senderUID = senderUID;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

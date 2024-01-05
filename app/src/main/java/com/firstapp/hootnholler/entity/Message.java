package com.firstapp.hootnholler.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Message {
    private String content, senderUID;

    private long timestamp;
    private Map<String, Boolean> readStatus;

    public Message(){

    }

    public Message(String content, String senderUID, Map<String, Boolean> readStatus) {
        this.content = content;
        this.senderUID = senderUID;
        this.readStatus = readStatus;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Boolean> getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Map<String, Boolean> readStatus) {
        this.readStatus = readStatus;
    }

}

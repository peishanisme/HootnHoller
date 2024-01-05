package com.firstapp.hootnholler.entity;

import java.util.HashMap;
import java.util.Map;

public class Conversation {
    private String name, conversationID;
    private long unreadMessage;
    private Map<String, Message> message;

    public Conversation(){

    }

    public Conversation(String name, Map<String, Message> message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUnreadMessage() {
        return unreadMessage;
    }

    public void setUnreadMessage(long unreadMessage) {
        this.unreadMessage = unreadMessage;
    }

    public String getConversationID() {
        return conversationID;
    }

    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }

    public Map<String, Message> getMessage() {
        return message;
    }

    public void setMessage(Map<String, Message> message) {
        this.message = message;
    }
}

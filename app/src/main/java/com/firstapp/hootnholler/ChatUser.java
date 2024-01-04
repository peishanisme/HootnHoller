package com.firstapp.hootnholler;

import com.firstapp.hootnholler.entity.User;

public class ChatUser extends User {
    private String lastMessage;
    private static int unreadCount;

    public ChatUser(String userid, String fullname, String email, String role, String phone_number, String birthday, String gender, String lastMessage, int unreadCount) {
        super(userid, fullname, email, role, phone_number, birthday, gender);
        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
    }

    public ChatUser(String fullname, String lastMessage, int unreadCount){

    }

    public ChatUser(String userid, String fullname, String s) {
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public static int getUnreadCount() {
        return unreadCount;
    }


}



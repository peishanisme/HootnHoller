package com.firstapp.hootnholler.models;

public class ContactItem {
    private int profileImage;
    private String username;
    private String role;

    public ContactItem(int profileImage, String username, String role) {
        this.profileImage = profileImage;
        this.username = username;
        this.role = role;
    }

    public int getProfileImage() {
        return profileImage;
    }

    public String getUsername() {
        return username;
    }

    public String getRole(){
        return role;
    }

}


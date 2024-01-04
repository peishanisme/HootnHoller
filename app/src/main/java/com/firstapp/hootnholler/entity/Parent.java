package com.firstapp.hootnholler.entity;

public class Parent {

    private String connectionKey;

    public Parent() {

    }

    public Parent(String code) {
        this.connectionKey = code;
    }

    public String getConnectionKey() {
        return connectionKey;
    }

    public void setConnectionKey(String code) {
        this.connectionKey = code;
    }
}


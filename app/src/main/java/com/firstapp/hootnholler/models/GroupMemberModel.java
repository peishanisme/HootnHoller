package com.firstapp.hootnholler.models;

public class GroupMemberModel {
    private String memberName;

    public GroupMemberModel(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}


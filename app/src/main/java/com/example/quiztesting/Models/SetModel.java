package com.example.quiztesting.Models;

public class SetModel {

    String setName, setKey;

    public SetModel(String setName, String setKey) {
        this.setName = setName;
        this.setKey = setKey;
    }

    public SetModel() {
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public String getSetKey() {
        return setKey;
    }

    public void setSetKey(String setKey) {
        this.setKey = setKey;
    }
}

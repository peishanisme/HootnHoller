package com.firstapp.hootnholler.entity;

import java.util.HashMap;


public class QuizInfoRetrieve {
    private String ctgKey;
    HashMap<String, SetInfo> setKeyInfo;

    public QuizInfoRetrieve(String ctgKey, HashMap<String, SetInfo> setKeyInfo) {
        this.ctgKey = ctgKey;
        this.setKeyInfo = setKeyInfo;
    }

    public String getCtgKey() {
        return ctgKey;
    }

    public void setCtgKey(String ctgKey) {
        this.ctgKey = ctgKey;
    }

    public HashMap<String, SetInfo> getSetKeyInfo() {
        return setKeyInfo;
    }

    public void setSetKeyInfo(HashMap<String, SetInfo> setKeyInfo) {
        this.setKeyInfo = setKeyInfo;
    }
}

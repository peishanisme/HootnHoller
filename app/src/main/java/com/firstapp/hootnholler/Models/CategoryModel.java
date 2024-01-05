package com.firstapp.hootnholler.Models;

import java.util.ArrayList;

public class CategoryModel {

    private String categoryName, categoryImage, ctgKey;
    int setNum;
    ArrayList<String> setKey;

    public CategoryModel(String categoryName, String categoryImage, String ctgKey, int setNum) {
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
        this.ctgKey = ctgKey;
        this.setNum = setNum;
        setKey = new ArrayList<>();
    }

    public CategoryModel(String categoryName, String categoryImage, String ctgKey, int setNum, ArrayList<String> setKey) {
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
        this.ctgKey = ctgKey;
        this.setNum = setNum;
        this.setKey = setKey;
    }

    public CategoryModel() {
        setKey = new ArrayList<>();
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCtgKey() {
        return ctgKey;
    }

    public void setCtgKey(String ctgKey) {
        this.ctgKey = ctgKey;
    }

    public int getSetNum() {
        return setNum;
    }

    public void setSetNum(int setNum) {
        this.setNum = setNum;
    }

    public ArrayList<String> getSetKey() {
        return setKey;
    }

    public void setSetKey(ArrayList<String> setKey) {
        this.setKey = setKey;
    }

    public void addSetKey(String setKey) {
        this.setKey.add(setKey);
    }

    public void removeSetKey(String setKey) {
        this.setKey.remove(setKey);
    }
}

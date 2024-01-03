package com.example.quiztesting.Models;

public class ClassroomModel {

    String classroomKey;
    String classroomName;
    public ClassroomModel(String classroomKey, String classroomName) {
        this.classroomKey = classroomKey;
        this.classroomName = classroomName;
    }

    public String getClassroomKey() {
        return classroomKey;
    }

    public void setClassroomKey(String classroomKey) {
        this.classroomKey = classroomKey;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }
}

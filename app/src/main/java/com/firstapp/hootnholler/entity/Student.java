package com.firstapp.hootnholler.entity;

public class Student {

    public String userName,studentUID,school,level,student_class,connection_key,userUID,profile_URL;

    public Student(){

    }

    public Student(String school, String level,String student_class,String connection_key){
        this.school=school;
        this.level=level;
        this.student_class=student_class;
        this.connection_key=connection_key;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setStudent_class(String student_class) {
        this.student_class = student_class;
    }

    public String getStudent_class() {
        return student_class;
    }

    public void setConnection_key(String connection_key) {
        this.connection_key = connection_key;
    }

    public String getLevel() {
        return level;
    }

    public String getSchool() {
        return school;
    }

    public String getConnection_key() {
        return connection_key;
    }
    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getProfile_URL() {
        return profile_URL;
    }

    public void setProfile_URL(String profile_URL) {
        this.profile_URL = profile_URL;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

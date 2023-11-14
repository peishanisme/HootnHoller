package com.firstapp.hootnholler.entity;

public class Educator {

    private String subject, school;

    public Educator(){

    }

    public Educator(String subject,String school){
        this.subject=subject;
        this.school=school;
    }

    public Educator(String school){
        this.school=school;
    }

    public String getSubject() {
        return subject;
    }

    public String getSchool() {
        return school;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}

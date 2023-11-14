package com.firstapp.hootnholler.entity;

public class Student {

    public String school,level,student_class,code;

    public Student(){

    }

    public Student(String school, String level,String student_class,String code){
        this.school=school;
        this.level=level;
        this.student_class=student_class;
        this.code=code;
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

    public void setCode(String code) {
        this.code = code;
    }

    public String getLevel() {
        return level;
    }

    public String getSchool() {
        return school;
    }

    public String getCode() {
        return code;
    }
}

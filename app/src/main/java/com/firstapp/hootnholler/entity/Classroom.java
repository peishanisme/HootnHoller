package com.firstapp.hootnholler.entity;

public class Classroom {
    public String className,classDescription,classSession,classOwner,classCode;
    public Classroom(){

    }
    public Classroom(String classDescription,String className,String classOwner,String classSession,String classCode){
        this.className=className;
        this.classDescription=classDescription;
        this.classSession=classSession;
        this.classOwner=classOwner;
        this.classCode=classCode;
    }

    public String getClassDescription() {
        return classDescription;
    }

    public String getClassName() {
        return className;
    }

    public String getClassSession() {
        return classSession;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public void setClassDescription(String classdescription) {
        this.classDescription = classdescription;
    }

    public void setClassName(String classname) {
        this.className = classname;
    }

    public void setClassSession(String classsession) {
        this.classSession = classsession;
    }

    public String getClassOwner() {
        return classOwner;
    }

    public void setClassOwner(String classowner) {
        this.classOwner = classowner;
    }

    @Override
    public String toString(){
        return className;
    }
}
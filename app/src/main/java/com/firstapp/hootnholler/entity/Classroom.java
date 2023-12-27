package com.firstapp.hootnholler.entity;

public class Classroom {

    public String className,classDescription,classSession;
    public Classroom(){

    }
    public Classroom(String ClassName,String ClassDescription,String ClassSession){
        this.className=ClassName;
        this.classDescription=ClassDescription;
        this.classSession=ClassSession;
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

    public void setClassDescription(String classDescription) {
        classDescription = classDescription;
    }

    public void setClassName(String className) {
        className = className;
    }

    public void setClassSession(String classSession) {
        classSession = classSession;
    }
}


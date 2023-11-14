package com.firstapp.hootnholler.entity;

public class User {

    public String userid,fullname,email,role,phone_number,birthday,gender;
    public User(){
        //empty constructor
    }

    //constructor used to save info in database
    public User(String userid, String fullname, String email,String role,String phone_number,String birthday,String gender) {
        this.userid = userid;
        this.fullname = fullname;
        this.email = email;
        this.role=role;
        this.phone_number = phone_number;
        this.birthday=birthday;
        this.gender=gender;

        }


    //accessors
    public String getUid() {
        return userid;
    }
    public String getFullname() {
        return fullname;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone_number() {
        return phone_number;
    }
    public String getRole(){return role;}
    public String getBirthday() {
        return birthday;
    }
    public String getGender() {
        return gender;
    }


    //mutators
    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
}

    public void setRole(String role) {
        this.role = role;
    }
}


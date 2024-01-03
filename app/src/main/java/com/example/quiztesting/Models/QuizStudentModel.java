package com.example.quiztesting.Models;

public class QuizStudentModel {

    String studentName, studentQuizStatus, score;

    public QuizStudentModel(String studentName, String studentQuizStatus, String score) {
        this.studentName = studentName;
        this.studentQuizStatus = studentQuizStatus;
        this.score = score;
    }

    public QuizStudentModel() {
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentQuizStatus() {
        return studentQuizStatus;
    }

    public void setStudentQuizStatus(String studentQuizStatus) {
        this.studentQuizStatus = studentQuizStatus;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}

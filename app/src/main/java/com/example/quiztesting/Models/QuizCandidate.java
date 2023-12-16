package com.example.quiztesting.Models;

public class QuizCandidate implements Comparable<QuizCandidate> {

    String uid;
    int score;

    public QuizCandidate(String uid, int score) {
        this.uid = uid;
        this.score = score;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(QuizCandidate otherCandidate) {
        return Integer.compare(otherCandidate.getScore(), this.score);
    }
}

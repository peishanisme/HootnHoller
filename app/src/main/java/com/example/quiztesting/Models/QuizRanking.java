package com.example.quiztesting.Models;

import java.util.ArrayList;
import java.util.Collections;

public class QuizRanking {

    ArrayList<QuizCandidate> candidates;

    public QuizRanking(ArrayList<QuizCandidate> candidates) {
        this.candidates = candidates;
    }

    public QuizRanking() {
        candidates = new ArrayList<>();
    }

    public ArrayList<QuizCandidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(ArrayList<QuizCandidate> candidates) {
        this.candidates = candidates;
    }

    public void addCandidate(QuizCandidate candidate) {
        candidates.add(candidate);
    }

    public boolean contain(QuizCandidate candidate) {
        return candidates.contains(candidate);
    }

    public int getPosition(QuizCandidate candidate) {
        if(contain(candidate)) {
            for(int i=0; i<candidates.size(); i++) {
                if(candidate.getUid().equals(candidates.get(i).getUid())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void sortRanking() {
        Collections.sort(candidates);
    }
}

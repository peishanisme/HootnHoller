package com.firstapp.hootnholler.Models;

public class AnswerModel {

    String questionKey, optionSelected;
    boolean correctness;

    public AnswerModel(String questionKey, String optionSelected, boolean correctness) {
        this.questionKey = questionKey;
        this.optionSelected = optionSelected;
        this.correctness = correctness;
    }

    public String getOptionSelected() {
        return optionSelected;
    }

    public void setOptionSelected(String optionSelected) {
        this.optionSelected = optionSelected;
    }

    public boolean isCorrectness() {
        return correctness;
    }

    public void setCorrectness(boolean correctness) {
        this.correctness = correctness;
    }
}

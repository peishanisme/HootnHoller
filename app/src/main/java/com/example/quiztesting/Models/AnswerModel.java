package com.example.quiztesting.Models;

public class AnswerModel {

    String optionSelected;

    public AnswerModel(String optionSelected, boolean correctness) {
        this.optionSelected = optionSelected;
        this.correctness = correctness;
    }

    boolean correctness;

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

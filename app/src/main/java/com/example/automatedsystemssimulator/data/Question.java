package com.example.automatedsystemssimulator.data;

import java.util.List;


public class Question {
    private String text;
    private List<Option> options;
    private int correctIndex;

    public Question(String text, List<Option> options, int correctIndex) {
        this.text = text;
        this.options = options;
        this.correctIndex = correctIndex;
    }

    public String getText() { return text; }
    public List<Option> getOptions() { return options; }
    public int getCorrectIndex() { return correctIndex; }
}
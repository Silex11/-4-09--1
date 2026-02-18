package com.example.automatedsystemssimulator.data;

public class Option {
    private String text;
    private boolean isCorrect;
    private String feedback;

    public Option(String text, boolean isCorrect, String feedback) {
        this.text = text;
        this.isCorrect = isCorrect;
        this.feedback = feedback;
    }

    public String getText() { return text; }
    public boolean isCorrect() { return isCorrect; }
    public String getFeedback() { return feedback; }
}
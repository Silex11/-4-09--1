package com.example.automatedsystemssimulator.data;

import java.util.List;


public class Test {
    private int id;
    private String title;
    private String topic;
    private List<Question> questions;

    public Test(int id, String title, String topic, List<Question> questions) {
        this.id = id;
        this.title = title;
        this.topic = topic;
        this.questions = questions;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getTopic() { return topic; }
    public List<Question> getQuestions() { return questions; }
}
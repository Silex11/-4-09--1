package com.example.automatedsystemssimulator.data;

import java.util.List;


public class PracticeScenario {
    private int id;
    private String title;
    private String description;
    private String task;
    private String topic;
    private int difficulty;
    private List<Option> options;

    public PracticeScenario(int id, String title, String description, String task,
                            String topic, int difficulty, List<Option> options) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.task = task;
        this.topic = topic;
        this.difficulty = difficulty;
        this.options = options;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getTask() { return task; }
    public String getTopic() { return topic; }
    public int getDifficulty() { return difficulty; }
    public List<Option> getOptions() { return options; }
}
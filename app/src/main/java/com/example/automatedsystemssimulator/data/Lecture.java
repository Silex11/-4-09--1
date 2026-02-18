package com.example.automatedsystemssimulator.data;

public class Lecture {
    private int id;
    private String title;
    private String content;
    private String topic;
    private int iconRes;
    private String videoUrl;
    private String importantInfo;

    public Lecture(int id, String title, String content, String topic, int iconRes, String videoUrl, String importantInfo) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.topic = topic;
        this.iconRes = iconRes;
        this.videoUrl = videoUrl;
        this.importantInfo = importantInfo;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getTopic() { return topic; }
    public int getIconRes() { return iconRes; }
    public String getVideoUrl() { return videoUrl; }
    public String getImportantInfo() { return importantInfo; }
}
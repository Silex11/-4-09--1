package com.example.automatedsystemssimulator.data;

public class Achievement {
    private String id;
    private String title;
    private String description;
    private String emoji;
    private boolean unlocked;

    public Achievement(String id, String title, String description, String emoji, boolean unlocked) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.emoji = emoji;
        this.unlocked = unlocked;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getEmoji() { return emoji; }
    public boolean isUnlocked() { return unlocked; }
    public void setUnlocked(boolean unlocked) { this.unlocked = unlocked; }
}
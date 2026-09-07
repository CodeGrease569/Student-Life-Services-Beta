package com.example.studentlife.models;

public class NotificationItem {
    private int id;
    private String title;
    private String message;
    private String timestamp;
    private String type; // scholarship, document, concern, inquiry, announcement
    private boolean isRead;

    public NotificationItem() {}

    public NotificationItem(int id, String title, String message, String timestamp, String type, boolean isRead) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
        this.type = type;
        this.isRead = isRead;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}

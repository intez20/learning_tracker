package com.tracker.dto.analytics;

import java.time.LocalDateTime;

public class ActivityDTO {
    private Long id;
    private String type;
    private String title;
    private LocalDateTime timestamp;

    public ActivityDTO() {
    }

    public ActivityDTO(Long id, String type, String title, LocalDateTime timestamp) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

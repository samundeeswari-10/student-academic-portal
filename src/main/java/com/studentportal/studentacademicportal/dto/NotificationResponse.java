package com.studentportal.studentacademicportal.dto;

import java.time.LocalDateTime;

public class NotificationResponse {

    private Long id;
    private Long studentId;
    private String title;
    private String message;
    private String type;
    private boolean read;
    private LocalDateTime createdAt;

    public NotificationResponse(
            Long id,
            Long studentId,
            String title,
            String message,
            String type,
            boolean read,
            LocalDateTime createdAt) {

        this.id = id;
        this.studentId = studentId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.read = read;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public boolean isRead() {
        return read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
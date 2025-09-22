package com.MyBooking.feedback.dto;

import java.time.LocalDateTime;

/**
 * DTO for feedback reply responses
 */
public class FeedbackReplyResponseDto {

    private Long id;

    private Long feedbackId;

    private Long adminUserId;

    private String adminUserName;

    private String adminUserEmail;

    private String message;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Constructors
    public FeedbackReplyResponseDto() {}

    public FeedbackReplyResponseDto(Long id, Long feedbackId, Long adminUserId, 
                                  String adminUserName, String adminUserEmail, String message, 
                                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.feedbackId = feedbackId;
        this.adminUserId = adminUserId;
        this.adminUserName = adminUserName;
        this.adminUserEmail = adminUserEmail;
        this.message = message;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Long getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(Long adminUserId) {
        this.adminUserId = adminUserId;
    }

    public String getAdminUserName() {
        return adminUserName;
    }

    public void setAdminUserName(String adminUserName) {
        this.adminUserName = adminUserName;
    }

    public String getAdminUserEmail() {
        return adminUserEmail;
    }

    public void setAdminUserEmail(String adminUserEmail) {
        this.adminUserEmail = adminUserEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "FeedbackReplyResponseDto{" +
                "id=" + id +
                ", feedbackId=" + feedbackId +
                ", adminUserId=" + adminUserId +
                ", adminUserName='" + adminUserName + '\'' +
                ", adminUserEmail='" + adminUserEmail + '\'' +
                ", message='" + message + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

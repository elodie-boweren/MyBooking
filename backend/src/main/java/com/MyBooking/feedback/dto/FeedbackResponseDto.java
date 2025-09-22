package com.MyBooking.feedback.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for feedback responses
 */
public class FeedbackResponseDto {

    private Long id;

    private Long reservationId;

    private String reservationNumber;

    private Long userId;

    private String userName;

    private String userEmail;

    private Integer rating;

    private String comment;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<FeedbackReplyResponseDto> replies;

    private Long replyCount;

    // Constructors
    public FeedbackResponseDto() {}

    public FeedbackResponseDto(Long id, Long reservationId, String reservationNumber, 
                             Long userId, String userName, String userEmail, Integer rating, 
                             String comment, LocalDateTime createdAt, LocalDateTime updatedAt, 
                             List<FeedbackReplyResponseDto> replies, Long replyCount) {
        this.id = id;
        this.reservationId = reservationId;
        this.reservationNumber = reservationNumber;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.replies = replies;
        this.replyCount = replyCount;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public List<FeedbackReplyResponseDto> getReplies() {
        return replies;
    }

    public void setReplies(List<FeedbackReplyResponseDto> replies) {
        this.replies = replies;
    }

    public Long getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(Long replyCount) {
        this.replyCount = replyCount;
    }

    @Override
    public String toString() {
        return "FeedbackResponseDto{" +
                "id=" + id +
                ", reservationId=" + reservationId +
                ", reservationNumber='" + reservationNumber + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", replies=" + replies +
                ", replyCount=" + replyCount +
                '}';
    }
}

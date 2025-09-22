package com.MyBooking.feedback.dto;

import java.time.LocalDateTime;

/**
 * DTO for feedback search criteria
 */
public class FeedbackSearchCriteriaDto {

    private Long userId;

    private Long reservationId;

    private Integer minRating;

    private Integer maxRating;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Boolean hasComments;

    // Constructors
    public FeedbackSearchCriteriaDto() {}

    public FeedbackSearchCriteriaDto(Long userId, Long reservationId, Integer minRating, 
                                   Integer maxRating, LocalDateTime startDate, LocalDateTime endDate, 
                                   Boolean hasComments) {
        this.userId = userId;
        this.reservationId = reservationId;
        this.minRating = minRating;
        this.maxRating = maxRating;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hasComments = hasComments;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Integer getMinRating() {
        return minRating;
    }

    public void setMinRating(Integer minRating) {
        this.minRating = minRating;
    }

    public Integer getMaxRating() {
        return maxRating;
    }

    public void setMaxRating(Integer maxRating) {
        this.maxRating = maxRating;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Boolean getHasComments() {
        return hasComments;
    }

    public void setHasComments(Boolean hasComments) {
        this.hasComments = hasComments;
    }

    @Override
    public String toString() {
        return "FeedbackSearchCriteriaDto{" +
                "userId=" + userId +
                ", reservationId=" + reservationId +
                ", minRating=" + minRating +
                ", maxRating=" + maxRating +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", hasComments=" + hasComments +
                '}';
    }
}

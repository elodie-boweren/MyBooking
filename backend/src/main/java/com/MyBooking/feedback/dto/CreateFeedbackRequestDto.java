package com.MyBooking.feedback.dto;

import jakarta.validation.constraints.*;

/**
 * DTO for creating new feedback
 */
public class CreateFeedbackRequestDto {

    @NotNull(message = "Reservation ID is required")
    @Positive(message = "Reservation ID must be positive")
    private Long reservationId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    private String comment;

    // Constructors
    public CreateFeedbackRequestDto() {}

    public CreateFeedbackRequestDto(Long reservationId, Integer rating, String comment) {
        this.reservationId = reservationId;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and Setters
    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
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

    @Override
    public String toString() {
        return "CreateFeedbackRequestDto{" +
                "reservationId=" + reservationId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }
}

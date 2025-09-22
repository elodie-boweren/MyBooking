package com.MyBooking.feedback.dto;

import jakarta.validation.constraints.*;

/**
 * DTO for updating existing feedback
 */
public class UpdateFeedbackRequestDto {

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    private String comment;

    // Constructors
    public UpdateFeedbackRequestDto() {}

    public UpdateFeedbackRequestDto(Integer rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and Setters
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
        return "UpdateFeedbackRequestDto{" +
                "rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }
}

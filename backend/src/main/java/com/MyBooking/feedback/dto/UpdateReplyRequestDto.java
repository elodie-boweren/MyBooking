package com.MyBooking.feedback.dto;

import jakarta.validation.constraints.*;

/**
 * DTO for updating feedback replies
 */
public class UpdateReplyRequestDto {

    @NotBlank(message = "Message is required")
    @Size(max = 2000, message = "Message cannot exceed 2000 characters")
    private String message;

    // Constructors
    public UpdateReplyRequestDto() {}

    public UpdateReplyRequestDto(String message) {
        this.message = message;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "UpdateReplyRequestDto{" +
                "message='" + message + '\'' +
                '}';
    }
}

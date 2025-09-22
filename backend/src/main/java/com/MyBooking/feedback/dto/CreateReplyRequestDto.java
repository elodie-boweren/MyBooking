package com.MyBooking.feedback.dto;

import jakarta.validation.constraints.*;

/**
 * DTO for creating feedback replies
 */
public class CreateReplyRequestDto {

    @NotBlank(message = "Message is required")
    @Size(max = 2000, message = "Message cannot exceed 2000 characters")
    private String message;

    // Constructors
    public CreateReplyRequestDto() {}

    public CreateReplyRequestDto(String message) {
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
        return "CreateReplyRequestDto{" +
                "message='" + message + '\'' +
                '}';
    }
}

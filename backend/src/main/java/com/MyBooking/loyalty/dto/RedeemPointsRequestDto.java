package com.MyBooking.loyalty.dto;

import jakarta.validation.constraints.*;

/**
 * DTO for redeeming points requests
 */
public class RedeemPointsRequestDto {

    @NotNull(message = "User ID is required")
    @Min(value = 1, message = "User ID must be positive")
    private Long userId;

    @NotNull(message = "Points is required")
    @Min(value = 100, message = "Minimum redemption is 100 points")
    @Max(value = 10000, message = "Maximum redemption is 10,000 points per transaction")
    private Integer points;

    @NotBlank(message = "Reason is required")
    @Size(max = 500, message = "Reason cannot exceed 500 characters")
    private String reason;

    // Constructors
    public RedeemPointsRequestDto() {}

    public RedeemPointsRequestDto(Long userId, Integer points, String reason) {
        this.userId = userId;
        this.points = points;
        this.reason = reason;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "RedeemPointsRequestDto{" +
                "userId=" + userId +
                ", points=" + points +
                ", reason='" + reason + '\'' +
                '}';
    }
}

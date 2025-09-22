package com.MyBooking.loyalty.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * DTO for earning points requests (admin only)
 */
public class EarnPointsRequestDto {

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Long userId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
    @Digits(integer = 10, fraction = 2, message = "Amount must have at most 10 integer digits and 2 decimal places")
    private BigDecimal amount;

    @NotBlank(message = "Reason is required")
    @Size(max = 500, message = "Reason cannot exceed 500 characters")
    private String reason;

    @Positive(message = "Reservation ID must be positive")
    private Long reservationId;

    // Constructors
    public EarnPointsRequestDto() {}

    public EarnPointsRequestDto(Long userId, BigDecimal amount, String reason, Long reservationId) {
        this.userId = userId;
        this.amount = amount;
        this.reason = reason;
        this.reservationId = reservationId;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    @Override
    public String toString() {
        return "EarnPointsRequestDto{" +
                "userId=" + userId +
                ", amount=" + amount +
                ", reason='" + reason + '\'' +
                ", reservationId=" + reservationId +
                '}';
    }
}

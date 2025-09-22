package com.MyBooking.loyalty.dto;

import com.MyBooking.loyalty.domain.LoyaltyTxType;
import java.time.LocalDateTime;

/**
 * DTO for loyalty transaction responses
 */
public class LoyaltyTransactionResponseDto {

    private Long id;

    private Long accountId;

    private Long userId;

    private String userName;

    private String userEmail;

    private LoyaltyTxType type;

    private Integer points;

    private Long reservationId;

    private String reservationNumber;

    private LocalDateTime createdAt;

    // Constructors
    public LoyaltyTransactionResponseDto() {}

    public LoyaltyTransactionResponseDto(Long id, Long accountId, Long userId, String userName,
                                       String userEmail, LoyaltyTxType type, Integer points,
                                       Long reservationId, String reservationNumber, LocalDateTime createdAt) {
        this.id = id;
        this.accountId = accountId;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.type = type;
        this.points = points;
        this.reservationId = reservationId;
        this.reservationNumber = reservationNumber;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
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

    public LoyaltyTxType getType() {
        return type;
    }

    public void setType(LoyaltyTxType type) {
        this.type = type;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "LoyaltyTransactionResponseDto{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", type=" + type +
                ", points=" + points +
                ", reservationId=" + reservationId +
                ", reservationNumber='" + reservationNumber + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

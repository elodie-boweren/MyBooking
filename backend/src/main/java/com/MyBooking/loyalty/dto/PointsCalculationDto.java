package com.MyBooking.loyalty.dto;

import java.math.BigDecimal;

/**
 * DTO for points calculation responses
 */
public class PointsCalculationDto {

    private Integer points;

    private BigDecimal amount;

    private BigDecimal discountAmount;

    private Integer maxRedeemablePoints;

    private String calculationType;

    // Constructors
    public PointsCalculationDto() {}

    public PointsCalculationDto(Integer points, BigDecimal amount, BigDecimal discountAmount,
                              Integer maxRedeemablePoints, String calculationType) {
        this.points = points;
        this.amount = amount;
        this.discountAmount = discountAmount;
        this.maxRedeemablePoints = maxRedeemablePoints;
        this.calculationType = calculationType;
    }

    // Getters and Setters
    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Integer getMaxRedeemablePoints() {
        return maxRedeemablePoints;
    }

    public void setMaxRedeemablePoints(Integer maxRedeemablePoints) {
        this.maxRedeemablePoints = maxRedeemablePoints;
    }

    public String getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(String calculationType) {
        this.calculationType = calculationType;
    }

    @Override
    public String toString() {
        return "PointsCalculationDto{" +
                "points=" + points +
                ", amount=" + amount +
                ", discountAmount=" + discountAmount +
                ", maxRedeemablePoints=" + maxRedeemablePoints +
                ", calculationType='" + calculationType + '\'' +
                '}';
    }
}

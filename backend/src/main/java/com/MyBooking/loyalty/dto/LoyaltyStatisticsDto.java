package com.MyBooking.loyalty.dto;

/**
 * DTO for loyalty program statistics
 */
public class LoyaltyStatisticsDto {

    private long totalAccounts;

    private long totalTransactions;

    private long totalBalance;

    private double averageBalance;

    private long totalEarnedPoints;

    private long totalRedeemedPoints;

    // Constructors
    public LoyaltyStatisticsDto() {}

    public LoyaltyStatisticsDto(long totalAccounts, long totalTransactions, long totalBalance,
                               double averageBalance, long totalEarnedPoints, long totalRedeemedPoints) {
        this.totalAccounts = totalAccounts;
        this.totalTransactions = totalTransactions;
        this.totalBalance = totalBalance;
        this.averageBalance = averageBalance;
        this.totalEarnedPoints = totalEarnedPoints;
        this.totalRedeemedPoints = totalRedeemedPoints;
    }

    // Getters and Setters
    public long getTotalAccounts() {
        return totalAccounts;
    }

    public void setTotalAccounts(long totalAccounts) {
        this.totalAccounts = totalAccounts;
    }

    public long getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(long totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public long getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(long totalBalance) {
        this.totalBalance = totalBalance;
    }

    public double getAverageBalance() {
        return averageBalance;
    }

    public void setAverageBalance(double averageBalance) {
        this.averageBalance = averageBalance;
    }

    public long getTotalEarnedPoints() {
        return totalEarnedPoints;
    }

    public void setTotalEarnedPoints(long totalEarnedPoints) {
        this.totalEarnedPoints = totalEarnedPoints;
    }

    public long getTotalRedeemedPoints() {
        return totalRedeemedPoints;
    }

    public void setTotalRedeemedPoints(long totalRedeemedPoints) {
        this.totalRedeemedPoints = totalRedeemedPoints;
    }

    @Override
    public String toString() {
        return "LoyaltyStatisticsDto{" +
                "totalAccounts=" + totalAccounts +
                ", totalTransactions=" + totalTransactions +
                ", totalBalance=" + totalBalance +
                ", averageBalance=" + averageBalance +
                ", totalEarnedPoints=" + totalEarnedPoints +
                ", totalRedeemedPoints=" + totalRedeemedPoints +
                '}';
    }
}

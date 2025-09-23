package com.MyBooking.analytics.dto;

import java.util.Map;

public class CustomerInsightsDto {
    private long totalCustomers;
    private long activeLoyaltyUsers;
    private double loyaltyEngagementRate;
    private Map<String, Object> customerFeedback;

    // Constructors
    public CustomerInsightsDto() {}

    public CustomerInsightsDto(long totalCustomers, long activeLoyaltyUsers, double loyaltyEngagementRate,
                              Map<String, Object> customerFeedback) {
        this.totalCustomers = totalCustomers;
        this.activeLoyaltyUsers = activeLoyaltyUsers;
        this.loyaltyEngagementRate = loyaltyEngagementRate;
        this.customerFeedback = customerFeedback;
    }

    // Getters and Setters
    public long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public long getActiveLoyaltyUsers() {
        return activeLoyaltyUsers;
    }

    public void setActiveLoyaltyUsers(long activeLoyaltyUsers) {
        this.activeLoyaltyUsers = activeLoyaltyUsers;
    }

    public double getLoyaltyEngagementRate() {
        return loyaltyEngagementRate;
    }

    public void setLoyaltyEngagementRate(double loyaltyEngagementRate) {
        this.loyaltyEngagementRate = loyaltyEngagementRate;
    }

    public Map<String, Object> getCustomerFeedback() {
        return customerFeedback;
    }

    public void setCustomerFeedback(Map<String, Object> customerFeedback) {
        this.customerFeedback = customerFeedback;
    }
}

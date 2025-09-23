package com.MyBooking.analytics.dto;

import java.util.Map;

public class SystemPerformanceDto {
    private double bookingSuccessRate;
    private Map<String, Object> responseMetrics;
    private Map<String, Object> healthIndicators;

    // Constructors
    public SystemPerformanceDto() {}

    public SystemPerformanceDto(double bookingSuccessRate, Map<String, Object> responseMetrics,
                               Map<String, Object> healthIndicators) {
        this.bookingSuccessRate = bookingSuccessRate;
        this.responseMetrics = responseMetrics;
        this.healthIndicators = healthIndicators;
    }

    // Getters and Setters
    public double getBookingSuccessRate() {
        return bookingSuccessRate;
    }

    public void setBookingSuccessRate(double bookingSuccessRate) {
        this.bookingSuccessRate = bookingSuccessRate;
    }

    public Map<String, Object> getResponseMetrics() {
        return responseMetrics;
    }

    public void setResponseMetrics(Map<String, Object> responseMetrics) {
        this.responseMetrics = responseMetrics;
    }

    public Map<String, Object> getHealthIndicators() {
        return healthIndicators;
    }

    public void setHealthIndicators(Map<String, Object> healthIndicators) {
        this.healthIndicators = healthIndicators;
    }
}

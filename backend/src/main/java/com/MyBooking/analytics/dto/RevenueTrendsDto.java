package com.MyBooking.analytics.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class RevenueTrendsDto {
    private List<Map<String, Object>> monthlyRevenue;
    private BigDecimal totalPeriodRevenue;

    // Constructors
    public RevenueTrendsDto() {}

    public RevenueTrendsDto(List<Map<String, Object>> monthlyRevenue, BigDecimal totalPeriodRevenue) {
        this.monthlyRevenue = monthlyRevenue;
        this.totalPeriodRevenue = totalPeriodRevenue;
    }

    // Getters and Setters
    public List<Map<String, Object>> getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public void setMonthlyRevenue(List<Map<String, Object>> monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public BigDecimal getTotalPeriodRevenue() {
        return totalPeriodRevenue;
    }

    public void setTotalPeriodRevenue(BigDecimal totalPeriodRevenue) {
        this.totalPeriodRevenue = totalPeriodRevenue;
    }
}

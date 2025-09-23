package com.MyBooking.analytics.dto;

import java.math.BigDecimal;
import java.util.Map;

public class RevenueAnalyticsDto {
    private BigDecimal roomRevenue;
    private BigDecimal eventRevenue;
    private BigDecimal totalRevenue;
    private Map<String, BigDecimal> revenueByService;
    private BigDecimal avgRevenuePerCustomer;

    // Constructors
    public RevenueAnalyticsDto() {}

    public RevenueAnalyticsDto(BigDecimal roomRevenue, BigDecimal eventRevenue, BigDecimal totalRevenue,
                              Map<String, BigDecimal> revenueByService, BigDecimal avgRevenuePerCustomer) {
        this.roomRevenue = roomRevenue;
        this.eventRevenue = eventRevenue;
        this.totalRevenue = totalRevenue;
        this.revenueByService = revenueByService;
        this.avgRevenuePerCustomer = avgRevenuePerCustomer;
    }

    // Getters and Setters
    public BigDecimal getRoomRevenue() {
        return roomRevenue;
    }

    public void setRoomRevenue(BigDecimal roomRevenue) {
        this.roomRevenue = roomRevenue;
    }

    public BigDecimal getEventRevenue() {
        return eventRevenue;
    }

    public void setEventRevenue(BigDecimal eventRevenue) {
        this.eventRevenue = eventRevenue;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Map<String, BigDecimal> getRevenueByService() {
        return revenueByService;
    }

    public void setRevenueByService(Map<String, BigDecimal> revenueByService) {
        this.revenueByService = revenueByService;
    }

    public BigDecimal getAvgRevenuePerCustomer() {
        return avgRevenuePerCustomer;
    }

    public void setAvgRevenuePerCustomer(BigDecimal avgRevenuePerCustomer) {
        this.avgRevenuePerCustomer = avgRevenuePerCustomer;
    }
}

package com.MyBooking.analytics.dto;

import java.util.Map;

public class DashboardDataDto {
    private Map<String, Object> revenueSummary;
    private Map<String, Object> occupancySummary;
    private Map<String, Object> customerInsights;
    private Map<String, Object> employeePerformance;
    private Map<String, Object> operationalKPIs;
    private Map<String, Object> systemPerformance;

    // Constructors
    public DashboardDataDto() {}

    public DashboardDataDto(Map<String, Object> revenueSummary, Map<String, Object> occupancySummary,
                           Map<String, Object> customerInsights, Map<String, Object> employeePerformance,
                           Map<String, Object> operationalKPIs, Map<String, Object> systemPerformance) {
        this.revenueSummary = revenueSummary;
        this.occupancySummary = occupancySummary;
        this.customerInsights = customerInsights;
        this.employeePerformance = employeePerformance;
        this.operationalKPIs = operationalKPIs;
        this.systemPerformance = systemPerformance;
    }

    // Getters and Setters
    public Map<String, Object> getRevenueSummary() {
        return revenueSummary;
    }

    public void setRevenueSummary(Map<String, Object> revenueSummary) {
        this.revenueSummary = revenueSummary;
    }

    public Map<String, Object> getOccupancySummary() {
        return occupancySummary;
    }

    public void setOccupancySummary(Map<String, Object> occupancySummary) {
        this.occupancySummary = occupancySummary;
    }

    public Map<String, Object> getCustomerInsights() {
        return customerInsights;
    }

    public void setCustomerInsights(Map<String, Object> customerInsights) {
        this.customerInsights = customerInsights;
    }

    public Map<String, Object> getEmployeePerformance() {
        return employeePerformance;
    }

    public void setEmployeePerformance(Map<String, Object> employeePerformance) {
        this.employeePerformance = employeePerformance;
    }

    public Map<String, Object> getOperationalKPIs() {
        return operationalKPIs;
    }

    public void setOperationalKPIs(Map<String, Object> operationalKPIs) {
        this.operationalKPIs = operationalKPIs;
    }

    public Map<String, Object> getSystemPerformance() {
        return systemPerformance;
    }

    public void setSystemPerformance(Map<String, Object> systemPerformance) {
        this.systemPerformance = systemPerformance;
    }
}

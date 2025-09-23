package com.MyBooking.analytics.dto;

import java.util.Map;

public class OperationalKPIsDto {
    private long totalRooms;
    private long totalInstallations;
    private long totalEmployees;
    private long totalCustomers;
    private Map<String, Object> serviceMetrics;
    private Map<String, Object> resourceUtilization;

    // Constructors
    public OperationalKPIsDto() {}

    public OperationalKPIsDto(long totalRooms, long totalInstallations, long totalEmployees, long totalCustomers,
                             Map<String, Object> serviceMetrics, Map<String, Object> resourceUtilization) {
        this.totalRooms = totalRooms;
        this.totalInstallations = totalInstallations;
        this.totalEmployees = totalEmployees;
        this.totalCustomers = totalCustomers;
        this.serviceMetrics = serviceMetrics;
        this.resourceUtilization = resourceUtilization;
    }

    // Getters and Setters
    public long getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(long totalRooms) {
        this.totalRooms = totalRooms;
    }

    public long getTotalInstallations() {
        return totalInstallations;
    }

    public void setTotalInstallations(long totalInstallations) {
        this.totalInstallations = totalInstallations;
    }

    public long getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(long totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public Map<String, Object> getServiceMetrics() {
        return serviceMetrics;
    }

    public void setServiceMetrics(Map<String, Object> serviceMetrics) {
        this.serviceMetrics = serviceMetrics;
    }

    public Map<String, Object> getResourceUtilization() {
        return resourceUtilization;
    }

    public void setResourceUtilization(Map<String, Object> resourceUtilization) {
        this.resourceUtilization = resourceUtilization;
    }
}

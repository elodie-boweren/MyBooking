package com.MyBooking.analytics.dto;

import java.util.Map;

public class EmployeeProductivityDto {
    private long totalEmployees;
    private long activeEmployees;
    private double activeEmployeeRate;
    private Map<String, Object> taskProductivity;

    // Constructors
    public EmployeeProductivityDto() {}

    public EmployeeProductivityDto(long totalEmployees, long activeEmployees, double activeEmployeeRate,
                                  Map<String, Object> taskProductivity) {
        this.totalEmployees = totalEmployees;
        this.activeEmployees = activeEmployees;
        this.activeEmployeeRate = activeEmployeeRate;
        this.taskProductivity = taskProductivity;
    }

    // Getters and Setters
    public long getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(long totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public long getActiveEmployees() {
        return activeEmployees;
    }

    public void setActiveEmployees(long activeEmployees) {
        this.activeEmployees = activeEmployees;
    }

    public double getActiveEmployeeRate() {
        return activeEmployeeRate;
    }

    public void setActiveEmployeeRate(double activeEmployeeRate) {
        this.activeEmployeeRate = activeEmployeeRate;
    }

    public Map<String, Object> getTaskProductivity() {
        return taskProductivity;
    }

    public void setTaskProductivity(Map<String, Object> taskProductivity) {
        this.taskProductivity = taskProductivity;
    }
}

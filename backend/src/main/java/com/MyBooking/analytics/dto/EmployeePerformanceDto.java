package com.MyBooking.analytics.dto;

import java.util.Map;

public class EmployeePerformanceDto {
    private Map<String, Object> taskMetrics;
    private Map<String, Object> trainingMetrics;
    private Map<String, Object> leaveMetrics;

    // Constructors
    public EmployeePerformanceDto() {}

    public EmployeePerformanceDto(Map<String, Object> taskMetrics, Map<String, Object> trainingMetrics,
                                 Map<String, Object> leaveMetrics) {
        this.taskMetrics = taskMetrics;
        this.trainingMetrics = trainingMetrics;
        this.leaveMetrics = leaveMetrics;
    }

    // Getters and Setters
    public Map<String, Object> getTaskMetrics() {
        return taskMetrics;
    }

    public void setTaskMetrics(Map<String, Object> taskMetrics) {
        this.taskMetrics = taskMetrics;
    }

    public Map<String, Object> getTrainingMetrics() {
        return trainingMetrics;
    }

    public void setTrainingMetrics(Map<String, Object> trainingMetrics) {
        this.trainingMetrics = trainingMetrics;
    }

    public Map<String, Object> getLeaveMetrics() {
        return leaveMetrics;
    }

    public void setLeaveMetrics(Map<String, Object> leaveMetrics) {
        this.leaveMetrics = leaveMetrics;
    }
}

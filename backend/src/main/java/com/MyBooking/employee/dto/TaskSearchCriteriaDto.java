package com.MyBooking.employee.dto;

import com.MyBooking.employee.domain.TaskStatus;

public class TaskSearchCriteriaDto {
    private Long employeeId;
    private TaskStatus status;
    private String title;

    // Constructors
    public TaskSearchCriteriaDto() {}

    public TaskSearchCriteriaDto(Long employeeId, TaskStatus status, String title) {
        this.employeeId = employeeId;
        this.status = status;
        this.title = title;
    }

    // Getters and Setters
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}

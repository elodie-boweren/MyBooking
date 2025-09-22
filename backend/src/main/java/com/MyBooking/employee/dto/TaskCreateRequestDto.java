package com.MyBooking.employee.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TaskCreateRequestDto {
    @NotNull(message = "Employee ID is required")
    private Long employeeId;
    
    @NotBlank(message = "Task title is required")
    @Size(max = 200, message = "Task title must not exceed 200 characters")
    private String title;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    // Constructors
    public TaskCreateRequestDto() {}

    public TaskCreateRequestDto(Long employeeId, String title, String description) {
        this.employeeId = employeeId;
        this.title = title;
        this.description = description;
    }

    // Getters and Setters
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

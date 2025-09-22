package com.MyBooking.employee.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EmployeeCreateRequestDto {
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotBlank(message = "Job title is required")
    @Size(max = 100, message = "Job title must not exceed 100 characters")
    private String jobTitle;

    // Constructors
    public EmployeeCreateRequestDto() {}

    public EmployeeCreateRequestDto(Long userId, String jobTitle) {
        this.userId = userId;
        this.jobTitle = jobTitle;
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
}

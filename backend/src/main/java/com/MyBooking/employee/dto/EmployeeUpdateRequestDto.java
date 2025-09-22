package com.MyBooking.employee.dto;

import com.MyBooking.employee.domain.EmployeeStatus;
import jakarta.validation.constraints.Size;

public class EmployeeUpdateRequestDto {
    @Size(max = 100, message = "Job title must not exceed 100 characters")
    private String jobTitle;
    
    private EmployeeStatus status;

    // Constructors
    public EmployeeUpdateRequestDto() {}

    public EmployeeUpdateRequestDto(String jobTitle, EmployeeStatus status) {
        this.jobTitle = jobTitle;
        this.status = status;
    }

    // Getters and Setters
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public EmployeeStatus getStatus() { return status; }
    public void setStatus(EmployeeStatus status) { this.status = status; }
}

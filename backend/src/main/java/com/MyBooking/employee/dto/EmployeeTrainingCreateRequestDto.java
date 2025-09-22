package com.MyBooking.employee.dto;

import jakarta.validation.constraints.NotNull;

public class EmployeeTrainingCreateRequestDto {
    @NotNull(message = "Employee ID is required")
    private Long employeeId;
    
    @NotNull(message = "Training ID is required")
    private Long trainingId;

    // Constructors
    public EmployeeTrainingCreateRequestDto() {}

    public EmployeeTrainingCreateRequestDto(Long employeeId, Long trainingId) {
        this.employeeId = employeeId;
        this.trainingId = trainingId;
    }

    // Getters and Setters
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public Long getTrainingId() { return trainingId; }
    public void setTrainingId(Long trainingId) { this.trainingId = trainingId; }
}

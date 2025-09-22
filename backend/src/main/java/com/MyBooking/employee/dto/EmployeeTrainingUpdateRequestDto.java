package com.MyBooking.employee.dto;

import com.MyBooking.employee.domain.TrainingStatus;
import jakarta.validation.constraints.NotNull;

public class EmployeeTrainingUpdateRequestDto {
    @NotNull(message = "Training status is required")
    private TrainingStatus status;

    // Constructors
    public EmployeeTrainingUpdateRequestDto() {}

    public EmployeeTrainingUpdateRequestDto(TrainingStatus status) {
        this.status = status;
    }

    // Getters and Setters
    public TrainingStatus getStatus() { return status; }
    public void setStatus(TrainingStatus status) { this.status = status; }
}

package com.MyBooking.employee.dto;

import com.MyBooking.employee.domain.TrainingStatus;

public class EmployeeTrainingSearchCriteriaDto {
    private Long employeeId;
    private Long trainingId;
    private TrainingStatus status;

    // Constructors
    public EmployeeTrainingSearchCriteriaDto() {}

    public EmployeeTrainingSearchCriteriaDto(Long employeeId, Long trainingId, TrainingStatus status) {
        this.employeeId = employeeId;
        this.trainingId = trainingId;
        this.status = status;
    }

    // Getters and Setters
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public Long getTrainingId() { return trainingId; }
    public void setTrainingId(Long trainingId) { this.trainingId = trainingId; }

    public TrainingStatus getStatus() { return status; }
    public void setStatus(TrainingStatus status) { this.status = status; }
}

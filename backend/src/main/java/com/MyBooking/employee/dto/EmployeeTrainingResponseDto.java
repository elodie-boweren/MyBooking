package com.MyBooking.employee.dto;

import com.MyBooking.employee.domain.TrainingStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class EmployeeTrainingResponseDto {
    private Long employeeId;
    private String employeeName;
    private String employeeEmail;
    private Long trainingId;
    private String trainingTitle;
    private LocalDate trainingStartDate;
    private LocalDate trainingEndDate;
    private TrainingStatus status;
    private LocalDateTime assignedAt;
    private LocalDateTime completedAt;

    // Constructors
    public EmployeeTrainingResponseDto() {}

    public EmployeeTrainingResponseDto(Long employeeId, String employeeName, String employeeEmail, 
                                     Long trainingId, String trainingTitle, LocalDate trainingStartDate, 
                                     LocalDate trainingEndDate, TrainingStatus status, 
                                     LocalDateTime assignedAt, LocalDateTime completedAt) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeEmail = employeeEmail;
        this.trainingId = trainingId;
        this.trainingTitle = trainingTitle;
        this.trainingStartDate = trainingStartDate;
        this.trainingEndDate = trainingEndDate;
        this.status = status;
        this.assignedAt = assignedAt;
        this.completedAt = completedAt;
    }

    // Getters and Setters
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getEmployeeEmail() { return employeeEmail; }
    public void setEmployeeEmail(String employeeEmail) { this.employeeEmail = employeeEmail; }

    public Long getTrainingId() { return trainingId; }
    public void setTrainingId(Long trainingId) { this.trainingId = trainingId; }

    public String getTrainingTitle() { return trainingTitle; }
    public void setTrainingTitle(String trainingTitle) { this.trainingTitle = trainingTitle; }

    public LocalDate getTrainingStartDate() { return trainingStartDate; }
    public void setTrainingStartDate(LocalDate trainingStartDate) { this.trainingStartDate = trainingStartDate; }

    public LocalDate getTrainingEndDate() { return trainingEndDate; }
    public void setTrainingEndDate(LocalDate trainingEndDate) { this.trainingEndDate = trainingEndDate; }

    public TrainingStatus getStatus() { return status; }
    public void setStatus(TrainingStatus status) { this.status = status; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}

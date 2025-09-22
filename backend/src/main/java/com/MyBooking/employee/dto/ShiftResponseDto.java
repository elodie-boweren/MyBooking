package com.MyBooking.employee.dto;

import java.time.LocalDateTime;

public class ShiftResponseDto {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private String employeeEmail;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    // Constructors
    public ShiftResponseDto() {}

    public ShiftResponseDto(Long id, Long employeeId, String employeeName, String employeeEmail, 
                           LocalDateTime startAt, LocalDateTime endAt) {
        this.id = id;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeEmail = employeeEmail;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getEmployeeEmail() { return employeeEmail; }
    public void setEmployeeEmail(String employeeEmail) { this.employeeEmail = employeeEmail; }

    public LocalDateTime getStartAt() { return startAt; }
    public void setStartAt(LocalDateTime startAt) { this.startAt = startAt; }

    public LocalDateTime getEndAt() { return endAt; }
    public void setEndAt(LocalDateTime endAt) { this.endAt = endAt; }
}

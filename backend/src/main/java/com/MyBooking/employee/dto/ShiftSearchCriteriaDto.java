package com.MyBooking.employee.dto;

import java.time.LocalDateTime;

public class ShiftSearchCriteriaDto {
    private Long employeeId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    // Constructors
    public ShiftSearchCriteriaDto() {}

    public ShiftSearchCriteriaDto(Long employeeId, LocalDateTime startAt, LocalDateTime endAt) {
        this.employeeId = employeeId;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    // Getters and Setters
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public LocalDateTime getStartAt() { return startAt; }
    public void setStartAt(LocalDateTime startAt) { this.startAt = startAt; }

    public LocalDateTime getEndAt() { return endAt; }
    public void setEndAt(LocalDateTime endAt) { this.endAt = endAt; }
}

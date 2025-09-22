package com.MyBooking.employee.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ShiftCreateRequestDto {
    @NotNull(message = "Employee ID is required")
    private Long employeeId;
    
    @NotNull(message = "Shift start time is required")
    private LocalDateTime startAt;
    
    @NotNull(message = "Shift end time is required")
    private LocalDateTime endAt;

    // Constructors
    public ShiftCreateRequestDto() {}

    public ShiftCreateRequestDto(Long employeeId, LocalDateTime startAt, LocalDateTime endAt) {
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

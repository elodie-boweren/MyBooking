package com.MyBooking.employee.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ShiftUpdateRequestDto {
    @NotNull(message = "Shift start time is required")
    private LocalDateTime startAt;
    
    @NotNull(message = "Shift end time is required")
    private LocalDateTime endAt;

    // Constructors
    public ShiftUpdateRequestDto() {}

    public ShiftUpdateRequestDto(LocalDateTime startAt, LocalDateTime endAt) {
        this.startAt = startAt;
        this.endAt = endAt;
    }

    // Getters and Setters
    public LocalDateTime getStartAt() { return startAt; }
    public void setStartAt(LocalDateTime startAt) { this.startAt = startAt; }

    public LocalDateTime getEndAt() { return endAt; }
    public void setEndAt(LocalDateTime endAt) { this.endAt = endAt; }
}

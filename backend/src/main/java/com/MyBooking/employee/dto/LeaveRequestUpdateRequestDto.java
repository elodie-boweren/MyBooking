package com.MyBooking.employee.dto;

import com.MyBooking.employee.domain.LeaveRequestStatus;
import jakarta.validation.constraints.NotNull;

public class LeaveRequestUpdateRequestDto {
    @NotNull(message = "Leave request status is required")
    private LeaveRequestStatus status;

    // Constructors
    public LeaveRequestUpdateRequestDto() {}

    public LeaveRequestUpdateRequestDto(LeaveRequestStatus status) {
        this.status = status;
    }

    // Getters and Setters
    public LeaveRequestStatus getStatus() { return status; }
    public void setStatus(LeaveRequestStatus status) { this.status = status; }
}

package com.MyBooking.employee.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class LeaveRequestCreateRequestDto {
    @NotNull(message = "Leave start date is required")
    private LocalDate fromDate;
    
    @NotNull(message = "Leave end date is required")
    private LocalDate toDate;
    
    @Size(max = 500, message = "Reason must not exceed 500 characters")
    private String reason;

    // Constructors
    public LeaveRequestCreateRequestDto() {}

    public LeaveRequestCreateRequestDto(LocalDate fromDate, LocalDate toDate, String reason) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.reason = reason;
    }

    // Getters and Setters
    public LocalDate getFromDate() { return fromDate; }
    public void setFromDate(LocalDate fromDate) { this.fromDate = fromDate; }

    public LocalDate getToDate() { return toDate; }
    public void setToDate(LocalDate toDate) { this.toDate = toDate; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}

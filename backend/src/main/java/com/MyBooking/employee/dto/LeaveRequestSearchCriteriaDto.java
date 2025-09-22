package com.MyBooking.employee.dto;

import com.MyBooking.employee.domain.LeaveRequestStatus;
import java.time.LocalDate;

public class LeaveRequestSearchCriteriaDto {
    private Long employeeId;
    private LeaveRequestStatus status;
    private LocalDate fromDate;
    private LocalDate toDate;

    // Constructors
    public LeaveRequestSearchCriteriaDto() {}

    public LeaveRequestSearchCriteriaDto(Long employeeId, LeaveRequestStatus status, LocalDate fromDate, LocalDate toDate) {
        this.employeeId = employeeId;
        this.status = status;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    // Getters and Setters
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public LeaveRequestStatus getStatus() { return status; }
    public void setStatus(LeaveRequestStatus status) { this.status = status; }

    public LocalDate getFromDate() { return fromDate; }
    public void setFromDate(LocalDate fromDate) { this.fromDate = fromDate; }

    public LocalDate getToDate() { return toDate; }
    public void setToDate(LocalDate toDate) { this.toDate = toDate; }
}

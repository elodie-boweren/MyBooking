package com.MyBooking.employee.dto;

import com.MyBooking.employee.domain.LeaveRequestStatus;
import java.time.LocalDate;

public class LeaveRequestResponseDto {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private String employeeEmail;
    private LocalDate fromDate;
    private LocalDate toDate;
    private LeaveRequestStatus status;
    private String reason;

    // Constructors
    public LeaveRequestResponseDto() {}

    public LeaveRequestResponseDto(Long id, Long employeeId, String employeeName, String employeeEmail, 
                                  LocalDate fromDate, LocalDate toDate, LeaveRequestStatus status, String reason) {
        this.id = id;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeEmail = employeeEmail;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.status = status;
        this.reason = reason;
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

    public LocalDate getFromDate() { return fromDate; }
    public void setFromDate(LocalDate fromDate) { this.fromDate = fromDate; }

    public LocalDate getToDate() { return toDate; }
    public void setToDate(LocalDate toDate) { this.toDate = toDate; }

    public LeaveRequestStatus getStatus() { return status; }
    public void setStatus(LeaveRequestStatus status) { this.status = status; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}

package com.MyBooking.employee.dto;

public class EmployeeAvailabilityDto {
    private Long employeeId;
    private String employeeName;
    private boolean available;
    private String reason; // "ACTIVE", "INACTIVE", "IN_TRAINING", "ON_LEAVE"

    // Constructors
    public EmployeeAvailabilityDto() {}

    public EmployeeAvailabilityDto(Long employeeId, String employeeName, boolean available, String reason) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.available = available;
        this.reason = reason;
    }

    // Getters and Setters
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}

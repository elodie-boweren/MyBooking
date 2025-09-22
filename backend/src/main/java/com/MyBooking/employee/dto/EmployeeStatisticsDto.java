package com.MyBooking.employee.dto;

public class EmployeeStatisticsDto {
    private long totalEmployees;
    private long activeEmployees;
    private long inactiveEmployees;
    private long pendingLeaveRequests;
    private long approvedLeaveRequests;
    private long inProgressTrainings;
    private long completedTrainings;

    // Constructors
    public EmployeeStatisticsDto() {}

    public EmployeeStatisticsDto(long totalEmployees, long activeEmployees, long inactiveEmployees,
                                long pendingLeaveRequests, long approvedLeaveRequests,
                                long inProgressTrainings, long completedTrainings) {
        this.totalEmployees = totalEmployees;
        this.activeEmployees = activeEmployees;
        this.inactiveEmployees = inactiveEmployees;
        this.pendingLeaveRequests = pendingLeaveRequests;
        this.approvedLeaveRequests = approvedLeaveRequests;
        this.inProgressTrainings = inProgressTrainings;
        this.completedTrainings = completedTrainings;
    }

    // Getters and Setters
    public long getTotalEmployees() { return totalEmployees; }
    public void setTotalEmployees(long totalEmployees) { this.totalEmployees = totalEmployees; }

    public long getActiveEmployees() { return activeEmployees; }
    public void setActiveEmployees(long activeEmployees) { this.activeEmployees = activeEmployees; }

    public long getInactiveEmployees() { return inactiveEmployees; }
    public void setInactiveEmployees(long inactiveEmployees) { this.inactiveEmployees = inactiveEmployees; }

    public long getPendingLeaveRequests() { return pendingLeaveRequests; }
    public void setPendingLeaveRequests(long pendingLeaveRequests) { this.pendingLeaveRequests = pendingLeaveRequests; }

    public long getApprovedLeaveRequests() { return approvedLeaveRequests; }
    public void setApprovedLeaveRequests(long approvedLeaveRequests) { this.approvedLeaveRequests = approvedLeaveRequests; }

    public long getInProgressTrainings() { return inProgressTrainings; }
    public void setInProgressTrainings(long inProgressTrainings) { this.inProgressTrainings = inProgressTrainings; }

    public long getCompletedTrainings() { return completedTrainings; }
    public void setCompletedTrainings(long completedTrainings) { this.completedTrainings = completedTrainings; }
}

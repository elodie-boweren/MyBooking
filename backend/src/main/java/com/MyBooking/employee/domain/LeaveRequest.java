package com.MyBooking.employee.domain;

import com.MyBooking.auth.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "leave_request")
public class LeaveRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_user_id", nullable = false)
    @NotNull(message = "Employee is required")
    private User employee;
    
    @NotNull(message = "Leave start date is required")
    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;
    
    @NotNull(message = "Leave end date is required")
    @Column(name = "to_date", nullable = false)
    private LocalDate toDate;
    
    @NotNull(message = "Leave request status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private LeaveRequestStatus status;
    
    @Size(max = 500, message = "Reason must not exceed 500 characters")
    @Column(name = "reason", length = 500)
    private String reason;
    
    // Constructors
    public LeaveRequest() {}
    
    public LeaveRequest(User employee, LocalDate fromDate, LocalDate toDate, LeaveRequestStatus status, String reason) {
        this.employee = employee;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.status = status;
        this.reason = reason;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getEmployee() { return employee; }
    public void setEmployee(User employee) { this.employee = employee; }
    
    public LocalDate getFromDate() { return fromDate; }
    public void setFromDate(LocalDate fromDate) { this.fromDate = fromDate; }
    
    public LocalDate getToDate() { return toDate; }
    public void setToDate(LocalDate toDate) { this.toDate = toDate; }
    
    public LeaveRequestStatus getStatus() { return status; }
    public void setStatus(LeaveRequestStatus status) { this.status = status; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
package com.MyBooking.employee.dto;

import com.MyBooking.employee.domain.EmployeeStatus;

public class EmployeeSearchCriteriaDto {
    private EmployeeStatus status;
    private String jobTitle;
    private String firstName;
    private String lastName;
    private String email;

    // Constructors
    public EmployeeSearchCriteriaDto() {}

    public EmployeeSearchCriteriaDto(EmployeeStatus status, String jobTitle, String firstName, String lastName, String email) {
        this.status = status;
        this.jobTitle = jobTitle;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Getters and Setters
    public EmployeeStatus getStatus() { return status; }
    public void setStatus(EmployeeStatus status) { this.status = status; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

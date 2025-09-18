package com.MyBooking.employee.domain;

import com.MyBooking.auth.domain.User;
import java.io.Serializable;
import java.util.Objects;

/**
 * Composite primary key for EmployeeTraining entity.
 * Represents the relationship between an employee and a training.
 */
public class EmployeeTrainingId implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private User employee;
    private Training training;
    
    // Default constructor
    public EmployeeTrainingId() {}
    
    // Constructor with parameters
    public EmployeeTrainingId(User employee, Training training) {
        this.employee = employee;
        this.training = training;
    }
    
    // Getters and setters
    public User getEmployee() {
        return employee;
    }
    
    public void setEmployee(User employee) {
        this.employee = employee;
    }
    
    public Training getTraining() {
        return training;
    }
    
    public void setTraining(Training training) {
        this.training = training;
    }
    
    // equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeTrainingId that = (EmployeeTrainingId) o;
        return Objects.equals(employee, that.employee) &&
               Objects.equals(training, that.training);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(employee, training);
    }
    
    @Override
    public String toString() {
        return "EmployeeTrainingId{" +
               "employee=" + employee +
               ", training=" + training +
               '}';
    }
}

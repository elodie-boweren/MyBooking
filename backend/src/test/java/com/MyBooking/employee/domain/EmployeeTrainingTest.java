package com.MyBooking.employee.domain;

import com.MyBooking.auth.domain.Role;
import com.MyBooking.auth.domain.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTrainingTest {
    
    private Validator validator;
    private EmployeeTraining employeeTraining;
    private User employee;
    private Training training;
    
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        // Create test employee
        employee = new User();
        employee.setId(1L);
        employee.setEmail("employee@hotel.com");
        employee.setPassword("password123");
        employee.setRole(Role.EMPLOYEE);
        
        // Create test training
        training = new Training(
            "Customer Service Training",
            LocalDate.of(2024, 1, 15),
            LocalDate.of(2024, 1, 20)
        );
        training.setId(1L);
        
        // Create test employee training
        employeeTraining = new EmployeeTraining(employee, training, TrainingStatus.ASSIGNED);
    }
    
    @Test
    void testDefaultConstructor() {
        EmployeeTraining newEmployeeTraining = new EmployeeTraining();
        assertNotNull(newEmployeeTraining);
        assertNull(newEmployeeTraining.getEmployee());
        assertNull(newEmployeeTraining.getTraining());
        assertNull(newEmployeeTraining.getStatus());
        assertNull(newEmployeeTraining.getAssignedAt());
        assertNull(newEmployeeTraining.getCompletedAt());
    }
    
    @Test
    void testParameterizedConstructor() {
        assertEquals(employee, employeeTraining.getEmployee());
        assertEquals(training, employeeTraining.getTraining());
        assertEquals(TrainingStatus.ASSIGNED, employeeTraining.getStatus());
    }
    
    @Test
    void testValidEmployeeTraining() {
        Set<ConstraintViolation<EmployeeTraining>> violations = validator.validate(employeeTraining);
        assertTrue(violations.isEmpty());
    }
    
    @Test
    void testEmployeeValidation() {
        employeeTraining.setEmployee(null);
        Set<ConstraintViolation<EmployeeTraining>> violations = validator.validate(employeeTraining);
        assertEquals(1, violations.size());
        assertEquals("Employee is required", violations.iterator().next().getMessage());
    }
    
    @Test
    void testTrainingValidation() {
        employeeTraining.setTraining(null);
        Set<ConstraintViolation<EmployeeTraining>> violations = validator.validate(employeeTraining);
        assertEquals(1, violations.size());
        assertEquals("Training is required", violations.iterator().next().getMessage());
    }
    
    @Test
    void testStatusValidation() {
        employeeTraining.setStatus(null);
        Set<ConstraintViolation<EmployeeTraining>> violations = validator.validate(employeeTraining);
        assertEquals(1, violations.size());
        assertEquals("Training status is required", violations.iterator().next().getMessage());
    }
    
    @Test
    void testGettersAndSetters() {
        // Test ID
        employeeTraining.setId(1L);
        assertEquals(1L, employeeTraining.getId());
        
        // Test employee
        User newEmployee = new User();
        newEmployee.setId(2L);
        employeeTraining.setEmployee(newEmployee);
        assertEquals(newEmployee, employeeTraining.getEmployee());
        
        // Test training
        Training newTraining = new Training("New Training", LocalDate.now(), LocalDate.now().plusDays(5));
        employeeTraining.setTraining(newTraining);
        assertEquals(newTraining, employeeTraining.getTraining());
        
        // Test status
        employeeTraining.setStatus(TrainingStatus.COMPLETED);
        assertEquals(TrainingStatus.COMPLETED, employeeTraining.getStatus());
        
        // Test assignedAt
        LocalDateTime assignedAt = LocalDateTime.now();
        employeeTraining.setAssignedAt(assignedAt);
        assertEquals(assignedAt, employeeTraining.getAssignedAt());
        
        // Test completedAt
        LocalDateTime completedAt = LocalDateTime.now();
        employeeTraining.setCompletedAt(completedAt);
        assertEquals(completedAt, employeeTraining.getCompletedAt());
    }
    
    @Test
    void testTrainingStatusEnumValues() {
        // Test ASSIGNED status
        employeeTraining.setStatus(TrainingStatus.ASSIGNED);
        assertEquals(TrainingStatus.ASSIGNED, employeeTraining.getStatus());
        
        // Test COMPLETED status
        employeeTraining.setStatus(TrainingStatus.COMPLETED);
        assertEquals(TrainingStatus.COMPLETED, employeeTraining.getStatus());
    }
}

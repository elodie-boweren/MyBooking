package com.MyBooking.employee.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TrainingTest {
    
    private Validator validator;
    private Training training;
    
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        training = new Training(
            "Customer Service Training",
            LocalDate.of(2024, 1, 15),
            LocalDate.of(2024, 1, 20)
        );
    }
    
    @Test
    void testDefaultConstructor() {
        Training newTraining = new Training();
        assertNotNull(newTraining);
        assertNull(newTraining.getTitle());
        assertNull(newTraining.getStartDate());
        assertNull(newTraining.getEndDate());
    }
    
    @Test
    void testParameterizedConstructor() {
        assertEquals("Customer Service Training", training.getTitle());
        assertEquals(LocalDate.of(2024, 1, 15), training.getStartDate());
        assertEquals(LocalDate.of(2024, 1, 20), training.getEndDate());
    }
    
    @Test
    void testValidTraining() {
        Set<ConstraintViolation<Training>> violations = validator.validate(training);
        assertTrue(violations.isEmpty());
    }
    
    @Test
    void testTitleValidation() {
        // Test null title
        training.setTitle(null);
        Set<ConstraintViolation<Training>> violations = validator.validate(training);
        assertEquals(1, violations.size());
        assertEquals("Training title is required", violations.iterator().next().getMessage());
        
        // Test blank title
        training.setTitle("");
        violations = validator.validate(training);
        assertEquals(1, violations.size());
        assertEquals("Training title is required", violations.iterator().next().getMessage());
        
        // Test title too long
        training.setTitle("a".repeat(201));
        violations = validator.validate(training);
        assertEquals(1, violations.size());
        assertEquals("Training title must not exceed 200 characters", violations.iterator().next().getMessage());
    }
    
    @Test
    void testStartDateValidation() {
        training.setStartDate(null);
        Set<ConstraintViolation<Training>> violations = validator.validate(training);
        assertEquals(1, violations.size());
        assertEquals("Training start date is required", violations.iterator().next().getMessage());
    }
    
    @Test
    void testEndDateValidation() {
        training.setEndDate(null);
        Set<ConstraintViolation<Training>> violations = validator.validate(training);
        assertEquals(1, violations.size());
        assertEquals("Training end date is required", violations.iterator().next().getMessage());
    }
    
    @Test
    void testGettersAndSetters() {
        // Test ID
        training.setId(1L);
        assertEquals(1L, training.getId());
        
        // Test title
        training.setTitle("New Training");
        assertEquals("New Training", training.getTitle());
        
        // Test start date
        LocalDate newStartDate = LocalDate.of(2024, 2, 1);
        training.setStartDate(newStartDate);
        assertEquals(newStartDate, training.getStartDate());
        
        // Test end date
        LocalDate newEndDate = LocalDate.of(2024, 2, 5);
        training.setEndDate(newEndDate);
        assertEquals(newEndDate, training.getEndDate());
    }
}
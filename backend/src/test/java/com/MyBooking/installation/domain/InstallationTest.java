package com.MyBooking.installation.domain;

import com.MyBooking.event.domain.Event;
import com.MyBooking.event.domain.EventType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InstallationTest {

    private Validator validator;
    private Installation installation;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        installation = new Installation();
        installation.setName("Luxury Spa Room");
        installation.setDescription("Premium spa facility with massage tables and aromatherapy");
        installation.setInstallationType(InstallationType.SPA_ROOM);
        installation.setCapacity(10);
        installation.setHourlyRate(new BigDecimal("50.00"));
        installation.setCurrency("EUR");
        installation.setEquipment("Massage tables, aromatherapy diffusers, relaxation chairs, towels");
    }

    @Test
    void testValidInstallation() {
        Set<ConstraintViolation<Installation>> violations = validator.validate(installation);
        assertTrue(violations.isEmpty(), "Valid installation should have no violations");
    }

    @Test
    void testNameValidation() {
        // Test null name
        installation.setName(null);
        Set<ConstraintViolation<Installation>> violations = validator.validate(installation);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Installation name is required")));

        // Test blank name
        installation.setName("");
        violations = validator.validate(installation);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Installation name is required")));

        // Test name too long
        installation.setName("A".repeat(101));
        violations = validator.validate(installation);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Installation name must not exceed 100 characters")));
    }

    @Test
    void testInstallationTypeValidation() {
        // Test null installation type
        installation.setInstallationType(null);
        Set<ConstraintViolation<Installation>> violations = validator.validate(installation);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Installation type is required")));
    }

    @Test
    void testCapacityValidation() {
        // Test null capacity
        installation.setCapacity(null);
        Set<ConstraintViolation<Installation>> violations = validator.validate(installation);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Capacity is required")));

        // Test capacity too low
        installation.setCapacity(0);
        violations = validator.validate(installation);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Capacity must be at least 1")));

        // Test capacity too high
        installation.setCapacity(201);
        violations = validator.validate(installation);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Capacity must not exceed 200")));
    }

    @Test
    void testHourlyRateValidation() {
        // Test null hourly rate
        installation.setHourlyRate(null);
        Set<ConstraintViolation<Installation>> violations = validator.validate(installation);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Hourly rate is required")));

        // Test zero hourly rate
        installation.setHourlyRate(BigDecimal.ZERO);
        violations = validator.validate(installation);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Hourly rate must be greater than 0")));

        // Test negative hourly rate
        installation.setHourlyRate(new BigDecimal("-10.00"));
        violations = validator.validate(installation);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Hourly rate must be greater than 0")));
    }

    @Test
    void testCurrencyValidation() {
        // Test null currency
        installation.setCurrency(null);
        Set<ConstraintViolation<Installation>> violations = validator.validate(installation);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Currency is required")));

        // Test blank currency
        installation.setCurrency("");
        violations = validator.validate(installation);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Currency is required")));

        // Test currency too short
        installation.setCurrency("EU");
        violations = validator.validate(installation);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Currency must be exactly 3 characters")));

        // Test currency too long
        installation.setCurrency("EURO");
        violations = validator.validate(installation);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Currency must be exactly 3 characters")));
    }

    @Test
    void testDescriptionValidation() {
        // Test description too long
        installation.setDescription("A".repeat(501));
        Set<ConstraintViolation<Installation>> violations = validator.validate(installation);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Description must not exceed 500 characters")));
    }

    @Test
    void testEquipmentValidation() {
        // Test equipment too long
        installation.setEquipment("A".repeat(256));
        Set<ConstraintViolation<Installation>> violations = validator.validate(installation);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Equipment must not exceed 255 characters")));
    }

    @Test
    void testFormattedHourlyRate() {
        installation.setCurrency("USD");
        installation.setHourlyRate(new BigDecimal("75.50"));
        assertEquals("USD 75.50/hour", installation.getFormattedHourlyRate());
    }

    @Test
    void testConstructor() {
        Installation newInstallation = new Installation("Conference Hall", InstallationType.CONFERENCE_ROOM, 
                50, new BigDecimal("100.00"), "EUR");
        
        assertEquals("Conference Hall", newInstallation.getName());
        assertEquals(InstallationType.CONFERENCE_ROOM, newInstallation.getInstallationType());
        assertEquals(50, newInstallation.getCapacity());
        assertEquals(new BigDecimal("100.00"), newInstallation.getHourlyRate());
        assertEquals("EUR", newInstallation.getCurrency());
    }

    @Test
    void testInstallationTypeEnumValues() {
        // Test all installation type enum values
        assertEquals(6, InstallationType.values().length);
        assertTrue(Set.of(InstallationType.values()).contains(InstallationType.SPA_ROOM));
        assertTrue(Set.of(InstallationType.values()).contains(InstallationType.CONFERENCE_ROOM));
        assertTrue(Set.of(InstallationType.values()).contains(InstallationType.GYM));
        assertTrue(Set.of(InstallationType.values()).contains(InstallationType.POOL));
        assertTrue(Set.of(InstallationType.values()).contains(InstallationType.TENNIS_COURT));
        assertTrue(Set.of(InstallationType.values()).contains(InstallationType.WEDDING_ROOM));
    }

    @Test
    void testEventRelationship() {
        // Test basic relationship (without business logic methods)
        Event event = new Event("Morning Yoga", EventType.YOGA_CLASS, 
                LocalDateTime.now().plusDays(1).withHour(9).withMinute(0),
                LocalDateTime.now().plusDays(1).withHour(10).withMinute(0),
                15, new BigDecimal("25.00"), "EUR", installation);
        
        // Test that we can set the relationship
        event.setInstallation(installation);
        assertEquals(installation, event.getInstallation());
        
        // Test that installation can have events (basic getter/setter)
        installation.getEvents().add(event);
        assertTrue(installation.getEvents().contains(event));
    }
}
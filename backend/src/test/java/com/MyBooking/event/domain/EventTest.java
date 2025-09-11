package com.MyBooking.event.domain;

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

class EventTest {

    private Validator validator;
    private Event event;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        event = new Event();
        event.setName("Morning Yoga Session");
        event.setDescription("Relaxing yoga class for all levels");
        event.setEventType(EventType.YOGA_CLASS);
        event.setStartAt(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0));
        event.setEndAt(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0));
        event.setCapacity(20);
        event.setPrice(new BigDecimal("25.00"));
        event.setCurrency("EUR");
    }

    @Test
    void testValidEvent() {
        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        assertTrue(violations.isEmpty(), "Valid event should have no violations");
    }

    @Test
    void testNameValidation() {
        // Test null name
        event.setName(null);
        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Event name is required")));

        // Test blank name
        event.setName("");
        violations = validator.validate(event);
        assertFalse(violations.isEmpty());

        // Test too long name
        event.setName("A".repeat(101));
        violations = validator.validate(event);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("must not exceed 100 characters")));
    }

    @Test
    void testEventTypeValidation() {
        // Test null event type
        event.setEventType(null);
        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Event type is required")));
    }

    @Test
    void testStartAtValidation() {
        // Test null start time
        event.setStartAt(null);
        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Start time is required")));
    }

    @Test
    void testEndAtValidation() {
        // Test null end time
        event.setEndAt(null);
        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("End time is required")));
    }

    @Test
    void testCapacityValidation() {
        // Test null capacity
        event.setCapacity(null);
        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Capacity is required")));

        // Test capacity too low
        event.setCapacity(0);
        violations = validator.validate(event);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Capacity must be at least 1")));

        // Test capacity too high
        event.setCapacity(101);
        violations = validator.validate(event);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("must not exceed 100")));
    }

    @Test
    void testPriceValidation() {
        // Test null price
        event.setPrice(null);
        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Price is required")));

        // Test zero price
        event.setPrice(BigDecimal.ZERO);
        violations = validator.validate(event);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Price must be greater than 0")));
    }

    @Test
    void testCurrencyValidation() {
        // Test null currency
        event.setCurrency(null);
        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Currency is required")));

        // Test invalid currency length
        event.setCurrency("EU");
        violations = validator.validate(event);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Currency must be exactly 3 characters")));
    }

    @Test
    void testDescriptionValidation() {
        // Test too long description
        event.setDescription("A".repeat(501));
        Set<ConstraintViolation<Event>> violations = validator.validate(event);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Description must not exceed 500 characters")));

        // Test valid description
        event.setDescription("A".repeat(500));
        violations = validator.validate(event);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testGetFormattedPrice() {
        event.setCurrency("EUR");
        event.setPrice(new BigDecimal("25.00"));
        assertEquals("EUR 25.00", event.getFormattedPrice());

        event.setCurrency("USD");
        event.setPrice(new BigDecimal("30.50"));
        assertEquals("USD 30.50", event.getFormattedPrice());
    }

    @Test
    void testConstructor() {
        Event newEvent = new Event("Spa Treatment", EventType.SPA, 
                LocalDateTime.now().plusDays(2).withHour(14).withMinute(0),
                LocalDateTime.now().plusDays(2).withHour(16).withMinute(0),
                10, new BigDecimal("80.00"), "EUR");
        
        assertEquals("Spa Treatment", newEvent.getName());
        assertEquals(EventType.SPA, newEvent.getEventType());
        assertEquals(10, newEvent.getCapacity());
        assertEquals(new BigDecimal("80.00"), newEvent.getPrice());
        assertEquals("EUR", newEvent.getCurrency());
    }

    @Test
    void testEventTypeEnumValues() {
        // Test all event type enum values
        assertEquals(5, EventType.values().length);
        assertTrue(Set.of("SPA", "CONFERENCE", "YOGA_CLASS", "FITNESS", "WEDDING")
                .containsAll(Set.of(EventType.values()).stream()
                        .map(Enum::name)
                        .collect(java.util.stream.Collectors.toSet())));
    }
}
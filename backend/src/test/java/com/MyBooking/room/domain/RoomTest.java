package com.MyBooking.room.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    private Validator validator;
    private Room room;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        room = new Room();
        room.setNumber("101");
        room.setRoomType(RoomType.SINGLE);
        room.setCapacity(2);
        room.setPrice(new BigDecimal("150.00"));
        room.setCurrency("EUR");
        room.setStatus(RoomStatus.AVAILABLE);
        room.setDescription("Comfortable single room");
        room.setEquipment("WiFi, TV, Mini-bar");
    }

    @Test
    void testValidRoom() {
        Set<ConstraintViolation<Room>> violations = validator.validate(room);
        assertTrue(violations.isEmpty(), "Valid room should have no violations");
    }

    @Test
    void testNumberValidation() {
        // Test null number
        room.setNumber(null);
        Set<ConstraintViolation<Room>> violations = validator.validate(room);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Room number is required")));

        // Test blank number
        room.setNumber("");
        violations = validator.validate(room);
        assertFalse(violations.isEmpty());

        // Test too long number
        room.setNumber("A".repeat(21));
        violations = validator.validate(room);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("must not exceed 20 characters")));
    }

    @Test
    void testRoomTypeValidation() {
        // Test null room type
        room.setRoomType(null);
        Set<ConstraintViolation<Room>> violations = validator.validate(room);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Room type is required")));
    }

    @Test
    void testCapacityValidation() {
        // Test null capacity
        room.setCapacity(null);
        Set<ConstraintViolation<Room>> violations = validator.validate(room);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Capacity is required")));

        // Test capacity too low
        room.setCapacity(0);
        violations = validator.validate(room);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Capacity must be at least 1")));

        // Test capacity too high
        room.setCapacity(11);
        violations = validator.validate(room);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("must not exceed 10")));
    }

    @Test
    void testPriceValidation() {
        // Test null price
        room.setPrice(null);
        Set<ConstraintViolation<Room>> violations = validator.validate(room);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Price is required")));

        // Test zero price
        room.setPrice(BigDecimal.ZERO);
        violations = validator.validate(room);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Price must be greater than 0")));
    }

    @Test
    void testCurrencyValidation() {
        // Test null currency
        room.setCurrency(null);
        Set<ConstraintViolation<Room>> violations = validator.validate(room);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Currency is required")));

        // Test invalid currency length
        room.setCurrency("EU");
        violations = validator.validate(room);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Currency must be exactly 3 characters")));

        // Test too long currency
        room.setCurrency("EURO");
        violations = validator.validate(room);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testStatusValidation() {
        // Test null status
        room.setStatus(null);
        Set<ConstraintViolation<Room>> violations = validator.validate(room);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Status is required")));
    }

    @Test
    void testDescriptionValidation() {
        // Test too long description
        room.setDescription("A".repeat(501));
        Set<ConstraintViolation<Room>> violations = validator.validate(room);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Description must not exceed 500 characters")));

        // Test valid description
        room.setDescription("A".repeat(500));
        violations = validator.validate(room);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testEquipmentValidation() {
        // Test too long equipment
        room.setEquipment("A".repeat(256));
        Set<ConstraintViolation<Room>> violations = validator.validate(room);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Equipment must not exceed 255 characters")));

        // Test valid equipment
        room.setEquipment("A".repeat(255));
        violations = validator.validate(room);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUtilityMethods() {
        // Test status checking methods
        room.setStatus(RoomStatus.AVAILABLE);
        assertTrue(room.isAvailable());
        assertFalse(room.isOccupied());
        assertFalse(room.isOutOfService());

        room.setStatus(RoomStatus.OCCUPIED);
        assertFalse(room.isAvailable());
        assertTrue(room.isOccupied());
        assertFalse(room.isOutOfService());

        room.setStatus(RoomStatus.OUT_OF_SERVICE);
        assertFalse(room.isAvailable());
        assertFalse(room.isOccupied());
        assertTrue(room.isOutOfService());
    }

    @Test
    void testGetFormattedPrice() {
        room.setCurrency("EUR");
        room.setPrice(new BigDecimal("150.00"));
        assertEquals("EUR 150.00", room.getFormattedPrice());

        room.setCurrency("USD");
        room.setPrice(new BigDecimal("200.50"));
        assertEquals("USD 200.50", room.getFormattedPrice());
    }

    @Test
    void testConstructor() {
        Room newRoom = new Room("102", RoomType.DOUBLE, 4, new BigDecimal("200.00"),
                "USD", RoomStatus.AVAILABLE);
        
        assertEquals("102", newRoom.getNumber());
        assertEquals(RoomType.DOUBLE, newRoom.getRoomType());
        assertEquals(4, newRoom.getCapacity());
        assertEquals(new BigDecimal("200.00"), newRoom.getPrice());
        assertEquals("USD", newRoom.getCurrency());
        assertEquals(RoomStatus.AVAILABLE, newRoom.getStatus());
    }

    @Test
    void testRoomTypeEnumValues() {
        // Test all room type enum values
        assertEquals(4, RoomType.values().length);
        assertTrue(Set.of("SINGLE", "DOUBLE", "DELUXE", "FAMILY")
                .containsAll(Set.of(RoomType.values()).stream()
                        .map(Enum::name)
                        .collect(java.util.stream.Collectors.toSet())));
    }

    @Test
    void testRoomStatusEnumValues() {
        // Test all room status enum values
        assertEquals(3, RoomStatus.values().length);
        assertTrue(Set.of("AVAILABLE", "OCCUPIED", "OUT_OF_SERVICE")
                .containsAll(Set.of(RoomStatus.values()).stream()
                        .map(Enum::name)
                        .collect(java.util.stream.Collectors.toSet())));
    }
}
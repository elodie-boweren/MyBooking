package com.MyBooking.event.domain;

import com.MyBooking.auth.domain.Role;
import com.MyBooking.auth.domain.User;
import com.MyBooking.installation.domain.Installation;
import com.MyBooking.installation.domain.InstallationType;
import com.MyBooking.reservation.domain.Reservation;
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

class EventBookingTest {
    
    private Validator validator;
    private EventBooking eventBooking;
    private Event event;
    private User client;
    private Installation installation;
    
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        // Create test installation
        installation = new Installation();
        installation.setId(1L);
        installation.setName("Spa Room");
        installation.setInstallationType(InstallationType.SPA_ROOM);
        installation.setHourlyRate(new BigDecimal("50.00"));
        
        // Create test event
        event = new Event(
            "Relaxing Spa Session",
            EventType.SPA,
            LocalDateTime.of(2024, 1, 15, 14, 0),
            LocalDateTime.of(2024, 1, 15, 16, 0),
            10,
            new BigDecimal("100.00"),
            "EUR",
            installation
        );
        event.setId(1L);
        
        // Create test client
        client = new User();
        client.setId(1L);
        client.setEmail("client@hotel.com");
        client.setPassword("password123");
        client.setRole(Role.CLIENT);
        
        // Create test event booking
        eventBooking = new EventBooking(
            event,
            client,
            LocalDateTime.of(2024, 1, 15, 14, 0), // Event date/time
            2, // Duration in hours
            LocalDateTime.of(2024, 1, 10, 9, 30), // Booking date
            1, // Number of participants
            new BigDecimal("100.00"), // Total price
            EventBookingStatus.CONFIRMED
        );
    }
    
    @Test
    void testDefaultConstructor() {
        EventBooking newBooking = new EventBooking();
        assertNotNull(newBooking);
        assertNull(newBooking.getEvent());
        assertNull(newBooking.getClient());
        assertNull(newBooking.getReservation());
        assertNull(newBooking.getEventDateTime());
        assertNull(newBooking.getDurationHours());
        assertNull(newBooking.getBookingDate());
        assertNull(newBooking.getNumberOfParticipants());
        assertNull(newBooking.getTotalPrice());
        assertNull(newBooking.getStatus());
    }
    
    @Test
    void testParameterizedConstructor() {
        assertEquals(event, eventBooking.getEvent());
        assertEquals(client, eventBooking.getClient());
        assertEquals(LocalDateTime.of(2024, 1, 15, 14, 0), eventBooking.getEventDateTime());
        assertEquals(2, eventBooking.getDurationHours());
        assertEquals(LocalDateTime.of(2024, 1, 10, 9, 30), eventBooking.getBookingDate());
        assertEquals(1, eventBooking.getNumberOfParticipants());
        assertEquals(new BigDecimal("100.00"), eventBooking.getTotalPrice());
        assertEquals(EventBookingStatus.CONFIRMED, eventBooking.getStatus());
    }
    
    @Test
    void testValidEventBooking() {
        Set<ConstraintViolation<EventBooking>> violations = validator.validate(eventBooking);
        assertTrue(violations.isEmpty());
    }
    
    @Test
    void testEventValidation() {
        eventBooking.setEvent(null);
        Set<ConstraintViolation<EventBooking>> violations = validator.validate(eventBooking);
        assertEquals(1, violations.size());
        assertEquals("Event is required", violations.iterator().next().getMessage());
    }
    
    @Test
    void testClientValidation() {
        eventBooking.setClient(null);
        Set<ConstraintViolation<EventBooking>> violations = validator.validate(eventBooking);
        assertEquals(1, violations.size());
        assertEquals("Client is required", violations.iterator().next().getMessage());
    }
    
    @Test
    void testEventDateTimeValidation() {
        eventBooking.setEventDateTime(null);
        Set<ConstraintViolation<EventBooking>> violations = validator.validate(eventBooking);
        assertEquals(1, violations.size());
        assertEquals("Event date and time is required", violations.iterator().next().getMessage());
    }
    
    @Test
    void testDurationHoursValidation() {
        eventBooking.setDurationHours(null);
        Set<ConstraintViolation<EventBooking>> violations = validator.validate(eventBooking);
        assertEquals(1, violations.size());
        assertEquals("Event duration is required", violations.iterator().next().getMessage());
        
        // Test negative duration
        eventBooking.setDurationHours(-1);
        violations = validator.validate(eventBooking);
        assertEquals(1, violations.size());
        assertEquals("Event duration must be positive", violations.iterator().next().getMessage());
    }
    
    @Test
    void testBookingDateValidation() {
        eventBooking.setBookingDate(null);
        Set<ConstraintViolation<EventBooking>> violations = validator.validate(eventBooking);
        assertEquals(1, violations.size());
        assertEquals("Booking date is required", violations.iterator().next().getMessage());
    }
    
    @Test
    void testNumberOfParticipantsValidation() {
        eventBooking.setNumberOfParticipants(null);
        Set<ConstraintViolation<EventBooking>> violations = validator.validate(eventBooking);
        assertEquals(1, violations.size());
        assertEquals("Number of participants is required", violations.iterator().next().getMessage());
        
        // Test negative participants
        eventBooking.setNumberOfParticipants(-1);
        violations = validator.validate(eventBooking);
        assertEquals(1, violations.size());
        assertEquals("Number of participants must be positive", violations.iterator().next().getMessage());
    }
    
    @Test
    void testTotalPriceValidation() {
        eventBooking.setTotalPrice(null);
        Set<ConstraintViolation<EventBooking>> violations = validator.validate(eventBooking);
        assertEquals(1, violations.size());
        assertEquals("Total price is required", violations.iterator().next().getMessage());
        
        // Test negative price
        eventBooking.setTotalPrice(new BigDecimal("-10.00"));
        violations = validator.validate(eventBooking);
        assertEquals(1, violations.size());
        assertEquals("Total price must be positive", violations.iterator().next().getMessage());
    }
    
    @Test
    void testStatusValidation() {
        eventBooking.setStatus(null);
        Set<ConstraintViolation<EventBooking>> violations = validator.validate(eventBooking);
        assertEquals(1, violations.size());
        assertEquals("Booking status is required", violations.iterator().next().getMessage());
    }
    
    @Test
    void testGettersAndSetters() {
        // Test ID
        eventBooking.setId(1L);
        assertEquals(1L, eventBooking.getId());
        
        // Test event
        Event newEvent = new Event();
        newEvent.setId(2L);
        eventBooking.setEvent(newEvent);
        assertEquals(newEvent, eventBooking.getEvent());
        
        // Test client
        User newClient = new User();
        newClient.setId(2L);
        eventBooking.setClient(newClient);
        assertEquals(newClient, eventBooking.getClient());
        
        // Test reservation
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        eventBooking.setReservation(reservation);
        assertEquals(reservation, eventBooking.getReservation());
        
        // Test event date/time
        LocalDateTime newEventDateTime = LocalDateTime.of(2024, 2, 1, 10, 0);
        eventBooking.setEventDateTime(newEventDateTime);
        assertEquals(newEventDateTime, eventBooking.getEventDateTime());
        
        // Test duration
        eventBooking.setDurationHours(3);
        assertEquals(3, eventBooking.getDurationHours());
        
        // Test booking date
        LocalDateTime newBookingDate = LocalDateTime.of(2024, 1, 20, 8, 0);
        eventBooking.setBookingDate(newBookingDate);
        assertEquals(newBookingDate, eventBooking.getBookingDate());
        
        // Test number of participants
        eventBooking.setNumberOfParticipants(2);
        assertEquals(2, eventBooking.getNumberOfParticipants());
        
        // Test total price
        BigDecimal newPrice = new BigDecimal("150.00");
        eventBooking.setTotalPrice(newPrice);
        assertEquals(newPrice, eventBooking.getTotalPrice());
        
        // Test status
        eventBooking.setStatus(EventBookingStatus.CANCELLED);
        assertEquals(EventBookingStatus.CANCELLED, eventBooking.getStatus());
    }
    
    @Test
    void testEventBookingStatusEnumValues() {
        // Test CONFIRMED status
        eventBooking.setStatus(EventBookingStatus.CONFIRMED);
        assertEquals(EventBookingStatus.CONFIRMED, eventBooking.getStatus());
        
        // Test CANCELLED status
        eventBooking.setStatus(EventBookingStatus.CANCELLED);
        assertEquals(EventBookingStatus.CANCELLED, eventBooking.getStatus());
    }
}

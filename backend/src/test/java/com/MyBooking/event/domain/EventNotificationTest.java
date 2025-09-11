package com.MyBooking.event.domain;

import com.MyBooking.auth.domain.Role;
import com.MyBooking.auth.domain.User;
import com.MyBooking.installation.domain.Installation;
import com.MyBooking.installation.domain.InstallationType;
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

class EventNotificationTest {
    
    private Validator validator;
    private EventNotification eventNotification;
    private EventBooking eventBooking;
    private User client;
    private Event event;
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
            LocalDateTime.of(2024, 1, 15, 14, 0),
            2,
            LocalDateTime.of(2024, 1, 10, 9, 30),
            1,
            new BigDecimal("100.00"),
            EventBookingStatus.CONFIRMED
        );
        eventBooking.setId(1L);
        
        // Create test event notification
        eventNotification = new EventNotification(
            eventBooking,
            client,
            "Your spa session is confirmed for January 15, 2024 at 2:00 PM",
            EventNotificationType.BOOKING_CONFIRMATION
        );
    }
    
    @Test
    void testDefaultConstructor() {
        EventNotification newNotification = new EventNotification();
        assertNotNull(newNotification);
        assertNull(newNotification.getEventBooking());
        assertNull(newNotification.getUser());
        assertNull(newNotification.getMessage());
        assertNull(newNotification.getType());
        assertNull(newNotification.getSentAt());
    }
    
    @Test
    void testParameterizedConstructor() {
        assertEquals(eventBooking, eventNotification.getEventBooking());
        assertEquals(client, eventNotification.getUser());
        assertEquals("Your spa session is confirmed for January 15, 2024 at 2:00 PM", eventNotification.getMessage());
        assertEquals(EventNotificationType.BOOKING_CONFIRMATION, eventNotification.getType());
    }
    
    @Test
    void testValidEventNotification() {
        Set<ConstraintViolation<EventNotification>> violations = validator.validate(eventNotification);
        assertTrue(violations.isEmpty());
    }
    
    @Test
    void testEventBookingValidation() {
        eventNotification.setEventBooking(null);
        Set<ConstraintViolation<EventNotification>> violations = validator.validate(eventNotification);
        assertEquals(1, violations.size());
        assertEquals("Event booking is required", violations.iterator().next().getMessage());
    }
    
    @Test
    void testUserValidation() {
        eventNotification.setUser(null);
        Set<ConstraintViolation<EventNotification>> violations = validator.validate(eventNotification);
        assertEquals(1, violations.size());
        assertEquals("User is required", violations.iterator().next().getMessage());
    }
    
    @Test
    void testMessageValidation() {
        // Test null message
        eventNotification.setMessage(null);
        Set<ConstraintViolation<EventNotification>> violations = validator.validate(eventNotification);
        assertEquals(1, violations.size());
        assertEquals("Notification message is required", violations.iterator().next().getMessage());
        
        // Test blank message
        eventNotification.setMessage("");
        violations = validator.validate(eventNotification);
        assertEquals(1, violations.size());
        assertEquals("Notification message is required", violations.iterator().next().getMessage());
        
        // Test message too long
        eventNotification.setMessage("a".repeat(501));
        violations = validator.validate(eventNotification);
        assertEquals(1, violations.size());
        assertEquals("Notification message must not exceed 500 characters", violations.iterator().next().getMessage());
    }
    
    @Test
    void testTypeValidation() {
        eventNotification.setType(null);
        Set<ConstraintViolation<EventNotification>> violations = validator.validate(eventNotification);
        assertEquals(1, violations.size());
        assertEquals("Notification type is required", violations.iterator().next().getMessage());
    }
    
    @Test
    void testGettersAndSetters() {
        // Test ID
        eventNotification.setId(1L);
        assertEquals(1L, eventNotification.getId());
        
        // Test event booking
        EventBooking newEventBooking = new EventBooking();
        newEventBooking.setId(2L);
        eventNotification.setEventBooking(newEventBooking);
        assertEquals(newEventBooking, eventNotification.getEventBooking());
        
        // Test user
        User newUser = new User();
        newUser.setId(2L);
        eventNotification.setUser(newUser);
        assertEquals(newUser, eventNotification.getUser());
        
        // Test message
        eventNotification.setMessage("New notification message");
        assertEquals("New notification message", eventNotification.getMessage());
        
        // Test type
        eventNotification.setType(EventNotificationType.EVENT_UPDATE);
        assertEquals(EventNotificationType.EVENT_UPDATE, eventNotification.getType());
        
        // Test sentAt
        LocalDateTime sentAt = LocalDateTime.now();
        eventNotification.setSentAt(sentAt);
        assertEquals(sentAt, eventNotification.getSentAt());
    }
    
    @Test
    void testEventNotificationTypeEnumValues() {
        // Test BOOKING_CONFIRMATION
        eventNotification.setType(EventNotificationType.BOOKING_CONFIRMATION);
        assertEquals(EventNotificationType.BOOKING_CONFIRMATION, eventNotification.getType());
        
        // Test BOOKING_CANCELLATION
        eventNotification.setType(EventNotificationType.BOOKING_CANCELLATION);
        assertEquals(EventNotificationType.BOOKING_CANCELLATION, eventNotification.getType());
        
        // Test EVENT_UPDATE
        eventNotification.setType(EventNotificationType.EVENT_UPDATE);
        assertEquals(EventNotificationType.EVENT_UPDATE, eventNotification.getType());
    }
}

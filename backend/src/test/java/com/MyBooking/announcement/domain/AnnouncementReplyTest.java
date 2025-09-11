package com.MyBooking.announcement.domain;

import com.MyBooking.auth.domain.Role;
import com.MyBooking.auth.domain.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AnnouncementReplyTest {
    
    private Validator validator;
    private AnnouncementReply announcementReply;
    private Announcement announcement;
    private User employee;
    
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        // Create test admin user for announcement
        User adminUser = new User();
        adminUser.setId(1L);
        adminUser.setEmail("admin@hotel.com");
        adminUser.setPassword("password123");
        adminUser.setRole(Role.ADMIN);
        
        // Create test announcement
        announcement = new Announcement(
            "Hotel Policy Update",
            "Please note the new check-in policy effective immediately.",
            adminUser,
            AnnouncementPriority.HIGH,
            AnnouncementStatus.PUBLISHED
        );
        announcement.setId(1L);
        
        // Create test employee
        employee = new User();
        employee.setId(2L);
        employee.setEmail("employee@hotel.com");
        employee.setPassword("password123");
        employee.setRole(Role.EMPLOYEE);
        
        // Create test announcement reply
        announcementReply = new AnnouncementReply(
            announcement,
            employee,
            "Thank you for the update. I have a question about the new policy."
        );
    }
    
    @Test
    void testDefaultConstructor() {
        AnnouncementReply newReply = new AnnouncementReply();
        assertNotNull(newReply);
        assertNull(newReply.getAnnouncement());
        assertNull(newReply.getUser());
        assertNull(newReply.getMessage());
        assertNull(newReply.getCreatedAt());
    }
    
    @Test
    void testParameterizedConstructor() {
        assertEquals(announcement, announcementReply.getAnnouncement());
        assertEquals(employee, announcementReply.getUser());
        assertEquals("Thank you for the update. I have a question about the new policy.", announcementReply.getMessage());
    }
    
    @Test
    void testValidAnnouncementReply() {
        Set<ConstraintViolation<AnnouncementReply>> violations = validator.validate(announcementReply);
        assertTrue(violations.isEmpty());
    }
    
    @Test
    void testAnnouncementValidation() {
        announcementReply.setAnnouncement(null);
        Set<ConstraintViolation<AnnouncementReply>> violations = validator.validate(announcementReply);
        assertEquals(1, violations.size());
        assertEquals("Announcement is required", violations.iterator().next().getMessage());
    }
    
    @Test
    void testUserValidation() {
        announcementReply.setUser(null);
        Set<ConstraintViolation<AnnouncementReply>> violations = validator.validate(announcementReply);
        assertEquals(1, violations.size());
        assertEquals("User is required", violations.iterator().next().getMessage());
    }
    
    @Test
    void testMessageValidation() {
        // Test null message
        announcementReply.setMessage(null);
        Set<ConstraintViolation<AnnouncementReply>> violations = validator.validate(announcementReply);
        assertEquals(1, violations.size());
        assertEquals("Reply message is required", violations.iterator().next().getMessage());
        
        // Test blank message
        announcementReply.setMessage("");
        violations = validator.validate(announcementReply);
        assertEquals(1, violations.size());
        assertEquals("Reply message is required", violations.iterator().next().getMessage());
        
        // Test message too long
        announcementReply.setMessage("a".repeat(1001));
        violations = validator.validate(announcementReply);
        assertEquals(1, violations.size());
        assertEquals("Reply message must not exceed 1000 characters", violations.iterator().next().getMessage());
    }
    
    @Test
    void testGettersAndSetters() {
        // Test ID
        announcementReply.setId(1L);
        assertEquals(1L, announcementReply.getId());
        
        // Test announcement
        Announcement newAnnouncement = new Announcement();
        newAnnouncement.setId(2L);
        announcementReply.setAnnouncement(newAnnouncement);
        assertEquals(newAnnouncement, announcementReply.getAnnouncement());
        
        // Test user
        User newUser = new User();
        newUser.setId(3L);
        announcementReply.setUser(newUser);
        assertEquals(newUser, announcementReply.getUser());
        
        // Test message
        announcementReply.setMessage("New reply message");
        assertEquals("New reply message", announcementReply.getMessage());
        
        // Test createdAt
        LocalDateTime createdAt = LocalDateTime.now();
        announcementReply.setCreatedAt(createdAt);
        assertEquals(createdAt, announcementReply.getCreatedAt());
    }
}

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

class AnnouncementTest {
    
    private Validator validator;
    private Announcement announcement;
    private User adminUser;
    
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        // Create test admin user
        adminUser = new User();
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
    }
    
    @Test
    void testDefaultConstructor() {
        Announcement newAnnouncement = new Announcement();
        assertNotNull(newAnnouncement);
        assertNull(newAnnouncement.getTitle());
        assertNull(newAnnouncement.getContent());
        assertNull(newAnnouncement.getCreatedBy());
        assertNull(newAnnouncement.getPriority());
        assertNull(newAnnouncement.getStatus());
        assertNull(newAnnouncement.getExpiresAt());
    }
    
    @Test
    void testParameterizedConstructor() {
        assertEquals("Hotel Policy Update", announcement.getTitle());
        assertEquals("Please note the new check-in policy effective immediately.", announcement.getContent());
        assertEquals(adminUser, announcement.getCreatedBy());
        assertEquals(AnnouncementPriority.HIGH, announcement.getPriority());
        assertEquals(AnnouncementStatus.PUBLISHED, announcement.getStatus());
    }
    
    @Test
    void testValidAnnouncement() {
        Set<ConstraintViolation<Announcement>> violations = validator.validate(announcement);
        assertTrue(violations.isEmpty());
    }
    
    @Test
    void testTitleValidation() {
        // Test null title
        announcement.setTitle(null);
        Set<ConstraintViolation<Announcement>> violations = validator.validate(announcement);
        assertEquals(1, violations.size());
        assertEquals("Announcement title is required", violations.iterator().next().getMessage());
        
        // Test blank title
        announcement.setTitle("");
        violations = validator.validate(announcement);
        assertEquals(1, violations.size());
        assertEquals("Announcement title is required", violations.iterator().next().getMessage());
        
        // Test title too long
        announcement.setTitle("a".repeat(201));
        violations = validator.validate(announcement);
        assertEquals(1, violations.size());
        assertEquals("Announcement title must not exceed 200 characters", violations.iterator().next().getMessage());
    }
    
    @Test
    void testContentValidation() {
        // Test null content
        announcement.setContent(null);
        Set<ConstraintViolation<Announcement>> violations = validator.validate(announcement);
        assertEquals(1, violations.size());
        assertEquals("Announcement content is required", violations.iterator().next().getMessage());
        
        // Test blank content
        announcement.setContent("");
        violations = validator.validate(announcement);
        assertEquals(1, violations.size());
        assertEquals("Announcement content is required", violations.iterator().next().getMessage());
        
        // Test content too long
        announcement.setContent("a".repeat(2001));
        violations = validator.validate(announcement);
        assertEquals(1, violations.size());
        assertEquals("Announcement content must not exceed 2000 characters", violations.iterator().next().getMessage());
    }
    
    @Test
    void testCreatedByValidation() {
        announcement.setCreatedBy(null);
        Set<ConstraintViolation<Announcement>> violations = validator.validate(announcement);
        assertEquals(1, violations.size());
        assertEquals("Created by user is required", violations.iterator().next().getMessage());
    }
    
    @Test
    void testPriorityValidation() {
        announcement.setPriority(null);
        Set<ConstraintViolation<Announcement>> violations = validator.validate(announcement);
        assertEquals(1, violations.size());
        assertEquals("Announcement priority is required", violations.iterator().next().getMessage());
    }
    
    @Test
    void testStatusValidation() {
        announcement.setStatus(null);
        Set<ConstraintViolation<Announcement>> violations = validator.validate(announcement);
        assertEquals(1, violations.size());
        assertEquals("Announcement status is required", violations.iterator().next().getMessage());
    }
    
    @Test
    void testGettersAndSetters() {
        // Test ID
        announcement.setId(1L);
        assertEquals(1L, announcement.getId());
        
        // Test title
        announcement.setTitle("New Title");
        assertEquals("New Title", announcement.getTitle());
        
        // Test content
        announcement.setContent("New Content");
        assertEquals("New Content", announcement.getContent());
        
        // Test priority
        announcement.setPriority(AnnouncementPriority.URGENT);
        assertEquals(AnnouncementPriority.URGENT, announcement.getPriority());
        
        // Test status
        announcement.setStatus(AnnouncementStatus.DRAFT);
        assertEquals(AnnouncementStatus.DRAFT, announcement.getStatus());
        
        // Test expiresAt
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(7);
        announcement.setExpiresAt(expiresAt);
        assertEquals(expiresAt, announcement.getExpiresAt());
    }
    
    @Test
    void testEnumValues() {
        // Test priority enum values
        announcement.setPriority(AnnouncementPriority.LOW);
        assertEquals(AnnouncementPriority.LOW, announcement.getPriority());
        
        announcement.setPriority(AnnouncementPriority.MEDIUM);
        assertEquals(AnnouncementPriority.MEDIUM, announcement.getPriority());
        
        announcement.setPriority(AnnouncementPriority.HIGH);
        assertEquals(AnnouncementPriority.HIGH, announcement.getPriority());
        
        announcement.setPriority(AnnouncementPriority.URGENT);
        assertEquals(AnnouncementPriority.URGENT, announcement.getPriority());
        
        // Test status enum values
        announcement.setStatus(AnnouncementStatus.DRAFT);
        assertEquals(AnnouncementStatus.DRAFT, announcement.getStatus());
        
        announcement.setStatus(AnnouncementStatus.PUBLISHED);
        assertEquals(AnnouncementStatus.PUBLISHED, announcement.getStatus());
        
        announcement.setStatus(AnnouncementStatus.ARCHIVED);
        assertEquals(AnnouncementStatus.ARCHIVED, announcement.getStatus());
    }
}
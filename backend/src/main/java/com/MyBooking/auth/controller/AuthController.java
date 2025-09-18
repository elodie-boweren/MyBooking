package com.MyBooking.auth.controller;

import com.MyBooking.auth.domain.NotificationPreference;
import com.MyBooking.auth.domain.Role;
import com.MyBooking.auth.domain.User;
import com.MyBooking.auth.dto.ChangePasswordRequestDto;
import com.MyBooking.auth.dto.LoginRequestDto;
import com.MyBooking.auth.dto.LoginResponseDto;
import com.MyBooking.auth.dto.UserResponseDto;
import com.MyBooking.auth.service.AuthService;
import com.MyBooking.common.exception.BusinessRuleException;
import com.MyBooking.common.exception.NotFoundException;
import com.MyBooking.common.mapper.PageResponse;
import com.MyBooking.common.security.JwtService;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for authentication and user management
 * Uses hybrid approach: DTOs for security-critical operations, entities for simple operations
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @Autowired
    private JwtService jwtService;

    /**
     * User registration 
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        try {
            // Set default role for new users
            if (user.getRole() == null) {
                user.setRole(Role.CLIENT);
            }
            
            User registeredUser = authService.registerUser(
                user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getAddress(),
                user.getBirthDate()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (BusinessRuleException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * User authentication - Uses DTO for security
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        try {
            String token = authService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
            User user = authService.getUserProfile(loginRequest.getEmail());
            
            // Calculate token expiration (24 hours)
            LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);
            
            LoginResponseDto response = new LoginResponseDto(
                token,
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                expiresAt
            );
            
            return ResponseEntity.ok(response);
        } catch (NotFoundException | BusinessRuleException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Get user profile - Uses DTO to exclude password
     */
    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('CLIENT', 'EMPLOYEE', 'ADMIN')")
    public ResponseEntity<UserResponseDto> getProfile(@RequestHeader("Authorization") String authHeader) {
        try {
            String email = extractEmailFromToken(authHeader);
            User user = authService.getUserProfile(email);
            
            UserResponseDto response = new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getBirthDate(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
            );
            
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update user profile 
     */
    @PutMapping("/profile")
    @PreAuthorize("hasAnyRole('CLIENT', 'EMPLOYEE', 'ADMIN')")
    public ResponseEntity<User> updateProfile(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody User user) {
        try {
            String email = extractEmailFromToken(authHeader);
            User updatedUser = authService.updateUserProfile(
                email,
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getAddress(),
                user.getBirthDate()
            );
            return ResponseEntity.ok(updatedUser);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Change password - Uses DTO for security
     */
    @PutMapping("/change-password")
    @PreAuthorize("hasAnyRole('CLIENT', 'EMPLOYEE', 'ADMIN')")
    public ResponseEntity<Void> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody ChangePasswordRequestDto request) {
        try {
            String email = extractEmailFromToken(authHeader);
            authService.changePassword(email, request.getCurrentPassword(), request.getNewPassword());
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BusinessRuleException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Reset password 
     */
    @PutMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @RequestParam String email,
            @RequestParam String newPassword) {
        try {
            authService.resetPassword(email, newPassword);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get user notification preferences - Uses entity directly
     */
    @GetMapping("/notification-preferences")
    @PreAuthorize("hasAnyRole('CLIENT', 'EMPLOYEE', 'ADMIN')")
    public ResponseEntity<List<NotificationPreference>> getNotificationPreferences(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String email = extractEmailFromToken(authHeader);
            List<NotificationPreference> preferences = authService.getUserNotificationPreferences(email);
            return ResponseEntity.ok(preferences);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update notification preference - Uses entity directly
     */
    @PutMapping("/notification-preferences")
    @PreAuthorize("hasAnyRole('CLIENT', 'EMPLOYEE', 'ADMIN')")
    public ResponseEntity<NotificationPreference> updateNotificationPreference(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody NotificationPreference preference) {
        try {
            String email = extractEmailFromToken(authHeader);
            NotificationPreference updatedPreference = authService.updateNotificationPreference(
                email,
                preference.getNotificationType(),
                preference.getEmailEnabled(),
                preference.getSmsEnabled(),
                preference.getPushEnabled()
            );
            return ResponseEntity.ok(updatedPreference);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get all users - Admin only, uses entity directly
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = authService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Get users by role - Admin only, uses entity directly
     */
    @GetMapping("/users/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable Role role) {
        List<User> users = authService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    /**
     * Create user - Admin only, uses entity directly
     */
    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        try {
            User createdUser = authService.registerUser(
                user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getAddress(),
                user.getBirthDate()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (BusinessRuleException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Auth service is running");
    }

    /**
     * Extract email from JWT token
     */
    private String extractEmailFromToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtService.extractUsername(token);
        }
        throw new BusinessRuleException("Invalid authorization header");
    }
}

package com.MyBooking.auth.dto;

import com.MyBooking.auth.domain.Role;
import java.time.LocalDateTime;

/**
 * DTO for authentication response
 * Contains JWT token and user information
 */
public class LoginResponseDto {
    
    private String token;
    private String tokenType = "Bearer";
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private LocalDateTime expiresAt;
    
    // Constructors
    public LoginResponseDto() {}
    
    public LoginResponseDto(String token, Long userId, String email, String firstName, 
                           String lastName, Role role, LocalDateTime expiresAt) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.expiresAt = expiresAt;
    }
    
    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    
    // Utility methods
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public String getRoleDisplayName() {
        return role != null ? role.toString() : "Unknown";
    }
    
    public boolean isTokenValid() {
        return token != null && !token.trim().isEmpty() && 
               expiresAt != null && expiresAt.isAfter(LocalDateTime.now());
    }
    
    public boolean isClient() {
        return role == Role.CLIENT;
    }
    
    public boolean isEmployee() {
        return role == Role.EMPLOYEE;
    }
    
    public boolean isAdmin() {
        return role == Role.ADMIN;
    }
}

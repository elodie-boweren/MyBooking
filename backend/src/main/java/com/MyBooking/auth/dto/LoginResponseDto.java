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
    
    // Additional fields for frontend compatibility
    private boolean client;
    private boolean employee;
    private boolean admin;
    private String roleDisplayName;
    private boolean tokenValid;
    private String fullName;
    
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
        
        // Set additional fields for frontend compatibility
        this.client = (role == Role.CLIENT);
        this.employee = (role == Role.EMPLOYEE);
        this.admin = (role == Role.ADMIN);
        this.roleDisplayName = role != null ? role.toString() : "Unknown";
        this.tokenValid = token != null && !token.trim().isEmpty() && 
                         expiresAt != null && expiresAt.isAfter(LocalDateTime.now());
        this.fullName = firstName + " " + lastName;
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
    
    // Additional field getters and setters
    public boolean isClient() { return client; }
    public void setClient(boolean client) { this.client = client; }
    
    public boolean isEmployee() { return employee; }
    public void setEmployee(boolean employee) { this.employee = employee; }
    
    public boolean isAdmin() { return admin; }
    public void setAdmin(boolean admin) { this.admin = admin; }
    
    public String getRoleDisplayName() { return roleDisplayName; }
    public void setRoleDisplayName(String roleDisplayName) { this.roleDisplayName = roleDisplayName; }
    
    public boolean isTokenValid() { return tokenValid; }
    public void setTokenValid(boolean tokenValid) { this.tokenValid = tokenValid; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    // Utility methods (now handled by constructor and getters)
}

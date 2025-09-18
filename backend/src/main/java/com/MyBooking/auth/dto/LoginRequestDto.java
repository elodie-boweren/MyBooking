package com.MyBooking.auth.dto;

import jakarta.validation.constraints.*;

/**
 * DTO for user authentication requests
 */
public class LoginRequestDto {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 1, message = "Password cannot be empty")
    private String password;
    
    // Constructors
    public LoginRequestDto() {}
    
    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    // Utility methods
    public boolean isValid() {
        return email != null && !email.trim().isEmpty() && 
               password != null && !password.trim().isEmpty();
    }
}

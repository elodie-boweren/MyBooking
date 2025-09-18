package com.MyBooking.auth.dto;

import jakarta.validation.constraints.*;

/**
 * DTO for password change requests
 */
public class ChangePasswordRequestDto {
    
    @NotBlank(message = "Current password is required")
    private String currentPassword;
    
    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "New password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", 
             message = "New password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    private String newPassword;
    
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
    
    // Constructors
    public ChangePasswordRequestDto() {}
    
    public ChangePasswordRequestDto(String currentPassword, String newPassword, String confirmPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }
    
    // Getters and Setters
    public String getCurrentPassword() { return currentPassword; }
    public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
    
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    
    // Utility methods
    public boolean isNewPasswordValid() {
        return newPassword != null && newPassword.length() >= 8;
    }
    
    public boolean doPasswordsMatch() {
        return newPassword != null && newPassword.equals(confirmPassword);
    }
    
    public boolean isCurrentPasswordDifferent() {
        return currentPassword != null && !currentPassword.equals(newPassword);
    }
}

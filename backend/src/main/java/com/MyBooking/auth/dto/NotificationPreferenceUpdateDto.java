package com.MyBooking.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO for updating notification preferences
 */
public class NotificationPreferenceUpdateDto {
    
    @NotBlank(message = "Notification type is required")
    @Size(max = 50, message = "Notification type must not exceed 50 characters")
    private String notificationType;
    
    @NotNull(message = "Email enabled is required")
    private Boolean emailEnabled;
    
    @NotNull(message = "SMS enabled is required")
    private Boolean smsEnabled;
    
    @NotNull(message = "Push enabled is required")
    private Boolean pushEnabled;
    
    // Constructors
    public NotificationPreferenceUpdateDto() {}
    
    public NotificationPreferenceUpdateDto(String notificationType, Boolean emailEnabled, Boolean smsEnabled, Boolean pushEnabled) {
        this.notificationType = notificationType;
        this.emailEnabled = emailEnabled;
        this.smsEnabled = smsEnabled;
        this.pushEnabled = pushEnabled;
    }
    
    // Getters and Setters
    public String getNotificationType() { return notificationType; }
    public void setNotificationType(String notificationType) { this.notificationType = notificationType; }
    
    public Boolean getEmailEnabled() { return emailEnabled; }
    public void setEmailEnabled(Boolean emailEnabled) { this.emailEnabled = emailEnabled; }
    
    public Boolean getSmsEnabled() { return smsEnabled; }
    public void setSmsEnabled(Boolean smsEnabled) { this.smsEnabled = smsEnabled; }
    
    public Boolean getPushEnabled() { return pushEnabled; }
    public void setPushEnabled(Boolean pushEnabled) { this.pushEnabled = pushEnabled; }
}

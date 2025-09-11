package com.MyBooking.auth.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_preference")
public class NotificationPreference {
    
    @Id
    @Column(name = "user_id")
    private Long userId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @MapsId
    @NotNull(message = "User is required")
    private User user;
    
    @NotNull(message = "Email enabled preference is required")
    @Column(name = "email_enabled", nullable = false)
    private Boolean emailEnabled = true;
    
    @NotNull(message = "SMS enabled preference is required")
    @Column(name = "sms_enabled", nullable = false)
    private Boolean smsEnabled = false;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Constructors
    public NotificationPreference() {}
    
    public NotificationPreference(User user, Boolean emailEnabled, Boolean smsEnabled) {
        this.user = user;
        this.emailEnabled = emailEnabled;
        this.smsEnabled = smsEnabled;
    }
    
    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Boolean getEmailEnabled() { return emailEnabled; }
    public void setEmailEnabled(Boolean emailEnabled) { this.emailEnabled = emailEnabled; }
    
    public Boolean getSmsEnabled() { return smsEnabled; }
    public void setSmsEnabled(Boolean smsEnabled) { this.smsEnabled = smsEnabled; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Simple utility methods
    public boolean isEmailEnabled() { return emailEnabled != null && emailEnabled; }
    public boolean isSmsEnabled() { return smsEnabled != null && smsEnabled; }
}
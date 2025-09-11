package com.MyBooking.feedback.domain;

import com.MyBooking.auth.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedback_reply")
public class FeedbackReply {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_id", nullable = false)
    @NotNull(message = "Feedback is required")
    private Feedback feedback;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_user_id", nullable = false)
    @NotNull(message = "Admin user is required")
    private User adminUser;
    
    @NotBlank(message = "Reply message is required")
    @Size(max = 1000, message = "Reply message must not exceed 1000 characters")
    @Column(name = "message", nullable = false, length = 1000)
    private String message;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // Constructors
    public FeedbackReply() {}
    
    public FeedbackReply(Feedback feedback, User adminUser, String message) {
        this.feedback = feedback;
        this.adminUser = adminUser;
        this.message = message;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Feedback getFeedback() { return feedback; }
    public void setFeedback(Feedback feedback) { this.feedback = feedback; }
    
    public User getAdminUser() { return adminUser; }
    public void setAdminUser(User adminUser) { this.adminUser = adminUser; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
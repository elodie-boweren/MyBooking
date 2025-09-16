package com.MyBooking.room.domain;

import com.MyBooking.auth.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "room_status_update")
public class RoomStatusUpdate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Room is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
    
    @NotNull(message = "Previous status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status", nullable = false, length = 32)
    private RoomStatus previousStatus;
    
    @NotNull(message = "New status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false, length = 32)
    private RoomStatus newStatus;
    
    @NotNull(message = "Updated by user is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_user_id", nullable = false)
    private User updatedBy;
    
    @Size(max = 500, message = "Notes must not exceed 500 characters")
    @Column(name = "notes", length = 500)
    private String notes;
    
    @NotNull(message = "Update timestamp is required")
    @CreationTimestamp
    @Column(name = "updated_at", nullable = false, updatable = false)
    private LocalDateTime updatedAt;
    
    @Size(max = 100, message = "Update reason must not exceed 100 characters")
    @Column(name = "update_reason", length = 100)
    private String updateReason;
    
    @Column(name = "is_automatic", nullable = false)
    private Boolean isAutomatic = false;
    
    // Constructors
    public RoomStatusUpdate() {}
    
    public RoomStatusUpdate(Room room, RoomStatus previousStatus, RoomStatus newStatus, User updatedBy, String notes) {
        this.room = room;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.updatedBy = updatedBy;
        this.notes = notes;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }
    
    public RoomStatus getPreviousStatus() { return previousStatus; }
    public void setPreviousStatus(RoomStatus previousStatus) { this.previousStatus = previousStatus; }
    
    public RoomStatus getNewStatus() { return newStatus; }
    public void setNewStatus(RoomStatus newStatus) { this.newStatus = newStatus; }
    
    public User getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(User updatedBy) { this.updatedBy = updatedBy; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public String getUpdateReason() { return updateReason; }
    public void setUpdateReason(String updateReason) { this.updateReason = updateReason; }
    
    public Boolean getIsAutomatic() { return isAutomatic; }
    public void setIsAutomatic(Boolean isAutomatic) { this.isAutomatic = isAutomatic; }
    
    // Business methods
    public boolean isStatusChange() {
        return !previousStatus.equals(newStatus);
    }
    
    public boolean isAutomaticUpdate() {
        return Boolean.TRUE.equals(isAutomatic);
    }
    
    public boolean isManualUpdate() {
        return Boolean.FALSE.equals(isAutomatic);
    }
}

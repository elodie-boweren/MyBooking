package com.MyBooking.event.dto;

import com.MyBooking.event.domain.EventBookingStatus;
import com.MyBooking.event.domain.EventType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EventBookingResponseDto {
    
    private Long id;
    private Long eventId;
    private String eventName;
    private EventType eventType;
    private LocalDateTime eventStartAt;
    private LocalDateTime eventEndAt;
    private BigDecimal eventPrice;
    private String eventCurrency;
    private Long userId;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private Long reservationId;
    private EventBookingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public EventBookingResponseDto() {}
    
    public EventBookingResponseDto(Long id, Long eventId, String eventName, EventType eventType, 
                                  LocalDateTime eventStartAt, LocalDateTime eventEndAt, 
                                  BigDecimal eventPrice, String eventCurrency, Long userId, 
                                  String userFirstName, String userLastName, String userEmail, 
                                  Long reservationId, EventBookingStatus status, 
                                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventType = eventType;
        this.eventStartAt = eventStartAt;
        this.eventEndAt = eventEndAt;
        this.eventPrice = eventPrice;
        this.eventCurrency = eventCurrency;
        this.userId = userId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.reservationId = reservationId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    
    public EventType getEventType() { return eventType; }
    public void setEventType(EventType eventType) { this.eventType = eventType; }
    
    public LocalDateTime getEventStartAt() { return eventStartAt; }
    public void setEventStartAt(LocalDateTime eventStartAt) { this.eventStartAt = eventStartAt; }
    
    public LocalDateTime getEventEndAt() { return eventEndAt; }
    public void setEventEndAt(LocalDateTime eventEndAt) { this.eventEndAt = eventEndAt; }
    
    public BigDecimal getEventPrice() { return eventPrice; }
    public void setEventPrice(BigDecimal eventPrice) { this.eventPrice = eventPrice; }
    
    public String getEventCurrency() { return eventCurrency; }
    public void setEventCurrency(String eventCurrency) { this.eventCurrency = eventCurrency; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getUserFirstName() { return userFirstName; }
    public void setUserFirstName(String userFirstName) { this.userFirstName = userFirstName; }
    
    public String getUserLastName() { return userLastName; }
    public void setUserLastName(String userLastName) { this.userLastName = userLastName; }
    
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    
    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }
    
    public EventBookingStatus getStatus() { return status; }
    public void setStatus(EventBookingStatus status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

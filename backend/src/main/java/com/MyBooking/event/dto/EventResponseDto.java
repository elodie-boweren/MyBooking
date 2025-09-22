package com.MyBooking.event.dto;

import com.MyBooking.event.domain.EventType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EventResponseDto {
    
    private Long id;
    private String name;
    private String description;
    private EventType eventType;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Integer capacity;
    private BigDecimal price;
    private String currency;
    private Long installationId;
    private String installationName;
    private String installationType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public EventResponseDto() {}
    
    public EventResponseDto(Long id, String name, String description, EventType eventType, 
                           LocalDateTime startAt, LocalDateTime endAt, Integer capacity, 
                           BigDecimal price, String currency, Long installationId, 
                           String installationName, String installationType, 
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.eventType = eventType;
        this.startAt = startAt;
        this.endAt = endAt;
        this.capacity = capacity;
        this.price = price;
        this.currency = currency;
        this.installationId = installationId;
        this.installationName = installationName;
        this.installationType = installationType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public EventType getEventType() { return eventType; }
    public void setEventType(EventType eventType) { this.eventType = eventType; }
    
    public LocalDateTime getStartAt() { return startAt; }
    public void setStartAt(LocalDateTime startAt) { this.startAt = startAt; }
    
    public LocalDateTime getEndAt() { return endAt; }
    public void setEndAt(LocalDateTime endAt) { this.endAt = endAt; }
    
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public Long getInstallationId() { return installationId; }
    public void setInstallationId(Long installationId) { this.installationId = installationId; }
    
    public String getInstallationName() { return installationName; }
    public void setInstallationName(String installationName) { this.installationName = installationName; }
    
    public String getInstallationType() { return installationType; }
    public void setInstallationType(String installationType) { this.installationType = installationType; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

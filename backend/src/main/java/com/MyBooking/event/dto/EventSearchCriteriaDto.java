package com.MyBooking.event.dto;

import com.MyBooking.event.domain.EventType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EventSearchCriteriaDto {
    
    private String name;
    private EventType eventType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long installationId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer minCapacity;
    private Integer maxCapacity;
    
    // Constructors
    public EventSearchCriteriaDto() {}
    
    public EventSearchCriteriaDto(String name, EventType eventType, LocalDateTime startDate, 
                                 LocalDateTime endDate, Long installationId, BigDecimal minPrice, 
                                 BigDecimal maxPrice, Integer minCapacity, Integer maxCapacity) {
        this.name = name;
        this.eventType = eventType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.installationId = installationId;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minCapacity = minCapacity;
        this.maxCapacity = maxCapacity;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public EventType getEventType() { return eventType; }
    public void setEventType(EventType eventType) { this.eventType = eventType; }
    
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    
    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    
    public Long getInstallationId() { return installationId; }
    public void setInstallationId(Long installationId) { this.installationId = installationId; }
    
    public BigDecimal getMinPrice() { return minPrice; }
    public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }
    
    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }
    
    public Integer getMinCapacity() { return minCapacity; }
    public void setMinCapacity(Integer minCapacity) { this.minCapacity = minCapacity; }
    
    public Integer getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(Integer maxCapacity) { this.maxCapacity = maxCapacity; }
}

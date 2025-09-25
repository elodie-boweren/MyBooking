package com.MyBooking.event.dto;

import jakarta.validation.constraints.NotNull;

public class EventBookingCreateRequestDto {
    
    @NotNull(message = "Event ID is required")
    private Long eventId;
    
    // Make reservation ID optional for standalone event bookings
    private Long reservationId;
    
    // Add participant count for better user experience
    private Integer numberOfParticipants = 1;
    
    // Add special requests
    private String specialRequests;
    
    // Constructors
    public EventBookingCreateRequestDto() {}
    
    public EventBookingCreateRequestDto(Long eventId, Long reservationId) {
        this.eventId = eventId;
        this.reservationId = reservationId;
    }
    
    public EventBookingCreateRequestDto(Long eventId, Long reservationId, Integer numberOfParticipants, String specialRequests) {
        this.eventId = eventId;
        this.reservationId = reservationId;
        this.numberOfParticipants = numberOfParticipants;
        this.specialRequests = specialRequests;
    }
    
    // Getters and Setters
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    
    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }
    
    public Integer getNumberOfParticipants() { return numberOfParticipants; }
    public void setNumberOfParticipants(Integer numberOfParticipants) { this.numberOfParticipants = numberOfParticipants; }
    
    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
}
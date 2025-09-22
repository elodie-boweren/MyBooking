package com.MyBooking.event.dto;

import jakarta.validation.constraints.NotNull;

public class EventBookingCreateRequestDto {
    
    @NotNull(message = "Event ID is required")
    private Long eventId;
    
    @NotNull(message = "Reservation ID is required")
    private Long reservationId;
    
    // Constructors
    public EventBookingCreateRequestDto() {}
    
    public EventBookingCreateRequestDto(Long eventId, Long reservationId) {
        this.eventId = eventId;
        this.reservationId = reservationId;
    }
    
    // Getters and Setters
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    
    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }
}

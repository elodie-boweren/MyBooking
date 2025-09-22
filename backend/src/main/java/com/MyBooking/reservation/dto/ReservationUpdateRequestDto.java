package com.MyBooking.reservation.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * DTO for updating an existing reservation
 * Used by clients and admins to modify reservation details
 */
public class ReservationUpdateRequestDto {
    
    @NotNull(message = "Check-in date is required")
    @Future(message = "Check-in date must be in the future")
    private LocalDate checkIn;
    
    @NotNull(message = "Check-out date is required")
    @Future(message = "Check-out date must be in the future")
    private LocalDate checkOut;
    
    @NotNull(message = "Number of guests is required")
    @Min(value = 1, message = "Number of guests must be at least 1")
    @Max(value = 10, message = "Number of guests must not exceed 10")
    private Integer numberOfGuests;
    
    // Constructors
    public ReservationUpdateRequestDto() {}
    
    public ReservationUpdateRequestDto(LocalDate checkIn, LocalDate checkOut, Integer numberOfGuests) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.numberOfGuests = numberOfGuests;
    }
    
    // Getters and Setters
    public LocalDate getCheckIn() {
        return checkIn;
    }
    
    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }
    
    public LocalDate getCheckOut() {
        return checkOut;
    }
    
    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }
    
    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
    
    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
    
    @Override
    public String toString() {
        return "ReservationUpdateRequestDto{" +
                "checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                ", numberOfGuests=" + numberOfGuests +
                '}';
    }
}

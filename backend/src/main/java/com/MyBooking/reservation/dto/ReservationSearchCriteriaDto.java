package com.MyBooking.reservation.dto;

import com.MyBooking.reservation.domain.ReservationStatus;
import java.time.LocalDate;

/**
 * DTO for reservation search criteria
 * Used for filtering and searching reservations
 */
public class ReservationSearchCriteriaDto {
    
    private Long clientId;
    private Long roomId;
    private ReservationStatus status;
    private LocalDate checkInFrom;
    private LocalDate checkInTo;
    private LocalDate checkOutFrom;
    private LocalDate checkOutTo;
    
    // Constructors
    public ReservationSearchCriteriaDto() {}
    
    public ReservationSearchCriteriaDto(Long clientId, Long roomId, ReservationStatus status,
                                      LocalDate checkInFrom, LocalDate checkInTo,
                                      LocalDate checkOutFrom, LocalDate checkOutTo) {
        this.clientId = clientId;
        this.roomId = roomId;
        this.status = status;
        this.checkInFrom = checkInFrom;
        this.checkInTo = checkInTo;
        this.checkOutFrom = checkOutFrom;
        this.checkOutTo = checkOutTo;
    }
    
    // Getters and Setters
    public Long getClientId() {
        return clientId;
    }
    
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
    
    public Long getRoomId() {
        return roomId;
    }
    
    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
    
    public ReservationStatus getStatus() {
        return status;
    }
    
    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
    
    public LocalDate getCheckInFrom() {
        return checkInFrom;
    }
    
    public void setCheckInFrom(LocalDate checkInFrom) {
        this.checkInFrom = checkInFrom;
    }
    
    public LocalDate getCheckInTo() {
        return checkInTo;
    }
    
    public void setCheckInTo(LocalDate checkInTo) {
        this.checkInTo = checkInTo;
    }
    
    public LocalDate getCheckOutFrom() {
        return checkOutFrom;
    }
    
    public void setCheckOutFrom(LocalDate checkOutFrom) {
        this.checkOutFrom = checkOutFrom;
    }
    
    public LocalDate getCheckOutTo() {
        return checkOutTo;
    }
    
    public void setCheckOutTo(LocalDate checkOutTo) {
        this.checkOutTo = checkOutTo;
    }
    
    @Override
    public String toString() {
        return "ReservationSearchCriteriaDto{" +
                "clientId=" + clientId +
                ", roomId=" + roomId +
                ", status=" + status +
                ", checkInFrom=" + checkInFrom +
                ", checkInTo=" + checkInTo +
                ", checkOutFrom=" + checkOutFrom +
                ", checkOutTo=" + checkOutTo +
                '}';
    }
}

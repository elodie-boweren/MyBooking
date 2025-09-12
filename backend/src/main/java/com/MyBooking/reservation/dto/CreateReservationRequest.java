package com.MyBooking.reservation.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class CreateReservationRequest {

    @NotNull
    @Future(message = "Check-in date cannot be in the future")
    private LocalDate checkIn;

    @NotNull
    @Future(message = "Vheck-out date must be in the future")
    private LocalDate checkOut;

    @Positive
    private Double total;

    @NotNull
    private String currency;

    private Integer usedPoints;

    @NotNull
    private Long userId;

    @NotNull
    private Long roomId;

    // getters & setters
    public LocalDate getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }

    public LocalDate getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Integer getUsedPoints() { return usedPoints; }
    public void setUsedPoints(Integer usedPoints) { this.usedPoints = usedPoints; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
}

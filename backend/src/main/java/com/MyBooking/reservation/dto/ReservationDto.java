package com.MyBooking.reservation.dto;

import com.MyBooking.reservation.domain.ReservationStatus;

import java.time.LocalDate;

public class ReservationDto {
    private Long id;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Double total;
    private String currency;
    private Integer usedPoints;
    private ReservationStatus status;
    private Long userId;
    private Long roomId;

    public ReservationDto(Long id, LocalDate checkIn, LocalDate checkOut, Double total, String currency,
                          Integer usedPoints, ReservationStatus status, Long userId, Long roomId) {
        this.id = id;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.total = total;
        this.currency = currency;
        this.usedPoints = usedPoints;
        this.status = status;
        this.userId = userId;
        this.roomId = roomId;
    }

    // getters
    public Long getId() { return id; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public Double getTotal() { return total; }
    public String getCurrency() { return currency; }
    public Integer getUsedPoints() { return usedPoints; }
    public ReservationStatus getStatus() { return status; }
    public Long getUserId() { return userId; }
    public Long getRoomId() { return roomId; }
}

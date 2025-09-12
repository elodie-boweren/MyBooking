package com.MyBooking.reservation.dto;

import java.time.LocalDate;

public class ReservationSearchCriteria {
    private LocalDate startDate;
    private LocalDate endDate;
    private Long roomId;
    private Long userId;

    public ReservationSearchCriteria(LocalDate startDate, LocalDate endDate, Long roomId, Long userId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.roomId = roomId;
        this.userId = userId;
    }

    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public Long getRoomId() { return roomId; }
    public Long getUserId() { return userId; }
}

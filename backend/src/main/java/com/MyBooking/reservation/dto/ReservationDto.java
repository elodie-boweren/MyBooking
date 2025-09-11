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
}

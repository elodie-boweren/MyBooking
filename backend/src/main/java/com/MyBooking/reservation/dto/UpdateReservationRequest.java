package com.MyBooking.reservation.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class UpdateReservationRequest {
    @NotNull
    private LocalDate checkIn;

    @NotNull
    private LocalDate checkOut;

    private Integer usedPoints;
}

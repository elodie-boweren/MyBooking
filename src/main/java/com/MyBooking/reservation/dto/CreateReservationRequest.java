package com.MyBooking.reservation.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class CreateReservationRequest {
    @NotNull
    private LocalDate checkIn;

    @NotNull
    private LocalDate checkOut;

    private Long roomId;
    private Integer usedPoints;
}

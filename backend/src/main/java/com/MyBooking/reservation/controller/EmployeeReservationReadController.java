package com.MyBooking.reservation.controller;

import com.MyBooking.reservation.dto.*;
import com.MyBooking.reservation.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee/reservations")
public class EmployeeReservationReadController {

    private final ReservationService reservationService;
    public EmployeeReservationReadController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/search")
    public List<ReservationDto> search(ReservationSearchCriteria criteria) {
        return reservationService.searchReservations(criteria);
    }
}
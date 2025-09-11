package com.MyBooking.reservation.controller;

import com.MyBooking.reservation.dto.*;
import com.MyBooking.reservation.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ReservationDto create(@RequestBody CreateReservationRequest request) {
        return reservationService.createReservation(request);
    }

    @GetMapping("/client/{userId}")
    public List<ReservationDto> getUserReservations(@PathVariable Long userId) {
        return reservationService.getUserReservations(userId);
    }

    @GetMapping("/{id}")
    public ReservationDto getById(@PathVariable Long id) {
        return null;
    }

    @PutMapping("/{id}")
    public ReservationDto update(@PathVariable Long id, @RequestBody UpdateReservationRequest request) {
        return reservationService.updateReservation(id, request);
    }

    @DeleteMapping("/{id}")
    public void cancel(@PathVariable Long id) {
        reservationService.cancelReservation(id);
    }
}

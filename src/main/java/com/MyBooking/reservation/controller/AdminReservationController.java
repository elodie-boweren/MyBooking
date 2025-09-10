package com.MyBooking.reservation.controller;

import com.MyBooking.reservation.dto.*;
import com.MyBooking.reservation.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/reservations")
public class AdminReservationController {

    private final ReservationService reservationService;
    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<ReservationDto> findAll() {
        // TODO
        return null;
    }

    @PostMapping
    public ReservationDto create(@RequestBody CreateReservationRequest request) {
        return reservationService.createReservation(request);
    }

    @PutMapping("/{id}")
    public ReservationDto update(@PathVariable Long id, @RequestBody UpdateReservationRequest request) {
        return reservationService.updateReservation(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        reservationService.cancelReservation(id);
    }

    @PostMapping("/{id}/reassign")
    public ReservationDto reassign(@PathVariable Long id, @RequestBody CreateReservationRequest request) {
        return reservationService.reassignReservation(id, request);
    }
}
package com.MyBooking.reservation.controller;

import com.MyBooking.reservation.domain.Reservation;
import com.MyBooking.reservation.dto.CreateReservationRequest;
import com.MyBooking.reservation.dto.UpdateReservationRequest;
import com.MyBooking.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // Client creates a reservation
    @PostMapping("/api/v1/reservations")
    public ResponseEntity<Reservation> create(@RequestBody CreateReservationRequest request) {
        return ResponseEntity.ok(reservationService.createReservation(request));
    }

    // Client displays all their reservations
    @GetMapping("/api/v1/client/reservations")
    public ResponseEntity<List<Reservation>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reservationService.getReservationsByUser(userId));
    }

    // Client displays past reservations
    @GetMapping ("/api/v1/client/reservations/past")
    public ResponseEntity<List<Reservation>> getReservationsByCheckOutDate(@PathVariable Long userId) {
        return ResponseEntity.ok(reservationService.getReservationsByCheckOutDate(userId));
    }

    // Client displays future reservations
    @GetMapping ("/api/v1/client/reservations/upcoming")
    public ResponseEntity<List<Reservation>> getFutureReservations(
            @PathVariable Long userId) {
        return ResponseEntity.ok(reservationService.getFutureReservations(userId));
    }

    // Show one particular reservation
    @GetMapping("/api/v1/reservations/{reservationId}")
    public ResponseEntity<Reservation> getById(@PathVariable Long id) {
        return reservationService.getAllReservations().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update a reservation
    @PutMapping("/api/v1/reservations/{reservationId}")
    public ResponseEntity<Reservation> update(
            @PathVariable Long id,
            @RequestBody UpdateReservationRequest request
    ) {
        return ResponseEntity.ok(reservationService.updateReservation(id, request));
    }

    // Cancel a reservation
    @DeleteMapping("/api/v1/reservations/{reservationId}")
    public ResponseEntity<Reservation> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.cancelReservation(id));
    }
}

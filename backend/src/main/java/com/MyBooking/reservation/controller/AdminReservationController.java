package main.java.com.MyBooking.reservation.controller;

import com.MyBooking.reservation.domain.Reservation;
import com.MyBooking.reservation.dto.CreateReservationRequest;
import com.MyBooking.reservation.dto.UpdateReservationRequest;
import com.MyBooking.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/reservations")
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // Show all reservations
    @GetMapping
    public ResponseEntity<List<Reservation>> getAll() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    // Show one particular reservation
    @GetMapping("/api/v1/admin/reservations/{reservationId} ")
    public ResponseEntity<Reservation> getById(@PathVariable Long id) {
        return reservationService.getAllReservations().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a reservation
    @PostMapping("/api/v1/admin/reservations")
    public ResponseEntity<Reservation> create(@RequestBody CreateReservationRequest request) {
        return ResponseEntity.ok(reservationService.createReservation(request));
    }

    // Update a reservation
    @PutMapping("/api/v1/admin/reservations/{reservationId}")
    public ResponseEntity<Reservation> update(
            @PathVariable Long id,
            @RequestBody UpdateReservationRequest request
    ) {
        return ResponseEntity.ok(reservationService.updateReservation(id, request));
    }

    // Cancel a reservation
    @DeleteMapping("/api/v1/admin/reservations/{reservationId}")
    public ResponseEntity<Reservation> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.cancelReservation(id));
    }

    // Reassign a reservation (cancel + re create)
    @PostMapping("/api/v1/admin/reservations/{reservationId}/reassign")
    public ResponseEntity<Reservation> reassign(
            @PathVariable Long id,
            @RequestBody CreateReservationRequest newRequest
    ) {
        return ResponseEntity.ok(reservationService.reassignReservation(id, newRequest));
    }
}

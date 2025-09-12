package com.MyBooking.reservation.controller;

import com.MyBooking.reservation.domain.Reservation;
import com.MyBooking.reservation.repository.ReservationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employee/reservations")
public class EmployeeReservationReadController {

    private final ReservationRepository reservationRepository;

    public EmployeeReservationReadController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    // Search reservation by period
    @GetMapping("/api/v1/reservations/search")
    public ResponseEntity<List<Reservation>> search(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return ResponseEntity.ok(reservationRepository.findByCheckInBetween(startDate, endDate));
    }
}

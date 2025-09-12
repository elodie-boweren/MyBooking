package com.MyBooking.reservation.service;

import com.MyBooking.reservation.domain.Reservation;
import com.MyBooking.reservation.domain.ReservationStatus;
import com.MyBooking.reservation.dto.CreateReservationRequest;
import com.MyBooking.reservation.dto.UpdateReservationRequest;
import com.MyBooking.reservation.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    // Create a reservation (with overlap rule)
    @Transactional
    public Reservation createReservation(CreateReservationRequest request) {
        // 1. Vérifier cohérence des dates
        if (request.getCheckIn().isAfter(request.getCheckOut())) {
            throw new IllegalArgumentException("Check-in must be before check-out");
        }

        // 2. Check for overlap
        List<Reservation> overlaps = reservationRepository.findOverlappingReservations(
                request.getRoomId(),
                request.getCheckIn(),
                request.getCheckOut()
        );

        if (!overlaps.isEmpty()) {
            throw new IllegalStateException("The room is already booked for these dates");
        }

        // 3. Create reservation
        Reservation reservation = new Reservation();
        reservation.setCheckIn(request.getCheckIn());
        reservation.setCheckOut(request.getCheckOut());
        reservation.setTotal(request.getTotal());
        reservation.setCurrency(request.getCurrency());
        reservation.setUsedPoints(request.getUsedPoints());
        reservation.setStatus(ReservationStatus.CONFIRMED);

        // create setUser() and setRoom() when Room and User are created
        return reservationRepository.save(reservation);
    }

    // Cancel a reservation
    @Transactional
    public Reservation cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Reservation cannot be found"));

        reservation.setStatus(ReservationStatus.CANCELLED);
        return reservationRepository.save(reservation);
    }

    // Reassign reservation (cancel + create)
    @Transactional
    public Reservation reassignReservation(Long reservationId, CreateReservationRequest newRequest) {
        cancelReservation(reservationId);
        return createReservation(newRequest);
    }

    // Update reservation
    @Transactional
    public Reservation updateReservation(Long reservationId, UpdateReservationRequest request) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Reservation cannot be found"));

        reservation.setCheckIn(request.getCheckIn());
        reservation.setCheckOut(request.getCheckOut());
        reservation.setTotal(request.getTotal());
        reservation.setCurrency(request.getCurrency());
        reservation.setUsedPoints(request.getUsedPoints());

        return reservationRepository.save(reservation);
    }

    // Read all reservations (admin)
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // Read reservation for one user (client)
    public List<Reservation> getReservationsByUser(Long userId) {
        return reservationRepository.findByUserId(userId);
    }
}
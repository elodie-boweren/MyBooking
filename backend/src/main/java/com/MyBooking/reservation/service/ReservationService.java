package com.MyBooking.reservation.service;

import com.MyBooking.reservation.dto.*;
import com.MyBooking.reservation.domain.Reservation;

import java.util.List;

public interface ReservationService {

    @Transactional
    ReservationDto createReservation(CreateReservationRequest request) {
        if (!req.getCheckIn().isBefore(req.getCheckOut())) throw new InvalidRequest("invalid dates");

        Room room = roomRepository.findById(req.getRoomId()).orElseThrow(...);
        // overlap only on CONFIRMED reservations
        List<Reservation> overlaps = reservationRepository.findOverlappingConfirmedReservations(room.getId(), req.getCheckIn(), req.getCheckOut());
        if (!overlaps.isEmpty()) throw new OverlapException("dates busy");

        Reservation r = new Reservation();
        r.setCheckIn(req.getCheckIn());
        r.setCheckOut(req.getCheckOut());
        r.setRoom(room);
        r.setUser(currentUser); // inject via SecurityContext
        r.setStatus(ReservationStatus.CONFIRMED);
        // calcul total possible ici
        reservationRepository.save(r);
        return mapToDto(r);
    };

    ReservationDto updateReservation(Long id, UpdateReservationRequest request);
    void cancelReservation(Long id);

    ReservationDto reassignReservation(Long id, CreateReservationRequest request);

    List<ReservationDto> getUserReservations(Long userId);

    List<ReservationDto> searchReservations(ReservationSearchCriteria criteria);
}
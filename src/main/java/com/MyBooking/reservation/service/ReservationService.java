package com.MyBooking.reservation.service;

import com.MyBooking.reservation.dto.*;
import com.MyBooking.reservation.domain.Reservation;

import java.util.List;

public interface ReservationService {
    ReservationDto createReservation(CreateReservationRequest request);
    ReservationDto updateReservation(Long id, UpdateReservationRequest request);
    void cancelReservation(Long id);
    ReservationDto reassignReservation(Long id, CreateReservationRequest request);

    List<ReservationDto> getUserReservations(Long userId);
    List<ReservationDto> searchReservations(ReservationSearchCriteria criteria);
}
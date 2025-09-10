package com.MyBooking.reservation.repository;

import com.MyBooking.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByCheckInBetween(LocalDate start, LocalDate end);

    List<Reservation> findByCheckOutBetween(LocalDate start, LocalDate end);

    List<Reservation> findByUserId(Long userId);
}

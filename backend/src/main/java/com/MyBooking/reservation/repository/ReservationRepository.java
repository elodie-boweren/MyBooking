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

    List<Reservation> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT r FROM Reservation r WHERE r.room.id = :roomId AND r.status = 'CONFIRMED' AND r.checkIn < :checkOut AND r.checkOut > :checkIn")
    List<Reservation> findOverlappingConfirmedReservations(@Param("roomId") Long roomId,
                                                           @Param("checkIn") LocalDate checkIn,
                                                           @Param("checkOut") LocalDate checkOut);
}

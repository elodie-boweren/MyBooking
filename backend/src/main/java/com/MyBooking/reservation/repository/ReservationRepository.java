package com.MyBooking.reservation.repository;

import com.MyBooking.reservation.domain.Reservation;
import com.MyBooking.reservation.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Get all reservations for a user
    List<Reservation> findByUserId(Long userId);

    // Get all reservations for a room
    List<Reservation> findByRoomId(Long roomId);

    // Get reservation for a certain date window (for employee/admin)
    List<Reservation> findByCheckInBetween(LocalDate start, LocalDate end);

    // Get reservations in the past
    List<Reservation> findByUserIdAndCheckOutDateBefore(Long userId, LocalDate now);

    // Get reservations to come
    List<Reservation> findByUserIdAndCheckOutDateAfter(Long userId, LocalDate now);

    // Check if there is a reservation overlap over a period of time
    @Query("""
        SELECT r FROM Reservation r
        WHERE r.room.id = :roomId
          AND r.status = 'CONFIRMED'
          AND (r.checkIn < :endDate AND r.checkOut > :startDate)
    """)
    List<Reservation> findOverlappingReservations(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Get cancelled reservations
    List<Reservation> findByStatus(ReservationStatus status);
}

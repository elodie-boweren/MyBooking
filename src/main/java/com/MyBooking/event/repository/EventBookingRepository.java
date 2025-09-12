package com.mybooking.event.repository;

import com.mybooking.event.domain.EventBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventBookingRepository extends JpaRepository<EventBooking, Long> {
    List<EventBooking> findByUserId(Long userId);
    List<EventBooking> findByEventId(Long eventId);
}

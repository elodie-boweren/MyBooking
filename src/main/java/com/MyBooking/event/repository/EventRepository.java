package com.mybooking.event.repository;

import com.mybooking.event.domain.Event;
import com.mybooking.event.domain.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByStartAtBetweenAndType(LocalDateTime from, LocalDateTime to, EventType type);
}

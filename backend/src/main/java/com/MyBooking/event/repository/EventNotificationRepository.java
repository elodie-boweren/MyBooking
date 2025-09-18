package com.MyBooking.event.repository;

import com.MyBooking.event.domain.Event;
import com.MyBooking.event.domain.EventBooking;
import com.MyBooking.event.domain.EventNotification;
import com.MyBooking.event.domain.EventNotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventNotificationRepository extends JpaRepository<EventNotification, Long> {

    // ==================== BASIC QUERIES ====================

    // Find by event booking
    List<EventNotification> findByEventBooking(EventBooking eventBooking);
    Page<EventNotification> findByEventBooking(EventBooking eventBooking, Pageable pageable);
    
    // Find by event booking ID
    List<EventNotification> findByEventBookingId(Long eventBookingId);
    Page<EventNotification> findByEventBookingId(Long eventBookingId, Pageable pageable);

    // ==================== NOTIFICATION TYPE QUERIES ====================

    // Find by notification type
    List<EventNotification> findByType(EventNotificationType type);
    Page<EventNotification> findByType(EventNotificationType type, Pageable pageable);
    
    // Find by event booking and notification type
    List<EventNotification> findByEventBookingAndType(EventBooking eventBooking, EventNotificationType type);
    Page<EventNotification> findByEventBookingAndType(EventBooking eventBooking, EventNotificationType type, Pageable pageable);
    
    // Find by event booking ID and notification type
    List<EventNotification> findByEventBookingIdAndType(Long eventBookingId, EventNotificationType type);
    Page<EventNotification> findByEventBookingIdAndType(Long eventBookingId, EventNotificationType type, Pageable pageable);

    // ==================== ORDERING QUERIES ====================

    // Find by event booking ordered by creation time (most recent first)
    List<EventNotification> findByEventBookingOrderByCreatedAtDesc(EventBooking eventBooking);
    List<EventNotification> findByEventBookingIdOrderByCreatedAtDesc(Long eventBookingId);
    
    // Find by event booking ordered by creation time (oldest first)
    List<EventNotification> findByEventBookingOrderByCreatedAtAsc(EventBooking eventBooking);
    List<EventNotification> findByEventBookingIdOrderByCreatedAtAsc(Long eventBookingId);
    
    // Find by notification type ordered by creation time
    List<EventNotification> findByTypeOrderByCreatedAtDesc(EventNotificationType type);
    List<EventNotification> findByTypeOrderByCreatedAtAsc(EventNotificationType type);

    // ==================== DATE-BASED QUERIES ====================

    // Find by creation date range
    List<EventNotification> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    Page<EventNotification> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // Find by creation date after
    List<EventNotification> findByCreatedAtAfter(LocalDateTime date);
    Page<EventNotification> findByCreatedAtAfter(LocalDateTime date, Pageable pageable);
    
    // Find by creation date before
    List<EventNotification> findByCreatedAtBefore(LocalDateTime date);
    Page<EventNotification> findByCreatedAtBefore(LocalDateTime date, Pageable pageable);
    
    // Find by event booking and creation date range
    List<EventNotification> findByEventBookingAndCreatedAtBetween(EventBooking eventBooking, LocalDateTime startDate, LocalDateTime endDate);
    Page<EventNotification> findByEventBookingAndCreatedAtBetween(EventBooking eventBooking, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // ==================== SEARCH QUERIES ====================

    // Find by message containing (case-insensitive)
    List<EventNotification> findByMessageContainingIgnoreCase(String message);
    Page<EventNotification> findByMessageContainingIgnoreCase(String message, Pageable pageable);

    // ==================== CUSTOM BUSINESS QUERIES ====================

    // Find recent notifications for an event
    @Query("SELECT en FROM EventNotification en WHERE en.eventBooking.event = :event ORDER BY en.createdAt DESC")
    List<EventNotification> findRecentByEvent(@Param("event") Event event, Pageable pageable);
    
    @Query("SELECT en FROM EventNotification en WHERE en.eventBooking.event.id = :eventId ORDER BY en.createdAt DESC")
    List<EventNotification> findRecentByEventId(@Param("eventId") Long eventId, Pageable pageable);
    
    // Find notifications by type in date range
    @Query("SELECT en FROM EventNotification en WHERE en.type = :type AND en.createdAt BETWEEN :startDate AND :endDate ORDER BY en.createdAt DESC")
    List<EventNotification> findByTypeInPeriod(@Param("type") EventNotificationType type, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Find latest notification for an event
    @Query("SELECT en FROM EventNotification en WHERE en.eventBooking.event = :event ORDER BY en.createdAt DESC")
    List<EventNotification> findLatestByEvent(@Param("event") Event event, Pageable pageable);
    
    @Query("SELECT en FROM EventNotification en WHERE en.eventBooking.event.id = :eventId ORDER BY en.createdAt DESC")
    List<EventNotification> findLatestByEventId(@Param("eventId") Long eventId, Pageable pageable);
    
    // Find notifications for events in date range
    @Query("SELECT en FROM EventNotification en WHERE en.eventBooking.event.startAt BETWEEN :startDate AND :endDate ORDER BY en.createdAt DESC")
    List<EventNotification> findForEventsInPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // ==================== COUNT QUERIES ====================

    // Count by event booking
    long countByEventBooking(EventBooking eventBooking);
    long countByEventBookingId(Long eventBookingId);
    
    // Count by notification type
    long countByType(EventNotificationType type);
    
    // Count by event booking and notification type
    long countByEventBookingAndType(EventBooking eventBooking, EventNotificationType type);
    long countByEventBookingIdAndType(Long eventBookingId, EventNotificationType type);
    
    // Count by creation date range
    long countByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    // ==================== EXISTENCE QUERIES ====================

    // Check existence by event booking
    boolean existsByEventBooking(EventBooking eventBooking);
    boolean existsByEventBookingId(Long eventBookingId);
    
    // Check existence by notification type
    boolean existsByType(EventNotificationType type);
    
    // Check existence by event booking and notification type
    boolean existsByEventBookingAndType(EventBooking eventBooking, EventNotificationType type);
    boolean existsByEventBookingIdAndType(Long eventBookingId, EventNotificationType type);

    // ==================== AGGREGATION QUERIES ====================

    // Get total number of notifications
    long count();

    // Get notification count by event
    @Query("SELECT en.eventBooking.event.id, COUNT(en) FROM EventNotification en GROUP BY en.eventBooking.event.id")
    List<Object[]> getNotificationCountByEvent();
    
    // Get notification count by type
    @Query("SELECT en.type, COUNT(en) FROM EventNotification en GROUP BY en.type")
    List<Object[]> getNotificationCountByType();
    
    // Get notification count by event and type
    @Query("SELECT en.eventBooking.event.id, en.type, COUNT(en) FROM EventNotification en GROUP BY en.eventBooking.event.id, en.type")
    List<Object[]> getNotificationCountByEventAndType();
    
    // Get recent notification statistics
    @Query("SELECT en.type, COUNT(en) FROM EventNotification en WHERE en.createdAt >= :since GROUP BY en.type")
    List<Object[]> getRecentNotificationStatistics(@Param("since") LocalDateTime since);
}

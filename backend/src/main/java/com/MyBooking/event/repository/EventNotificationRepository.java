package com.MyBooking.event.repository;

import com.MyBooking.event.domain.Event;
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

    // Find by event
    List<EventNotification> findByEvent(Event event);
    Page<EventNotification> findByEvent(Event event, Pageable pageable);
    
    // Find by event ID
    List<EventNotification> findByEventId(Long eventId);
    Page<EventNotification> findByEventId(Long eventId, Pageable pageable);

    // ==================== NOTIFICATION TYPE QUERIES ====================

    // Find by notification type
    List<EventNotification> findByNotificationType(EventNotificationType notificationType);
    Page<EventNotification> findByNotificationType(EventNotificationType notificationType, Pageable pageable);
    
    // Find by event and notification type
    List<EventNotification> findByEventAndNotificationType(Event event, EventNotificationType notificationType);
    Page<EventNotification> findByEventAndNotificationType(Event event, EventNotificationType notificationType, Pageable pageable);
    
    // Find by event ID and notification type
    List<EventNotification> findByEventIdAndNotificationType(Long eventId, EventNotificationType notificationType);
    Page<EventNotification> findByEventIdAndNotificationType(Long eventId, EventNotificationType notificationType, Pageable pageable);

    // ==================== ORDERING QUERIES ====================

    // Find by event ordered by creation time (most recent first)
    List<EventNotification> findByEventOrderByCreatedAtDesc(Event event);
    List<EventNotification> findByEventIdOrderByCreatedAtDesc(Long eventId);
    
    // Find by event ordered by creation time (oldest first)
    List<EventNotification> findByEventOrderByCreatedAtAsc(Event event);
    List<EventNotification> findByEventIdOrderByCreatedAtAsc(Long eventId);
    
    // Find by notification type ordered by creation time
    List<EventNotification> findByNotificationTypeOrderByCreatedAtDesc(EventNotificationType notificationType);
    List<EventNotification> findByNotificationTypeOrderByCreatedAtAsc(EventNotificationType notificationType);

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
    
    // Find by event and creation date range
    List<EventNotification> findByEventAndCreatedAtBetween(Event event, LocalDateTime startDate, LocalDateTime endDate);
    Page<EventNotification> findByEventAndCreatedAtBetween(Event event, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // ==================== SEARCH QUERIES ====================

    // Find by message containing (case-insensitive)
    List<EventNotification> findByMessageContainingIgnoreCase(String message);
    Page<EventNotification> findByMessageContainingIgnoreCase(String message, Pageable pageable);
    
    // Find by title containing (case-insensitive)
    List<EventNotification> findByTitleContainingIgnoreCase(String title);
    Page<EventNotification> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // ==================== CUSTOM BUSINESS QUERIES ====================

    // Find recent notifications for an event
    @Query("SELECT en FROM EventNotification en WHERE en.event = :event ORDER BY en.createdAt DESC")
    List<EventNotification> findRecentByEvent(@Param("event") Event event, Pageable pageable);
    
    @Query("SELECT en FROM EventNotification en WHERE en.event.id = :eventId ORDER BY en.createdAt DESC")
    List<EventNotification> findRecentByEventId(@Param("eventId") Long eventId, Pageable pageable);
    
    // Find notifications by type in date range
    @Query("SELECT en FROM EventNotification en WHERE en.notificationType = :type AND en.createdAt BETWEEN :startDate AND :endDate ORDER BY en.createdAt DESC")
    List<EventNotification> findByTypeInPeriod(@Param("type") EventNotificationType notificationType, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Find latest notification for an event
    @Query("SELECT en FROM EventNotification en WHERE en.event = :event ORDER BY en.createdAt DESC")
    List<EventNotification> findLatestByEvent(@Param("event") Event event, Pageable pageable);
    
    @Query("SELECT en FROM EventNotification en WHERE en.event.id = :eventId ORDER BY en.createdAt DESC")
    List<EventNotification> findLatestByEventId(@Param("eventId") Long eventId, Pageable pageable);
    
    // Find notifications for events in date range
    @Query("SELECT en FROM EventNotification en WHERE en.event.startAt BETWEEN :startDate AND :endDate ORDER BY en.createdAt DESC")
    List<EventNotification> findForEventsInPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // ==================== COUNT QUERIES ====================

    // Count by event
    long countByEvent(Event event);
    long countByEventId(Long eventId);
    
    // Count by notification type
    long countByNotificationType(EventNotificationType notificationType);
    
    // Count by event and notification type
    long countByEventAndNotificationType(Event event, EventNotificationType notificationType);
    long countByEventIdAndNotificationType(Long eventId, EventNotificationType notificationType);
    
    // Count by creation date range
    long countByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    // ==================== EXISTENCE QUERIES ====================

    // Check existence by event
    boolean existsByEvent(Event event);
    boolean existsByEventId(Long eventId);
    
    // Check existence by notification type
    boolean existsByNotificationType(EventNotificationType notificationType);
    
    // Check existence by event and notification type
    boolean existsByEventAndNotificationType(Event event, EventNotificationType notificationType);
    boolean existsByEventIdAndNotificationType(Long eventId, EventNotificationType notificationType);

    // ==================== AGGREGATION QUERIES ====================

    // Get total number of notifications
    long count();

    // Get notification count by event
    @Query("SELECT en.event.id, COUNT(en) FROM EventNotification en GROUP BY en.event.id")
    List<Object[]> getNotificationCountByEvent();
    
    // Get notification count by type
    @Query("SELECT en.notificationType, COUNT(en) FROM EventNotification en GROUP BY en.notificationType")
    List<Object[]> getNotificationCountByType();
    
    // Get notification count by event and type
    @Query("SELECT en.event.id, en.notificationType, COUNT(en) FROM EventNotification en GROUP BY en.event.id, en.notificationType")
    List<Object[]> getNotificationCountByEventAndType();
    
    // Get recent notification statistics
    @Query("SELECT en.notificationType, COUNT(en) FROM EventNotification en WHERE en.createdAt >= :since GROUP BY en.notificationType")
    List<Object[]> getRecentNotificationStatistics(@Param("since") LocalDateTime since);
}

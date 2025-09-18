package com.MyBooking.room.repository;

import com.MyBooking.auth.domain.User;
import com.MyBooking.room.domain.Room;
import com.MyBooking.room.domain.RoomStatus;
import com.MyBooking.room.domain.RoomStatusUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomStatusUpdateRepository extends JpaRepository<RoomStatusUpdate, Long> {

    // ==================== BASIC QUERIES ====================

    // Find by room
    List<RoomStatusUpdate> findByRoom(Room room);
    Page<RoomStatusUpdate> findByRoom(Room room, Pageable pageable);
    
    // Find by room ID
    List<RoomStatusUpdate> findByRoomId(Long roomId);
    Page<RoomStatusUpdate> findByRoomId(Long roomId, Pageable pageable);
    
    // Find by updated by user
    List<RoomStatusUpdate> findByUpdatedBy(User user);
    Page<RoomStatusUpdate> findByUpdatedBy(User user, Pageable pageable);
    
    // Find by updated by user ID
    List<RoomStatusUpdate> findByUpdatedById(Long userId);
    Page<RoomStatusUpdate> findByUpdatedById(Long userId, Pageable pageable);

    // ==================== STATUS QUERIES ====================

    // Find by previous status
    List<RoomStatusUpdate> findByPreviousStatus(RoomStatus previousStatus);
    Page<RoomStatusUpdate> findByPreviousStatus(RoomStatus previousStatus, Pageable pageable);
    
    // Find by new status
    List<RoomStatusUpdate> findByNewStatus(RoomStatus newStatus);
    Page<RoomStatusUpdate> findByNewStatus(RoomStatus newStatus, Pageable pageable);
    
    // Find by status change (from one status to another)
    List<RoomStatusUpdate> findByPreviousStatusAndNewStatus(RoomStatus previousStatus, RoomStatus newStatus);
    Page<RoomStatusUpdate> findByPreviousStatusAndNewStatus(RoomStatus previousStatus, RoomStatus newStatus, Pageable pageable);
    
    // Find by room and status change
    List<RoomStatusUpdate> findByRoomAndPreviousStatusAndNewStatus(Room room, RoomStatus previousStatus, RoomStatus newStatus);
    Page<RoomStatusUpdate> findByRoomAndPreviousStatusAndNewStatus(Room room, RoomStatus previousStatus, RoomStatus newStatus, Pageable pageable);

    // ==================== ORDERING QUERIES ====================

    // Find by room ordered by update time (most recent first)
    List<RoomStatusUpdate> findByRoomOrderByUpdatedAtDesc(Room room);
    List<RoomStatusUpdate> findByRoomIdOrderByUpdatedAtDesc(Long roomId);
    
    // Find by room ordered by update time (oldest first)
    List<RoomStatusUpdate> findByRoomOrderByUpdatedAtAsc(Room room);
    List<RoomStatusUpdate> findByRoomIdOrderByUpdatedAtAsc(Long roomId);
    
    // Find by user ordered by update time (most recent first)
    List<RoomStatusUpdate> findByUpdatedByOrderByUpdatedAtDesc(User user);
    List<RoomStatusUpdate> findByUpdatedByIdOrderByUpdatedAtDesc(Long userId);

    // ==================== DATE-BASED QUERIES ====================

    // Find by update date range
    List<RoomStatusUpdate> findByUpdatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    Page<RoomStatusUpdate> findByUpdatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // Find by update date after
    List<RoomStatusUpdate> findByUpdatedAtAfter(LocalDateTime date);
    Page<RoomStatusUpdate> findByUpdatedAtAfter(LocalDateTime date, Pageable pageable);
    
    // Find by update date before
    List<RoomStatusUpdate> findByUpdatedAtBefore(LocalDateTime date);
    Page<RoomStatusUpdate> findByUpdatedAtBefore(LocalDateTime date, Pageable pageable);
    
    // Find by room and update date range
    List<RoomStatusUpdate> findByRoomAndUpdatedAtBetween(Room room, LocalDateTime startDate, LocalDateTime endDate);
    Page<RoomStatusUpdate> findByRoomAndUpdatedAtBetween(Room room, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // Find by user and update date range
    List<RoomStatusUpdate> findByUpdatedByAndUpdatedAtBetween(User user, LocalDateTime startDate, LocalDateTime endDate);
    Page<RoomStatusUpdate> findByUpdatedByAndUpdatedAtBetween(User user, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // ==================== SEARCH QUERIES ====================

    // Find by notes containing (case-insensitive)
    List<RoomStatusUpdate> findByNotesContainingIgnoreCase(String notes);
    Page<RoomStatusUpdate> findByNotesContainingIgnoreCase(String notes, Pageable pageable);
    
    // Find by update reason
    List<RoomStatusUpdate> findByUpdateReason(String updateReason);
    Page<RoomStatusUpdate> findByUpdateReason(String updateReason, Pageable pageable);
    
    // Find by update reason containing (case-insensitive)
    List<RoomStatusUpdate> findByUpdateReasonContainingIgnoreCase(String updateReason);
    Page<RoomStatusUpdate> findByUpdateReasonContainingIgnoreCase(String updateReason, Pageable pageable);

    // ==================== AUTOMATIC/MANUAL QUERIES ====================

    // Find automatic updates
    List<RoomStatusUpdate> findByIsAutomaticTrue();
    Page<RoomStatusUpdate> findByIsAutomaticTrue(Pageable pageable);
    
    // Find manual updates
    List<RoomStatusUpdate> findByIsAutomaticFalse();
    Page<RoomStatusUpdate> findByIsAutomaticFalse(Pageable pageable);
    
    // Find automatic updates by room
    List<RoomStatusUpdate> findByRoomAndIsAutomaticTrue(Room room);
    Page<RoomStatusUpdate> findByRoomAndIsAutomaticTrue(Room room, Pageable pageable);
    
    // Find manual updates by room
    List<RoomStatusUpdate> findByRoomAndIsAutomaticFalse(Room room);
    Page<RoomStatusUpdate> findByRoomAndIsAutomaticFalse(Room room, Pageable pageable);

    // ==================== CUSTOM BUSINESS QUERIES ====================

    // Find latest status update for a room
    @Query("SELECT rsu FROM RoomStatusUpdate rsu WHERE rsu.room = :room ORDER BY rsu.updatedAt DESC")
    List<RoomStatusUpdate> findLatestByRoom(@Param("room") Room room, Pageable pageable);
    
    @Query("SELECT rsu FROM RoomStatusUpdate rsu WHERE rsu.room.id = :roomId ORDER BY rsu.updatedAt DESC")
    List<RoomStatusUpdate> findLatestByRoomId(@Param("roomId") Long roomId, Pageable pageable);
    
    // Find status changes in a specific time period
    @Query("SELECT rsu FROM RoomStatusUpdate rsu WHERE rsu.room = :room AND rsu.updatedAt BETWEEN :startDate AND :endDate ORDER BY rsu.updatedAt DESC")
    List<RoomStatusUpdate> findStatusChangesInPeriod(@Param("room") Room room, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Find all status changes for a room (excluding same status updates)
    @Query("SELECT rsu FROM RoomStatusUpdate rsu WHERE rsu.room = :room AND rsu.previousStatus != rsu.newStatus ORDER BY rsu.updatedAt DESC")
    List<RoomStatusUpdate> findStatusChangesByRoom(@Param("room") Room room);
    
    @Query("SELECT rsu FROM RoomStatusUpdate rsu WHERE rsu.room.id = :roomId AND rsu.previousStatus != rsu.newStatus ORDER BY rsu.updatedAt DESC")
    List<RoomStatusUpdate> findStatusChangesByRoomId(@Param("roomId") Long roomId);
    
    // Find updates by specific user in date range
    @Query("SELECT rsu FROM RoomStatusUpdate rsu WHERE rsu.updatedBy = :user AND rsu.updatedAt BETWEEN :startDate AND :endDate ORDER BY rsu.updatedAt DESC")
    List<RoomStatusUpdate> findUpdatesByUserInPeriod(@Param("user") User user, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // ==================== COUNT QUERIES ====================

    // Count by room
    long countByRoom(Room room);
    long countByRoomId(Long roomId);
    
    // Count by user
    long countByUpdatedBy(User user);
    long countByUpdatedById(Long userId);
    
    // Count by status
    long countByPreviousStatus(RoomStatus previousStatus);
    long countByNewStatus(RoomStatus newStatus);
    
    // Count by status change
    long countByPreviousStatusAndNewStatus(RoomStatus previousStatus, RoomStatus newStatus);
    
    // Count by update type
    long countByIsAutomaticTrue();
    long countByIsAutomaticFalse();
    
    // Count by update reason
    long countByUpdateReason(String updateReason);
    
    // Count by date range
    long countByUpdatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    // ==================== EXISTENCE QUERIES ====================

    // Check existence by room
    boolean existsByRoom(Room room);
    boolean existsByRoomId(Long roomId);
    
    // Check existence by user
    boolean existsByUpdatedBy(User user);
    boolean existsByUpdatedById(Long userId);
    
    // Check existence by status change
    boolean existsByPreviousStatusAndNewStatus(RoomStatus previousStatus, RoomStatus newStatus);
    
    // Check existence by update reason
    boolean existsByUpdateReason(String updateReason);

    // ==================== AGGREGATION QUERIES ====================

    // Get total number of updates
    long count();

    // Get update count by room
    @Query("SELECT rsu.room.id, COUNT(rsu) FROM RoomStatusUpdate rsu GROUP BY rsu.room.id")
    List<Object[]> getUpdateCountByRoom();
    
    // Get update count by user
    @Query("SELECT rsu.updatedBy.id, COUNT(rsu) FROM RoomStatusUpdate rsu GROUP BY rsu.updatedBy.id")
    List<Object[]> getUpdateCountByUser();
    
    // Get status change statistics
    @Query("SELECT rsu.previousStatus, rsu.newStatus, COUNT(rsu) FROM RoomStatusUpdate rsu WHERE rsu.previousStatus != rsu.newStatus GROUP BY rsu.previousStatus, rsu.newStatus")
    List<Object[]> getStatusChangeStatistics();
    
    // Get update count by update reason
    @Query("SELECT rsu.updateReason, COUNT(rsu) FROM RoomStatusUpdate rsu WHERE rsu.updateReason IS NOT NULL GROUP BY rsu.updateReason")
    List<Object[]> getUpdateCountByReason();
    
    // Get automatic vs manual update statistics
    @Query("SELECT rsu.isAutomatic, COUNT(rsu) FROM RoomStatusUpdate rsu GROUP BY rsu.isAutomatic")
    List<Object[]> getAutomaticVsManualStatistics();
}

package com.MyBooking.auth.repository;

import com.MyBooking.auth.domain.NotificationPreference;
import com.MyBooking.auth.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, Long> {

    // ==================== BASIC QUERIES ====================

    // Find by user
    List<NotificationPreference> findByUser(User user);
    Page<NotificationPreference> findByUser(User user, Pageable pageable);
    
    // Find by user ID
    List<NotificationPreference> findByUserId(Long userId);
    Page<NotificationPreference> findByUserId(Long userId, Pageable pageable);
    
    // Find by notification type
    List<NotificationPreference> findByNotificationType(String notificationType);
    Page<NotificationPreference> findByNotificationType(String notificationType, Pageable pageable);
    
    // Find by user and notification type
    Optional<NotificationPreference> findByUserAndNotificationType(User user, String notificationType);
    Optional<NotificationPreference> findByUserIdAndNotificationType(Long userId, String notificationType);

    // ==================== ACTIVE PREFERENCES QUERIES ====================

    // Find active preferences
    List<NotificationPreference> findByIsActiveTrue();
    Page<NotificationPreference> findByIsActiveTrue(Pageable pageable);
    
    // Find active preferences by user
    List<NotificationPreference> findByUserAndIsActiveTrue(User user);
    Page<NotificationPreference> findByUserAndIsActiveTrue(User user, Pageable pageable);
    
    // Find active preferences by user ID
    List<NotificationPreference> findByUserIdAndIsActiveTrue(Long userId);
    Page<NotificationPreference> findByUserIdAndIsActiveTrue(Long userId, Pageable pageable);
    
    // Find active preferences by notification type
    List<NotificationPreference> findByNotificationTypeAndIsActiveTrue(String notificationType);
    Page<NotificationPreference> findByNotificationTypeAndIsActiveTrue(String notificationType, Pageable pageable);

    // ==================== NOTIFICATION CHANNEL QUERIES ====================

    // Find by email enabled
    List<NotificationPreference> findByEmailEnabledTrue();
    Page<NotificationPreference> findByEmailEnabledTrue(Pageable pageable);
    
    // Find by SMS enabled
    List<NotificationPreference> findBySmsEnabledTrue();
    Page<NotificationPreference> findBySmsEnabledTrue(Pageable pageable);
    
    // Find by push enabled
    List<NotificationPreference> findByPushEnabledTrue();
    Page<NotificationPreference> findByPushEnabledTrue(Pageable pageable);
    
    // Find by user and email enabled
    List<NotificationPreference> findByUserAndEmailEnabledTrue(User user);
    List<NotificationPreference> findByUserIdAndEmailEnabledTrue(Long userId);
    
    // Find by user and SMS enabled
    List<NotificationPreference> findByUserAndSmsEnabledTrue(User user);
    List<NotificationPreference> findByUserIdAndSmsEnabledTrue(Long userId);
    
    // Find by user and push enabled
    List<NotificationPreference> findByUserAndPushEnabledTrue(User user);
    List<NotificationPreference> findByUserIdAndPushEnabledTrue(Long userId);

    // ==================== COMBINED QUERIES ====================

    // Find by user and notification type and active status
    Optional<NotificationPreference> findByUserAndNotificationTypeAndIsActiveTrue(User user, String notificationType);
    Optional<NotificationPreference> findByUserIdAndNotificationTypeAndIsActiveTrue(Long userId, String notificationType);
    
    // Find by notification type and email enabled
    List<NotificationPreference> findByNotificationTypeAndEmailEnabledTrue(String notificationType);
    Page<NotificationPreference> findByNotificationTypeAndEmailEnabledTrue(String notificationType, Pageable pageable);
    
    // Find by notification type and SMS enabled
    List<NotificationPreference> findByNotificationTypeAndSmsEnabledTrue(String notificationType);
    Page<NotificationPreference> findByNotificationTypeAndSmsEnabledTrue(String notificationType, Pageable pageable);
    
    // Find by notification type and push enabled
    List<NotificationPreference> findByNotificationTypeAndPushEnabledTrue(String notificationType);
    Page<NotificationPreference> findByNotificationTypeAndPushEnabledTrue(String notificationType, Pageable pageable);

    // ==================== SEARCH QUERIES ====================

    // Find by notification type containing (case-insensitive)
    List<NotificationPreference> findByNotificationTypeContainingIgnoreCase(String notificationType);
    Page<NotificationPreference> findByNotificationTypeContainingIgnoreCase(String notificationType, Pageable pageable);

    // ==================== DATE-BASED QUERIES ====================

    // Find by creation date range
    List<NotificationPreference> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    Page<NotificationPreference> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // Find by creation date after
    List<NotificationPreference> findByCreatedAtAfter(LocalDateTime date);
    Page<NotificationPreference> findByCreatedAtAfter(LocalDateTime date, Pageable pageable);
    
    // Find by creation date before
    List<NotificationPreference> findByCreatedAtBefore(LocalDateTime date);
    Page<NotificationPreference> findByCreatedAtBefore(LocalDateTime date, Pageable pageable);
    
    // Find by update date range
    List<NotificationPreference> findByUpdatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    Page<NotificationPreference> findByUpdatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // ==================== CUSTOM BUSINESS QUERIES ====================

    // Find preferences with any notification enabled
    @Query("SELECT np FROM NotificationPreference np WHERE (np.emailEnabled = true OR np.smsEnabled = true OR np.pushEnabled = true) AND np.isActive = true")
    List<NotificationPreference> findWithAnyNotificationEnabled();
    @Query("SELECT np FROM NotificationPreference np WHERE (np.emailEnabled = true OR np.smsEnabled = true OR np.pushEnabled = true) AND np.isActive = true")
    Page<NotificationPreference> findWithAnyNotificationEnabled(Pageable pageable);
    
    // Find preferences with all notifications disabled
    @Query("SELECT np FROM NotificationPreference np WHERE np.emailEnabled = false AND np.smsEnabled = false AND np.pushEnabled = false AND np.isActive = true")
    List<NotificationPreference> findWithAllNotificationsDisabled();
    @Query("SELECT np FROM NotificationPreference np WHERE np.emailEnabled = false AND np.smsEnabled = false AND np.pushEnabled = false AND np.isActive = true")
    Page<NotificationPreference> findWithAllNotificationsDisabled(Pageable pageable);
    
    // Find user preferences for specific notification types
    @Query("SELECT np FROM NotificationPreference np WHERE np.user = :user AND np.notificationType IN :types AND np.isActive = true")
    List<NotificationPreference> findByUserAndNotificationTypes(@Param("user") User user, @Param("types") List<String> notificationTypes);
    
    @Query("SELECT np FROM NotificationPreference np WHERE np.user.id = :userId AND np.notificationType IN :types AND np.isActive = true")
    List<NotificationPreference> findByUserIdAndNotificationTypes(@Param("userId") Long userId, @Param("types") List<String> notificationTypes);

    // ==================== COUNT QUERIES ====================

    // Count by user
    long countByUser(User user);
    long countByUserId(Long userId);
    
    // Count by notification type
    long countByNotificationType(String notificationType);
    
    // Count by user and notification type
    long countByUserAndNotificationType(User user, String notificationType);
    long countByUserIdAndNotificationType(Long userId, String notificationType);
    
    // Count by active status
    long countByIsActiveTrue();
    long countByIsActiveFalse();
    
    // Count by notification channel
    long countByEmailEnabledTrue();
    long countBySmsEnabledTrue();
    long countByPushEnabledTrue();
    
    // Count by creation date range
    long countByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    // ==================== EXISTENCE QUERIES ====================

    // Check existence by user
    boolean existsByUser(User user);
    boolean existsByUserId(Long userId);
    
    // Check existence by notification type
    boolean existsByNotificationType(String notificationType);
    
    // Check existence by user and notification type
    boolean existsByUserAndNotificationType(User user, String notificationType);
    boolean existsByUserIdAndNotificationType(Long userId, String notificationType);
    
    // Check existence by active status
    boolean existsByIsActiveTrue();
    boolean existsByIsActiveFalse();

    // ==================== AGGREGATION QUERIES ====================

    // Get total number of preferences
    long count();

    // Get preference count by user
    @Query("SELECT np.user.id, COUNT(np) FROM NotificationPreference np GROUP BY np.user.id")
    List<Object[]> getPreferenceCountByUser();
    
    // Get preference count by notification type
    @Query("SELECT np.notificationType, COUNT(np) FROM NotificationPreference np GROUP BY np.notificationType")
    List<Object[]> getPreferenceCountByNotificationType();
    
    // Get notification channel statistics
    @Query("SELECT 'EMAIL', COUNT(np) FROM NotificationPreference np WHERE np.emailEnabled = true AND np.isActive = true " +
           "UNION ALL " +
           "SELECT 'SMS', COUNT(np) FROM NotificationPreference np WHERE np.smsEnabled = true AND np.isActive = true " +
           "UNION ALL " +
           "SELECT 'PUSH', COUNT(np) FROM NotificationPreference np WHERE np.pushEnabled = true AND np.isActive = true")
    List<Object[]> getNotificationChannelStatistics();
    
    // Get active vs inactive statistics
    @Query("SELECT np.isActive, COUNT(np) FROM NotificationPreference np GROUP BY np.isActive")
    List<Object[]> getActiveVsInactiveStatistics();
}

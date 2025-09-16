package com.MyBooking.feedback.repository;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.MyBooking.hotel_management.HotelManagementApplication;
import com.MyBooking.feedback.domain.FeedbackReply;
import com.MyBooking.feedback.domain.Feedback;
import com.MyBooking.feedback.repository.FeedbackReplyRepository;
import com.MyBooking.reservation.domain.Reservation;
import com.MyBooking.reservation.domain.ReservationStatus;
import com.MyBooking.auth.domain.User;
import com.MyBooking.auth.domain.Role;
import com.MyBooking.room.domain.Room;
import com.MyBooking.room.domain.RoomType;
import com.MyBooking.room.domain.RoomStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("repository-test")
@Transactional
@Rollback
@ContextConfiguration(classes = HotelManagementApplication.class)
@EntityScan(basePackages = {
    "com.MyBooking.feedback.domain",
    "com.MyBooking.reservation.domain", 
    "com.MyBooking.auth.domain",
    "com.MyBooking.room.domain"
})
@EnableJpaRepositories(basePackages = "com.MyBooking.feedback.repository")
class FeedbackReplyRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FeedbackReplyRepository feedbackReplyRepository;

    // Test data
    private User client1, client2, admin1, admin2;
    private Room room1, room2;
    private Reservation reservation1, reservation2;
    private Feedback feedback1, feedback2;
    private FeedbackReply reply1, reply2, reply3, reply4, reply5;
    private LocalDateTime baseDateTime;

    @BeforeEach
    void setUp() {
        // Create test users (clients and admins)
        client1 = new User("John", "Doe", "john.doe@hotel.com", "password123", "+1234567890", "123 Main St", LocalDate.of(1990, 1, 1), Role.CLIENT);
        client2 = new User("Jane", "Smith", "jane.smith@hotel.com", "password123", "+1234567891", "456 Oak Ave", LocalDate.of(1985, 5, 15), Role.CLIENT);
        admin1 = new User("Admin", "One", "admin1@hotel.com", "password123", "+1234567892", "789 Admin St", LocalDate.of(1980, 3, 10), Role.ADMIN);
        admin2 = new User("Admin", "Two", "admin2@hotel.com", "password123", "+1234567893", "321 Admin Ave", LocalDate.of(1982, 7, 20), Role.ADMIN);

        // Create test rooms
        room1 = new Room("101", RoomType.SINGLE, 1, new BigDecimal("100.00"), "USD", RoomStatus.AVAILABLE);
        room2 = new Room("102", RoomType.DOUBLE, 2, new BigDecimal("150.00"), "USD", RoomStatus.AVAILABLE);

        // Create test reservations
        reservation1 = new Reservation(LocalDate.now().minusDays(10), LocalDate.now().minusDays(7), 1, new BigDecimal("300.00"), "USD", ReservationStatus.CONFIRMED, client1, room1);
        reservation2 = new Reservation(LocalDate.now().minusDays(5), LocalDate.now().minusDays(3), 2, new BigDecimal("300.00"), "USD", ReservationStatus.CONFIRMED, client2, room2);

        // Set base date time
        baseDateTime = LocalDateTime.now();

        // Persist test data
        entityManager.persistAndFlush(client1);
        entityManager.persistAndFlush(client2);
        entityManager.persistAndFlush(admin1);
        entityManager.persistAndFlush(admin2);
        entityManager.persistAndFlush(room1);
        entityManager.persistAndFlush(room2);
        entityManager.persistAndFlush(reservation1);
        entityManager.persistAndFlush(reservation2);

        // Create test feedbacks
        feedback1 = new Feedback(reservation1, client1, 4, "Good experience, minor issues with WiFi");
        feedback2 = new Feedback(reservation2, client2, 5, "Excellent service and clean room!");

        // Persist feedbacks
        entityManager.persistAndFlush(feedback1);
        entityManager.persistAndFlush(feedback2);

        // Create test feedback replies
        reply1 = new FeedbackReply(feedback1, admin1, "Thank you for your feedback. We'll look into the WiFi issues.");
        reply2 = new FeedbackReply(feedback1, admin2, "We've noted your concerns and will address them.");
        reply3 = new FeedbackReply(feedback2, admin1, "We're delighted you enjoyed your stay!");
        reply4 = new FeedbackReply(feedback2, admin2, "Thank you for the positive feedback!");
        reply5 = new FeedbackReply(feedback1, admin1, "Follow-up: WiFi issues have been resolved.");

        // Persist replies
        entityManager.persistAndFlush(reply1);
        entityManager.persistAndFlush(reply2);
        entityManager.persistAndFlush(reply3);
        entityManager.persistAndFlush(reply4);
        entityManager.persistAndFlush(reply5);
    }

    // ==================== BASIC QUERIES ====================

    @Test
    void testFindByFeedback() {
        List<FeedbackReply> replies = feedbackReplyRepository.findByFeedback(feedback1);
        
        assertThat(replies).hasSize(3);
        assertThat(replies).containsExactlyInAnyOrder(reply1, reply2, reply5);
    }

    @Test
    void testFindByFeedbackWithPagination() {
        Page<FeedbackReply> page = feedbackReplyRepository.findByFeedback(feedback1, PageRequest.of(0, 2));
        
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testFindByFeedbackId() {
        List<FeedbackReply> replies = feedbackReplyRepository.findByFeedbackId(feedback2.getId());
        
        assertThat(replies).hasSize(2);
        assertThat(replies).containsExactlyInAnyOrder(reply3, reply4);
    }

    @Test
    void testFindByFeedbackIdWithPagination() {
        Page<FeedbackReply> page = feedbackReplyRepository.findByFeedbackId(feedback2.getId(), PageRequest.of(0, 1));
        
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testFindByAdminUser() {
        List<FeedbackReply> replies = feedbackReplyRepository.findByAdminUser(admin1);
        
        assertThat(replies).hasSize(3);
        assertThat(replies).containsExactlyInAnyOrder(reply1, reply3, reply5);
    }

    @Test
    void testFindByAdminUserWithPagination() {
        Page<FeedbackReply> page = feedbackReplyRepository.findByAdminUser(admin1, PageRequest.of(0, 2));
        
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testFindByAdminUserId() {
        List<FeedbackReply> replies = feedbackReplyRepository.findByAdminUserId(admin2.getId());
        
        assertThat(replies).hasSize(2);
        assertThat(replies).containsExactlyInAnyOrder(reply2, reply4);
    }

    @Test
    void testFindByAdminUserIdWithPagination() {
        Page<FeedbackReply> page = feedbackReplyRepository.findByAdminUserId(admin2.getId(), PageRequest.of(0, 1));
        
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    // ==================== DATE-BASED QUERIES ====================

    @Test
    void testFindByCreatedAtBetween() {
        LocalDateTime start = baseDateTime.minusMinutes(10);
        LocalDateTime end = baseDateTime.plusMinutes(10);
        
        List<FeedbackReply> replies = feedbackReplyRepository.findByCreatedAtBetween(start, end);
        
        assertThat(replies).hasSize(5);
        assertThat(replies).containsExactlyInAnyOrder(reply1, reply2, reply3, reply4, reply5);
    }

    @Test
    void testFindByCreatedAtBetweenWithPagination() {
        LocalDateTime start = baseDateTime.minusMinutes(10);
        LocalDateTime end = baseDateTime.plusMinutes(10);
        
        Page<FeedbackReply> page = feedbackReplyRepository.findByCreatedAtBetween(start, end, PageRequest.of(0, 3));
        
        assertThat(page.getContent()).hasSize(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testFindByCreatedAtAfter() {
        LocalDateTime cutoff = baseDateTime.minusMinutes(5);
        
        List<FeedbackReply> replies = feedbackReplyRepository.findByCreatedAtAfter(cutoff);
        
        assertThat(replies).hasSize(5);
        assertThat(replies).containsExactlyInAnyOrder(reply1, reply2, reply3, reply4, reply5);
    }

    @Test
    void testFindByCreatedAtAfterWithPagination() {
        LocalDateTime cutoff = baseDateTime.minusMinutes(5);
        
        Page<FeedbackReply> page = feedbackReplyRepository.findByCreatedAtAfter(cutoff, PageRequest.of(0, 2));
        
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getTotalPages()).isEqualTo(3);
    }

    @Test
    void testFindByCreatedAtBefore() {
        LocalDateTime cutoff = baseDateTime.plusMinutes(5);
        
        List<FeedbackReply> replies = feedbackReplyRepository.findByCreatedAtBefore(cutoff);
        
        assertThat(replies).hasSize(5);
        assertThat(replies).containsExactlyInAnyOrder(reply1, reply2, reply3, reply4, reply5);
    }

    @Test
    void testFindByCreatedAtBeforeWithPagination() {
        LocalDateTime cutoff = baseDateTime.plusMinutes(5);
        
        Page<FeedbackReply> page = feedbackReplyRepository.findByCreatedAtBefore(cutoff, PageRequest.of(0, 3));
        
        assertThat(page.getContent()).hasSize(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    // ==================== COMBINED QUERIES ====================

    @Test
    void testFindByFeedbackAndAdminUser() {
        List<FeedbackReply> replies = feedbackReplyRepository.findByFeedbackAndAdminUser(feedback1, admin1);
        
        assertThat(replies).hasSize(2);
        assertThat(replies).containsExactlyInAnyOrder(reply1, reply5);
    }

    @Test
    void testFindByFeedbackAndAdminUserWithPagination() {
        Page<FeedbackReply> page = feedbackReplyRepository.findByFeedbackAndAdminUser(feedback1, admin1, PageRequest.of(0, 1));
        
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testFindByFeedbackIdAndAdminUserId() {
        List<FeedbackReply> replies = feedbackReplyRepository.findByFeedbackIdAndAdminUserId(feedback2.getId(), admin2.getId());
        
        assertThat(replies).hasSize(1);
        assertThat(replies).containsExactly(reply4);
    }

    @Test
    void testFindByFeedbackIdAndAdminUserIdWithPagination() {
        Page<FeedbackReply> page = feedbackReplyRepository.findByFeedbackIdAndAdminUserId(feedback2.getId(), admin2.getId(), PageRequest.of(0, 1));
        
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getTotalPages()).isEqualTo(1);
    }

    // ==================== CUSTOM BUSINESS QUERIES ====================

    @Test
    void testFindRecentReplies() {
        LocalDateTime since = baseDateTime.minusMinutes(5);
        
        List<FeedbackReply> replies = feedbackReplyRepository.findRecentReplies(since);
        
        assertThat(replies).hasSize(5);
        assertThat(replies).containsExactlyInAnyOrder(reply1, reply2, reply3, reply4, reply5);
    }

    @Test
    void testFindRecentRepliesWithPagination() {
        LocalDateTime since = baseDateTime.minusMinutes(5);
        
        Page<FeedbackReply> page = feedbackReplyRepository.findRecentReplies(since, PageRequest.of(0, 3));
        
        assertThat(page.getContent()).hasSize(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testFindByFeedbackOrderByCreatedAtAsc() {
        List<FeedbackReply> replies = feedbackReplyRepository.findByFeedbackOrderByCreatedAtAsc(feedback1);
        
        assertThat(replies).hasSize(3);
        // Should be ordered by creation time ascending
        assertThat(replies.get(0)).isEqualTo(reply1);
        assertThat(replies.get(1)).isEqualTo(reply2);
        assertThat(replies.get(2)).isEqualTo(reply5);
    }

    @Test
    void testFindByFeedbackOrderByCreatedAtAscWithPagination() {
        Page<FeedbackReply> page = feedbackReplyRepository.findByFeedbackOrderByCreatedAtAsc(feedback1, PageRequest.of(0, 2));
        
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getContent().get(0)).isEqualTo(reply1);
        assertThat(page.getContent().get(1)).isEqualTo(reply2);
    }

    @Test
    void testFindByFeedbackIdOrderByCreatedAtAsc() {
        List<FeedbackReply> replies = feedbackReplyRepository.findByFeedbackIdOrderByCreatedAtAsc(feedback2.getId());
        
        assertThat(replies).hasSize(2);
        assertThat(replies.get(0)).isEqualTo(reply3);
        assertThat(replies.get(1)).isEqualTo(reply4);
    }

    @Test
    void testFindByFeedbackIdOrderByCreatedAtAscWithPagination() {
        Page<FeedbackReply> page = feedbackReplyRepository.findByFeedbackIdOrderByCreatedAtAsc(feedback2.getId(), PageRequest.of(0, 1));
        
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getContent().get(0)).isEqualTo(reply3);
    }

    @Test
    void testFindByAdminUserOrderByCreatedAtDesc() {
        List<FeedbackReply> replies = feedbackReplyRepository.findByAdminUserOrderByCreatedAtDesc(admin1);
        
        assertThat(replies).hasSize(3);
        // Should be ordered by creation time descending
        assertThat(replies.get(0)).isEqualTo(reply5);
        assertThat(replies.get(1)).isEqualTo(reply3);
        assertThat(replies.get(2)).isEqualTo(reply1);
    }

    @Test
    void testFindByAdminUserOrderByCreatedAtDescWithPagination() {
        Page<FeedbackReply> page = feedbackReplyRepository.findByAdminUserOrderByCreatedAtDesc(admin1, PageRequest.of(0, 2));
        
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getContent().get(0)).isEqualTo(reply5);
        assertThat(page.getContent().get(1)).isEqualTo(reply3);
    }

    @Test
    void testFindByAdminUserIdOrderByCreatedAtDesc() {
        List<FeedbackReply> replies = feedbackReplyRepository.findByAdminUserIdOrderByCreatedAtDesc(admin2.getId());
        
        assertThat(replies).hasSize(2);
        assertThat(replies.get(0)).isEqualTo(reply4);
        assertThat(replies.get(1)).isEqualTo(reply2);
    }

    @Test
    void testFindByAdminUserIdOrderByCreatedAtDescWithPagination() {
        Page<FeedbackReply> page = feedbackReplyRepository.findByAdminUserIdOrderByCreatedAtDesc(admin2.getId(), PageRequest.of(0, 1));
        
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getContent().get(0)).isEqualTo(reply4);
    }

    // ==================== COUNT QUERIES ====================

    @Test
    void testCountByFeedback() {
        long count = feedbackReplyRepository.countByFeedback(feedback1);
        assertThat(count).isEqualTo(3);
    }

    @Test
    void testCountByFeedbackId() {
        long count = feedbackReplyRepository.countByFeedbackId(feedback2.getId());
        assertThat(count).isEqualTo(2);
    }

    @Test
    void testCountByAdminUser() {
        long count = feedbackReplyRepository.countByAdminUser(admin1);
        assertThat(count).isEqualTo(3);
    }

    @Test
    void testCountByAdminUserId() {
        long count = feedbackReplyRepository.countByAdminUserId(admin2.getId());
        assertThat(count).isEqualTo(2);
    }

    @Test
    void testCountByCreatedAtBetween() {
        LocalDateTime start = baseDateTime.minusMinutes(10);
        LocalDateTime end = baseDateTime.plusMinutes(10);
        
        long count = feedbackReplyRepository.countByCreatedAtBetween(start, end);
        assertThat(count).isEqualTo(5);
    }

    @Test
    void testCountByCreatedAtAfter() {
        LocalDateTime cutoff = baseDateTime.minusMinutes(5);
        
        long count = feedbackReplyRepository.countByCreatedAtAfter(cutoff);
        assertThat(count).isEqualTo(5);
    }

    @Test
    void testCountByCreatedAtBefore() {
        LocalDateTime cutoff = baseDateTime.plusMinutes(5);
        
        long count = feedbackReplyRepository.countByCreatedAtBefore(cutoff);
        assertThat(count).isEqualTo(5);
    }

    @Test
    void testCountByFeedbackAndAdminUser() {
        long count = feedbackReplyRepository.countByFeedbackAndAdminUser(feedback1, admin1);
        assertThat(count).isEqualTo(2);
    }

    @Test
    void testCountByFeedbackIdAndAdminUserId() {
        long count = feedbackReplyRepository.countByFeedbackIdAndAdminUserId(feedback2.getId(), admin2.getId());
        assertThat(count).isEqualTo(1);
    }

    // ==================== EXISTENCE QUERIES ====================

    @Test
    void testExistsByFeedback() {
        boolean exists = feedbackReplyRepository.existsByFeedback(feedback1);
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByFeedbackId() {
        boolean exists = feedbackReplyRepository.existsByFeedbackId(feedback2.getId());
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByAdminUser() {
        boolean exists = feedbackReplyRepository.existsByAdminUser(admin1);
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByAdminUserId() {
        boolean exists = feedbackReplyRepository.existsByAdminUserId(admin2.getId());
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByFeedbackAndAdminUser() {
        boolean exists = feedbackReplyRepository.existsByFeedbackAndAdminUser(feedback1, admin1);
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByFeedbackIdAndAdminUserId() {
        boolean exists = feedbackReplyRepository.existsByFeedbackIdAndAdminUserId(feedback2.getId(), admin2.getId());
        assertThat(exists).isTrue();
    }

    // ==================== AGGREGATION QUERIES ====================

    @Test
    void testFindLatestReplyByFeedback() {
        List<FeedbackReply> replies = feedbackReplyRepository.findLatestReplyByFeedback(feedback1, PageRequest.of(0, 1));
        
        assertThat(replies).hasSize(1);
        assertThat(replies.get(0)).isEqualTo(reply5); // Latest reply
    }

    @Test
    void testFindLatestReplyByFeedbackId() {
        List<FeedbackReply> replies = feedbackReplyRepository.findLatestReplyByFeedbackId(feedback2.getId(), PageRequest.of(0, 1));
        
        assertThat(replies).hasSize(1);
        assertThat(replies.get(0)).isEqualTo(reply4); // Latest reply
    }

    @Test
    void testGetReplyCountByFeedback() {
        List<Object[]> results = feedbackReplyRepository.getReplyCountByFeedback();
        
        assertThat(results).hasSize(2);
        
        // Check that we have counts for both feedbacks
        boolean foundFeedback1 = false;
        boolean foundFeedback2 = false;
        
        for (Object[] result : results) {
            Feedback feedback = (Feedback) result[0];
            Long count = (Long) result[1];
            
            if (feedback.getId().equals(feedback1.getId())) {
                assertThat(count).isEqualTo(3);
                foundFeedback1 = true;
            } else if (feedback.getId().equals(feedback2.getId())) {
                assertThat(count).isEqualTo(2);
                foundFeedback2 = true;
            }
        }
        
        assertThat(foundFeedback1).isTrue();
        assertThat(foundFeedback2).isTrue();
    }

    // ==================== EDGE CASES ====================

    @Test
    void testFindByNonExistentFeedback() {
        Room room = new Room("999", RoomType.SINGLE, 1, new BigDecimal("100.00"), "USD", RoomStatus.AVAILABLE);
        entityManager.persistAndFlush(room);
        
        Reservation nonExistentReservation = new Reservation(LocalDate.now().plusDays(10), LocalDate.now().plusDays(12), 1, new BigDecimal("200.00"), "USD", ReservationStatus.CONFIRMED, client1, room);
        entityManager.persistAndFlush(nonExistentReservation);
        
        Feedback nonExistentFeedback = new Feedback(nonExistentReservation, client1, 3, "Test feedback");
        entityManager.persistAndFlush(nonExistentFeedback);
        
        List<FeedbackReply> replies = feedbackReplyRepository.findByFeedback(nonExistentFeedback);
        assertThat(replies).isEmpty();
    }

    @Test
    void testFindByNonExistentAdminUser() {
        User nonExistentAdmin = new User("Non", "Existent", "nonexistent@hotel.com", "password123", "+1234567899", "999 Non St", LocalDate.of(1990, 1, 1), Role.ADMIN);
        entityManager.persistAndFlush(nonExistentAdmin);
        
        List<FeedbackReply> replies = feedbackReplyRepository.findByAdminUser(nonExistentAdmin);
        assertThat(replies).isEmpty();
    }

    @Test
    void testCountByNonExistentFeedback() {
        Room room = new Room("999", RoomType.SINGLE, 1, new BigDecimal("100.00"), "USD", RoomStatus.AVAILABLE);
        entityManager.persistAndFlush(room);
        
        Reservation nonExistentReservation = new Reservation(LocalDate.now().plusDays(10), LocalDate.now().plusDays(12), 1, new BigDecimal("200.00"), "USD", ReservationStatus.CONFIRMED, client1, room);
        entityManager.persistAndFlush(nonExistentReservation);
        
        Feedback nonExistentFeedback = new Feedback(nonExistentReservation, client1, 3, "Test feedback");
        entityManager.persistAndFlush(nonExistentFeedback);
        
        long count = feedbackReplyRepository.countByFeedback(nonExistentFeedback);
        assertThat(count).isEqualTo(0);
    }

    @Test
    void testExistsByNonExistentFeedback() {
        Room room = new Room("999", RoomType.SINGLE, 1, new BigDecimal("100.00"), "USD", RoomStatus.AVAILABLE);
        entityManager.persistAndFlush(room);
        
        Reservation nonExistentReservation = new Reservation(LocalDate.now().plusDays(10), LocalDate.now().plusDays(12), 1, new BigDecimal("200.00"), "USD", ReservationStatus.CONFIRMED, client1, room);
        entityManager.persistAndFlush(nonExistentReservation);
        
        Feedback nonExistentFeedback = new Feedback(nonExistentReservation, client1, 3, "Test feedback");
        entityManager.persistAndFlush(nonExistentFeedback);
        
        boolean exists = feedbackReplyRepository.existsByFeedback(nonExistentFeedback);
        assertThat(exists).isFalse();
    }

    @Test
    void testFindByDateRangeWithNoResults() {
        LocalDateTime futureStart = baseDateTime.plusDays(1);
        LocalDateTime futureEnd = baseDateTime.plusDays(2);
        
        List<FeedbackReply> replies = feedbackReplyRepository.findByCreatedAtBetween(futureStart, futureEnd);
        assertThat(replies).isEmpty();
    }

    @Test
    void testFindRecentRepliesWithFutureDate() {
        LocalDateTime futureDate = baseDateTime.plusDays(1);
        
        List<FeedbackReply> replies = feedbackReplyRepository.findRecentReplies(futureDate);
        assertThat(replies).isEmpty();
    }
}

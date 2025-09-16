package com.MyBooking.feedback.repository;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.MyBooking.hotel_management.HotelManagementApplication;
import com.MyBooking.feedback.domain.Feedback;
import com.MyBooking.feedback.repository.FeedbackRepository;
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
class FeedbackRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FeedbackRepository feedbackRepository;

    // Test data
    private User user1, user2, user3;
    private Room room1, room2;
    private Reservation reservation1, reservation2, reservation3;
    private Feedback feedback1, feedback2, feedback3, feedback4, feedback5, feedback6;
    private LocalDateTime baseDateTime;

    @BeforeEach
    void setUp() {
        // Create test users
        user1 = new User("John", "Doe", "john.doe@hotel.com", "password123", "+1234567890", "123 Main St", LocalDate.of(1990, 1, 1), Role.CLIENT);
        user2 = new User("Jane", "Smith", "jane.smith@hotel.com", "password123", "+1234567891", "456 Oak Ave", LocalDate.of(1985, 5, 15), Role.CLIENT);
        user3 = new User("Bob", "Wilson", "bob.wilson@hotel.com", "password123", "+1234567892", "789 Pine St", LocalDate.of(1992, 8, 20), Role.CLIENT);

        // Create test rooms
        room1 = new Room("101", RoomType.SINGLE, 1, new BigDecimal("100.00"), "USD", RoomStatus.AVAILABLE);
        room2 = new Room("102", RoomType.DOUBLE, 2, new BigDecimal("150.00"), "USD", RoomStatus.AVAILABLE);

        // Create test reservations
        reservation1 = new Reservation(LocalDate.now().minusDays(10), LocalDate.now().minusDays(7), 1, new BigDecimal("300.00"), "USD", ReservationStatus.CONFIRMED, user1, room1);
        reservation2 = new Reservation(LocalDate.now().minusDays(5), LocalDate.now().minusDays(3), 2, new BigDecimal("300.00"), "USD", ReservationStatus.CONFIRMED, user2, room2);
        reservation3 = new Reservation(LocalDate.now().minusDays(2), LocalDate.now().plusDays(1), 1, new BigDecimal("300.00"), "USD", ReservationStatus.CONFIRMED, user3, room1);

        // Set base date time
        baseDateTime = LocalDateTime.now();

        // Persist test data
        entityManager.persistAndFlush(user1);
        entityManager.persistAndFlush(user2);
        entityManager.persistAndFlush(user3);
        entityManager.persistAndFlush(room1);
        entityManager.persistAndFlush(room2);
        entityManager.persistAndFlush(reservation1);
        entityManager.persistAndFlush(reservation2);
        entityManager.persistAndFlush(reservation3);

        // Create test feedbacks with different ratings and comments
        feedback1 = new Feedback(reservation1, user1, 5, "Excellent service and clean room!");
        feedback2 = new Feedback(reservation2, user2, 4, "Good experience, minor issues with WiFi");
        feedback3 = new Feedback(reservation3, user3, 2, "Room was not clean, poor service");
        feedback4 = new Feedback(reservation1, user2, 3, "Average stay"); // Different user for same reservation
        feedback5 = new Feedback(reservation2, user3, 5, "Perfect stay, will come back!");
        feedback6 = new Feedback(reservation3, user1, 1, "Terrible experience, never again!");

        // Persist feedbacks
        entityManager.persistAndFlush(feedback1);
        entityManager.persistAndFlush(feedback2);
        entityManager.persistAndFlush(feedback3);
        entityManager.persistAndFlush(feedback4);
        entityManager.persistAndFlush(feedback5);
        entityManager.persistAndFlush(feedback6);
    }

    // ==================== BASIC QUERIES ====================

    @Test
    void testFindByUser() {
        List<Feedback> johnFeedbacks = feedbackRepository.findByUser(user1);
        
        assertThat(johnFeedbacks).hasSize(2);
        assertThat(johnFeedbacks).containsExactlyInAnyOrder(feedback1, feedback6);
    }

    @Test
    void testFindByUserId() {
        List<Feedback> johnFeedbacks = feedbackRepository.findByUserId(user1.getId());
        
        assertThat(johnFeedbacks).hasSize(2);
        assertThat(johnFeedbacks).containsExactlyInAnyOrder(feedback1, feedback6);
    }

    @Test
    void testFindByReservation() {
        List<Feedback> reservation1Feedbacks = feedbackRepository.findByReservation(reservation1);
        
        assertThat(reservation1Feedbacks).hasSize(2);
        assertThat(reservation1Feedbacks).containsExactlyInAnyOrder(feedback1, feedback4);
    }

    @Test
    void testFindByReservationId() {
        List<Feedback> reservation1Feedbacks = feedbackRepository.findByReservationId(reservation1.getId());
        
        assertThat(reservation1Feedbacks).hasSize(2);
        assertThat(reservation1Feedbacks).containsExactlyInAnyOrder(feedback1, feedback4);
    }

    @Test
    void testFindByRating() {
        List<Feedback> fiveStarFeedbacks = feedbackRepository.findByRating(5);
        
        assertThat(fiveStarFeedbacks).hasSize(2);
        assertThat(fiveStarFeedbacks).containsExactlyInAnyOrder(feedback1, feedback5);
    }

    @Test
    void testFindByRatingBetween() {
        List<Feedback> highRatedFeedbacks = feedbackRepository.findByRatingBetween(4, 5);
        
        assertThat(highRatedFeedbacks).hasSize(3);
        assertThat(highRatedFeedbacks).containsExactlyInAnyOrder(feedback1, feedback2, feedback5);
    }

    @Test
    void testFindByUserAndReservation() {
        Optional<Feedback> johnReservation1Feedback = feedbackRepository.findByUserAndReservation(user1, reservation1);
        
        assertThat(johnReservation1Feedback).isPresent();
        assertThat(johnReservation1Feedback.get()).isEqualTo(feedback1);
    }

    @Test
    void testFindByUserIdAndReservationId() {
        Optional<Feedback> johnReservation1Feedback = feedbackRepository.findByUserIdAndReservationId(user1.getId(), reservation1.getId());
        
        assertThat(johnReservation1Feedback).isPresent();
        assertThat(johnReservation1Feedback.get()).isEqualTo(feedback1);
    }

    @Test
    void testFindByUserAndReservationNotFound() {
        Optional<Feedback> nonExistentFeedback = feedbackRepository.findByUserAndReservation(user1, reservation2);
        
        assertThat(nonExistentFeedback).isEmpty();
    }

    // ==================== DATE-BASED QUERIES ====================

    @Test
    void testFindByCreatedAtBetween() {
        LocalDateTime startDate = baseDateTime.minusMinutes(1);
        LocalDateTime endDate = baseDateTime.plusMinutes(1);
        List<Feedback> recentFeedbacks = feedbackRepository.findByCreatedAtBetween(startDate, endDate);
        
        assertThat(recentFeedbacks).hasSize(6);
        assertThat(recentFeedbacks).containsExactlyInAnyOrder(feedback1, feedback2, feedback3, feedback4, feedback5, feedback6);
    }

    @Test
    void testFindByCreatedAtAfter() {
        LocalDateTime cutoff = baseDateTime.minusMinutes(1);
        List<Feedback> recentFeedbacks = feedbackRepository.findByCreatedAtAfter(cutoff);
        
        assertThat(recentFeedbacks).hasSize(6);
        assertThat(recentFeedbacks).containsExactlyInAnyOrder(feedback1, feedback2, feedback3, feedback4, feedback5, feedback6);
    }

    @Test
    void testFindByCreatedAtBefore() {
        LocalDateTime cutoff = baseDateTime.plusMinutes(1);
        List<Feedback> olderFeedbacks = feedbackRepository.findByCreatedAtBefore(cutoff);
        
        assertThat(olderFeedbacks).hasSize(6);
        assertThat(olderFeedbacks).containsExactlyInAnyOrder(feedback1, feedback2, feedback3, feedback4, feedback5, feedback6);
    }

    // ==================== COMBINED QUERIES ====================

    @Test
    void testFindByReservationAndRating() {
        List<Feedback> reservation1FiveStar = feedbackRepository.findByReservationAndRating(reservation1, 5);
        
        assertThat(reservation1FiveStar).hasSize(1);
        assertThat(reservation1FiveStar).containsExactly(feedback1);
    }

    @Test
    void testFindByReservationIdAndRating() {
        List<Feedback> reservation1FiveStar = feedbackRepository.findByReservationIdAndRating(reservation1.getId(), 5);
        
        assertThat(reservation1FiveStar).hasSize(1);
        assertThat(reservation1FiveStar).containsExactly(feedback1);
    }

    @Test
    void testFindByUserAndRating() {
        List<Feedback> johnFiveStar = feedbackRepository.findByUserAndRating(user1, 5);
        
        assertThat(johnFiveStar).hasSize(1);
        assertThat(johnFiveStar).containsExactly(feedback1);
    }

    @Test
    void testFindByUserIdAndRating() {
        List<Feedback> johnFiveStar = feedbackRepository.findByUserIdAndRating(user1.getId(), 5);
        
        assertThat(johnFiveStar).hasSize(1);
        assertThat(johnFiveStar).containsExactly(feedback1);
    }

    @Test
    void testFindByRatingGreaterThanEqual() {
        List<Feedback> highRatedFeedbacks = feedbackRepository.findByRatingGreaterThanEqual(4);
        
        assertThat(highRatedFeedbacks).hasSize(3);
        assertThat(highRatedFeedbacks).containsExactlyInAnyOrder(feedback1, feedback2, feedback5);
    }

    @Test
    void testFindByRatingLessThanEqual() {
        List<Feedback> lowRatedFeedbacks = feedbackRepository.findByRatingLessThanEqual(2);
        
        assertThat(lowRatedFeedbacks).hasSize(2);
        assertThat(lowRatedFeedbacks).containsExactlyInAnyOrder(feedback3, feedback6);
    }

    // ==================== CUSTOM BUSINESS QUERIES ====================

    @Test
    void testFindRecentFeedbacks() {
        LocalDateTime since = baseDateTime.minusMinutes(1);
        List<Feedback> recentFeedbacks = feedbackRepository.findRecentFeedbacks(since);
        
        assertThat(recentFeedbacks).hasSize(6);
        // Should be ordered by createdAt DESC
        assertThat(recentFeedbacks.get(0).getCreatedAt()).isAfterOrEqualTo(recentFeedbacks.get(1).getCreatedAt());
    }

    @Test
    void testFindFeedbacksWithComments() {
        List<Feedback> feedbacksWithComments = feedbackRepository.findFeedbacksWithComments();
        
        assertThat(feedbacksWithComments).hasSize(6);
        assertThat(feedbacksWithComments).allMatch(f -> f.getComment() != null && !f.getComment().trim().isEmpty());
    }

    @Test
    void testFindFeedbacksWithoutComments() {
        // Create a feedback without comment
        Feedback feedbackWithoutComment = new Feedback(reservation1, user3, 3, null);
        entityManager.persistAndFlush(feedbackWithoutComment);
        
        List<Feedback> feedbacksWithoutComments = feedbackRepository.findFeedbacksWithoutComments();
        
        assertThat(feedbacksWithoutComments).hasSize(1);
        assertThat(feedbacksWithoutComments).containsExactly(feedbackWithoutComment);
    }

    @Test
    void testFindHighRatedFeedbacks() {
        List<Feedback> highRatedFeedbacks = feedbackRepository.findHighRatedFeedbacks();
        
        assertThat(highRatedFeedbacks).hasSize(3);
        assertThat(highRatedFeedbacks).containsExactlyInAnyOrder(feedback1, feedback2, feedback5);
        assertThat(highRatedFeedbacks).allMatch(f -> f.getRating() >= 4);
    }

    @Test
    void testFindLowRatedFeedbacks() {
        List<Feedback> lowRatedFeedbacks = feedbackRepository.findLowRatedFeedbacks();
        
        assertThat(lowRatedFeedbacks).hasSize(2);
        assertThat(lowRatedFeedbacks).containsExactlyInAnyOrder(feedback3, feedback6);
        assertThat(lowRatedFeedbacks).allMatch(f -> f.getRating() <= 2);
    }

    // ==================== COUNT QUERIES ====================

    @Test
    void testCountByUser() {
        long johnFeedbackCount = feedbackRepository.countByUser(user1);
        
        assertThat(johnFeedbackCount).isEqualTo(2);
    }

    @Test
    void testCountByUserId() {
        long johnFeedbackCount = feedbackRepository.countByUserId(user1.getId());
        
        assertThat(johnFeedbackCount).isEqualTo(2);
    }

    @Test
    void testCountByReservation() {
        long reservation1Count = feedbackRepository.countByReservation(reservation1);
        
        assertThat(reservation1Count).isEqualTo(2);
    }

    @Test
    void testCountByReservationId() {
        long reservation1Count = feedbackRepository.countByReservationId(reservation1.getId());
        
        assertThat(reservation1Count).isEqualTo(2);
    }

    @Test
    void testCountByRating() {
        long fiveStarCount = feedbackRepository.countByRating(5);
        
        assertThat(fiveStarCount).isEqualTo(2);
    }

    @Test
    void testCountByRatingBetween() {
        long highRatedCount = feedbackRepository.countByRatingBetween(4, 5);
        
        assertThat(highRatedCount).isEqualTo(3);
    }

    @Test
    void testCountByCreatedAtBetween() {
        LocalDateTime startDate = baseDateTime.minusMinutes(1);
        LocalDateTime endDate = baseDateTime.plusMinutes(1);
        long recentCount = feedbackRepository.countByCreatedAtBetween(startDate, endDate);
        
        assertThat(recentCount).isEqualTo(6);
    }

    @Test
    void testCountByUserAndRating() {
        long johnFiveStarCount = feedbackRepository.countByUserAndRating(user1, 5);
        
        assertThat(johnFiveStarCount).isEqualTo(1);
    }

    @Test
    void testCountByUserIdAndRating() {
        long johnFiveStarCount = feedbackRepository.countByUserIdAndRating(user1.getId(), 5);
        
        assertThat(johnFiveStarCount).isEqualTo(1);
    }

    @Test
    void testCountByReservationAndRating() {
        long reservation1FiveStarCount = feedbackRepository.countByReservationAndRating(reservation1, 5);
        
        assertThat(reservation1FiveStarCount).isEqualTo(1);
    }

    @Test
    void testCountByReservationIdAndRating() {
        long reservation1FiveStarCount = feedbackRepository.countByReservationIdAndRating(reservation1.getId(), 5);
        
        assertThat(reservation1FiveStarCount).isEqualTo(1);
    }

    // ==================== EXISTENCE QUERIES ====================

    @Test
    void testExistsByUserAndReservation() {
        boolean exists = feedbackRepository.existsByUserAndReservation(user1, reservation1);
        
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByUserIdAndReservationId() {
        boolean exists = feedbackRepository.existsByUserIdAndReservationId(user1.getId(), reservation1.getId());
        
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByUserAndReservationNotFound() {
        boolean exists = feedbackRepository.existsByUserAndReservation(user1, reservation2);
        
        assertThat(exists).isFalse();
    }

    @Test
    void testExistsByUser() {
        boolean exists = feedbackRepository.existsByUser(user1);
        
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByUserId() {
        boolean exists = feedbackRepository.existsByUserId(user1.getId());
        
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByReservation() {
        boolean exists = feedbackRepository.existsByReservation(reservation1);
        
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByReservationId() {
        boolean exists = feedbackRepository.existsByReservationId(reservation1.getId());
        
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByRating() {
        boolean exists = feedbackRepository.existsByRating(5);
        
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByRatingNotFound() {
        boolean exists = feedbackRepository.existsByRating(6);
        
        assertThat(exists).isFalse();
    }

    // ==================== AGGREGATION QUERIES ====================

    @Test
    void testGetAverageRatingByReservation() {
        Double averageRating = feedbackRepository.getAverageRatingByReservation(reservation1);
        
        assertThat(averageRating).isEqualTo(4.0); // (5 + 3) / 2
    }

    @Test
    void testGetAverageRatingByReservationId() {
        Double averageRating = feedbackRepository.getAverageRatingByReservationId(reservation1.getId());
        
        assertThat(averageRating).isEqualTo(4.0); // (5 + 3) / 2
    }

    @Test
    void testGetAverageRatingByUser() {
        Double averageRating = feedbackRepository.getAverageRatingByUser(user1);
        
        assertThat(averageRating).isEqualTo(3.0); // (5 + 1) / 2
    }

    @Test
    void testGetAverageRatingByUserId() {
        Double averageRating = feedbackRepository.getAverageRatingByUserId(user1.getId());
        
        assertThat(averageRating).isEqualTo(3.0); // (5 + 1) / 2
    }

    @Test
    void testGetAverageRatingOverall() {
        Double averageRating = feedbackRepository.getAverageRatingOverall();
        
        assertThat(averageRating).isEqualTo(3.33, org.assertj.core.data.Offset.offset(0.01)); // (5+4+2+3+5+1)/6
    }

    @Test
    void testGetRatingDistribution() {
        List<Object[]> distribution = feedbackRepository.getRatingDistribution();
        
        assertThat(distribution).hasSize(5); // Ratings 1, 2, 3, 4, 5
        
        // Check that we have the right counts
        for (Object[] entry : distribution) {
            Integer rating = (Integer) entry[0];
            Long count = (Long) entry[1];
            
            if (rating == 1) assertThat(count).isEqualTo(1); // feedback6
            if (rating == 2) assertThat(count).isEqualTo(1); // feedback3
            if (rating == 3) assertThat(count).isEqualTo(1); // feedback4
            if (rating == 4) assertThat(count).isEqualTo(1); // feedback2
            if (rating == 5) assertThat(count).isEqualTo(2); // feedback1, feedback5
        }
    }

    // ==================== PAGINATION TESTS ====================

    @Test
    void testFindByUserWithPagination() {
        Page<Feedback> page = feedbackRepository.findByUser(user1, PageRequest.of(0, 1));
        
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testFindByRatingWithPagination() {
        Page<Feedback> page = feedbackRepository.findByRating(5, PageRequest.of(0, 1));
        
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testFindRecentFeedbacksWithPagination() {
        LocalDateTime since = baseDateTime.minusMinutes(1);
        Page<Feedback> page = feedbackRepository.findRecentFeedbacks(since, PageRequest.of(0, 3));
        
        assertThat(page.getContent()).hasSize(3);
        assertThat(page.getTotalElements()).isEqualTo(6);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testFindHighRatedFeedbacksWithPagination() {
        Page<Feedback> page = feedbackRepository.findHighRatedFeedbacks(PageRequest.of(0, 1));
        
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(3);
    }

    // ==================== EDGE CASES ====================

    @Test
    void testFindByNonExistentUser() {
        User nonExistentUser = new User("Non", "Existent", "non@example.com", "password", "+9999999999", "Address", LocalDate.of(2000, 1, 1), Role.CLIENT);
        entityManager.persistAndFlush(nonExistentUser);
        
        List<Feedback> feedbacks = feedbackRepository.findByUser(nonExistentUser);
        
        assertThat(feedbacks).isEmpty();
    }

    @Test
    void testFindByNonExistentReservation() {
        Room room = new Room("999", RoomType.SINGLE, 1, new BigDecimal("100.00"), "USD", RoomStatus.AVAILABLE);
        entityManager.persistAndFlush(room);
        Reservation nonExistentReservation = new Reservation(LocalDate.now().plusDays(10), LocalDate.now().plusDays(12), 1, new BigDecimal("200.00"), "USD", ReservationStatus.CONFIRMED, user1, room);
        entityManager.persistAndFlush(nonExistentReservation);
        
        List<Feedback> feedbacks = feedbackRepository.findByReservation(nonExistentReservation);
        
        assertThat(feedbacks).isEmpty();
    }

    @Test
    void testFindByNonExistentRating() {
        List<Feedback> feedbacks = feedbackRepository.findByRating(10);
        
        assertThat(feedbacks).isEmpty();
    }

    @Test
    void testGetAverageRatingByNonExistentReservation() {
        Room room = new Room("999", RoomType.SINGLE, 1, new BigDecimal("100.00"), "USD", RoomStatus.AVAILABLE);
        entityManager.persistAndFlush(room);
        Reservation nonExistentReservation = new Reservation(LocalDate.now().plusDays(10), LocalDate.now().plusDays(12), 1, new BigDecimal("200.00"), "USD", ReservationStatus.CONFIRMED, user1, room);
        entityManager.persistAndFlush(nonExistentReservation);
        
        Double averageRating = feedbackRepository.getAverageRatingByReservation(nonExistentReservation);
        
        assertThat(averageRating).isNull();
    }

    @Test
    void testGetAverageRatingByNonExistentUser() {
        User nonExistentUser = new User("Non", "Existent", "non@example.com", "password", "+9999999999", "Address", LocalDate.of(2000, 1, 1), Role.CLIENT);
        entityManager.persistAndFlush(nonExistentUser);
        
        Double averageRating = feedbackRepository.getAverageRatingByUser(nonExistentUser);
        
        assertThat(averageRating).isNull();
    }
}
package com.MyBooking.employee.repository;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.MyBooking.hotel_management.HotelManagementApplication;
import com.MyBooking.employee.domain.Training;
import com.MyBooking.employee.repository.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("repository-test")
@ContextConfiguration(classes = HotelManagementApplication.class)
@EntityScan({"com.MyBooking.employee.domain", "com.MyBooking.auth.domain"})
@EnableJpaRepositories("com.MyBooking.employee.repository")
@Transactional
@Rollback
class TrainingRepositoryTest {

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private TestEntityManager entityManager;

    // Test data
    private Training training1, training2, training3, training4, training5, training6;
    private LocalDate baseDate;

    @BeforeEach
    void setUp() {
        // Clear any existing data
        trainingRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();

        // Base date for testing
        baseDate = LocalDate.of(2024, 1, 15);

        // Create test trainings with different date ranges
        // Training1: Jan 15-20 (base date + 0-5 days)
        training1 = new Training("Hotel Management Fundamentals", baseDate, baseDate.plusDays(5));
        
        // Training2: Jan 18-25 (base date + 3-10 days) - overlaps with training1
        training2 = new Training("Hotel Security", baseDate.plusDays(3), baseDate.plusDays(10));
        
        // Training3: Jan 30-Feb 5 (base date + 15-21 days) - no overlap
        training3 = new Training("Cleaning and Maintenance", baseDate.plusDays(15), baseDate.plusDays(21));
        
        // Training4: Feb 10-15 (base date + 26-31 days) - no overlap
        training4 = new Training("Hotel Management Fundamentals", baseDate.plusDays(26), baseDate.plusDays(31));
        
        // Training5: Feb 20-25 (base date + 36-41 days) - no overlap
        training5 = new Training("Customer Service", baseDate.plusDays(36), baseDate.plusDays(41));
        
        // Training6: Mar 1-10 (base date + 45-54 days) - no overlap
        training6 = new Training("Reception skills", baseDate.plusDays(45), baseDate.plusDays(54));

        // Save trainings
        training1 = entityManager.persistAndFlush(training1);
        training2 = entityManager.persistAndFlush(training2);
        training3 = entityManager.persistAndFlush(training3);
        training4 = entityManager.persistAndFlush(training4);
        training5 = entityManager.persistAndFlush(training5);
        training6 = entityManager.persistAndFlush(training6);
    }

    // ==================== BASIC QUERIES TESTS ====================

    @Test
    void testFindByTitle() {
        List<Training> hotelManagementTrainings = trainingRepository.findByTitle("Hotel Management Fundamentals");
        
        assertThat(hotelManagementTrainings).hasSize(2);
        assertThat(hotelManagementTrainings).containsExactlyInAnyOrder(training1, training4);
    }

    @Test
    void testFindByTitleContaining() {
        List<Training> hotelTrainings = trainingRepository.findByTitleContaining("Hotel");
        
        assertThat(hotelTrainings).hasSize(3);
        assertThat(hotelTrainings).containsExactlyInAnyOrder(training1, training2, training4);
    }

    @Test
    void testFindByTitleContainingWithNoResults() {
        List<Training> nonExistentTrainings = trainingRepository.findByTitleContaining("NonExistent");
        
        assertThat(nonExistentTrainings).isEmpty();
    }

    // ==================== DATE-BASED QUERIES TESTS ====================

    @Test
    void testFindByStartDate() {
        List<Training> trainingsStartingOnBaseDate = trainingRepository.findByStartDate(baseDate);
        
        assertThat(trainingsStartingOnBaseDate).hasSize(1);
        assertThat(trainingsStartingOnBaseDate).containsExactly(training1);
    }

    @Test
    void testFindByEndDate() {
        List<Training> trainingsEndingOnSpecificDate = trainingRepository.findByEndDate(baseDate.plusDays(5));
        
        assertThat(trainingsEndingOnSpecificDate).hasSize(1);
        assertThat(trainingsEndingOnSpecificDate).containsExactly(training1);
    }

    @Test
    void testFindByStartDateBetween() {
        LocalDate startRange = baseDate.plusDays(10);
        LocalDate endRange = baseDate.plusDays(30);
        List<Training> trainingsInRange = trainingRepository.findByStartDateBetween(startRange, endRange);
        
        assertThat(trainingsInRange).hasSize(2);
        assertThat(trainingsInRange).containsExactlyInAnyOrder(training3, training4);
    }

    @Test
    void testFindByEndDateBetween() {
        LocalDate startRange = baseDate.plusDays(5);
        LocalDate endRange = baseDate.plusDays(15);
        List<Training> trainingsInRange = trainingRepository.findByEndDateBetween(startRange, endRange);
        
        assertThat(trainingsInRange).hasSize(2);
        assertThat(trainingsInRange).containsExactlyInAnyOrder(training1, training2);
    }

    // ==================== DATE RANGE QUERIES TESTS ====================

    @Test
    void testFindByStartDateAfter() {
        LocalDate cutoff = baseDate.plusDays(20);
        List<Training> trainingsAfter = trainingRepository.findByStartDateAfter(cutoff);
        
        assertThat(trainingsAfter).hasSize(3);
        assertThat(trainingsAfter).containsExactlyInAnyOrder(training4, training5, training6);
        assertThat(trainingsAfter).allMatch(training -> training.getStartDate().isAfter(cutoff));
    }

    @Test
    void testFindByEndDateBefore() {
        LocalDate cutoff = baseDate.plusDays(20);
        List<Training> trainingsBefore = trainingRepository.findByEndDateBefore(cutoff);
        
        assertThat(trainingsBefore).hasSize(2);
        assertThat(trainingsBefore).containsExactlyInAnyOrder(training1, training2);
        assertThat(trainingsBefore).allMatch(training -> training.getEndDate().isBefore(cutoff));
    }

    // ==================== OVERLAP DETECTION TESTS ====================

    @Test
    void testFindOverlappingTrainings() {
        LocalDate startDate = baseDate.plusDays(2);
        LocalDate endDate = baseDate.plusDays(8);
        List<Training> overlappingTrainings = trainingRepository.findOverlappingTrainings(startDate, endDate);
        
        assertThat(overlappingTrainings).hasSize(2);
        assertThat(overlappingTrainings).containsExactlyInAnyOrder(training1, training2);
    }

    @Test
    void testFindOverlappingTrainingsExcluding() {
        LocalDate startDate = baseDate.plusDays(2);
        LocalDate endDate = baseDate.plusDays(8);
        List<Training> overlappingTrainings = trainingRepository.findOverlappingTrainingsExcluding(
            training1.getId(), startDate, endDate);
        
        assertThat(overlappingTrainings).hasSize(1);
        assertThat(overlappingTrainings).containsExactly(training2);
    }

    @Test
    void testFindOverlappingTrainingsNoOverlap() {
        LocalDate startDate = baseDate.plusDays(70);
        LocalDate endDate = baseDate.plusDays(80);
        List<Training> overlappingTrainings = trainingRepository.findOverlappingTrainings(startDate, endDate);
        
        assertThat(overlappingTrainings).isEmpty();
    }

    // ==================== EXISTENCE CHECKS TESTS ====================

    @Test
    void testExistsByTitle() {
        assertThat(trainingRepository.existsByTitle("Hotel Management Fundamentals")).isTrue();
        assertThat(trainingRepository.existsByTitle("Hotel Security")).isTrue();
        assertThat(trainingRepository.existsByTitle("Non-existent Training")).isFalse();
    }

    @Test
    void testExistsByTitleContaining() {
        assertThat(trainingRepository.existsByTitleContaining("Hotel")).isTrue();
        assertThat(trainingRepository.existsByTitleContaining("Customer")).isTrue();
        assertThat(trainingRepository.existsByTitleContaining("Reception")).isTrue();
    }

    @Test
    void testExistsByStartDate() {
        assertThat(trainingRepository.existsByStartDate(baseDate)).isTrue();
        assertThat(trainingRepository.existsByStartDate(baseDate.plusDays(3))).isTrue();
        assertThat(trainingRepository.existsByStartDate(baseDate.plusDays(100))).isFalse();
    }

    @Test
    void testExistsByEndDate() {
        assertThat(trainingRepository.existsByEndDate(baseDate.plusDays(5))).isTrue();
        assertThat(trainingRepository.existsByEndDate(baseDate.plusDays(10))).isTrue();
        assertThat(trainingRepository.existsByEndDate(baseDate.plusDays(100))).isFalse();
    }

    // ==================== PAGINATION SUPPORT TESTS ====================

    @Test
    void testFindByTitleContainingWithPagination() {
        Page<Training> page = trainingRepository.findByTitleContaining("Hotel", PageRequest.of(0, 2));
        
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testFindByStartDateBetweenWithPagination() {
        LocalDate startRange = baseDate.plusDays(10);
        LocalDate endRange = baseDate.plusDays(40);
        Page<Training> page = trainingRepository.findByStartDateBetween(startRange, endRange, PageRequest.of(0, 2));
        
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testFindByStartDateAfterWithPagination() {
        LocalDate cutoff = baseDate.plusDays(20);
        Page<Training> page = trainingRepository.findByStartDateAfter(cutoff, PageRequest.of(0, 2));
        
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testFindByEndDateBeforeWithPagination() {
        LocalDate cutoff = baseDate.plusDays(20);
        Page<Training> page = trainingRepository.findByEndDateBefore(cutoff, PageRequest.of(0, 1));
        
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    // ==================== STATISTICS AND COUNTS TESTS ====================

    @Test
    void testCountByStartDateAfter() {
        LocalDate cutoff = baseDate.plusDays(20);
        long count = trainingRepository.countByStartDateAfter(cutoff);
        
        assertThat(count).isEqualTo(3);
    }

    @Test
    void testCountByEndDateBefore() {
        LocalDate cutoff = baseDate.plusDays(20);
        long count = trainingRepository.countByEndDateBefore(cutoff);
        
        assertThat(count).isEqualTo(2);
    }

    @Test
    void testCountByStartDateBetween() {
        LocalDate startRange = baseDate.plusDays(10);
        LocalDate endRange = baseDate.plusDays(40);
        long count = trainingRepository.countByStartDateBetween(startRange, endRange);
        
        assertThat(count).isEqualTo(3);
    }

    @Test
    void testCountByEndDateBetween() {
        LocalDate startRange = baseDate.plusDays(5);
        LocalDate endRange = baseDate.plusDays(15);
        long count = trainingRepository.countByEndDateBetween(startRange, endRange);
        
        assertThat(count).isEqualTo(2);
    }

  @Test
    void testCountByTitleContaining() {
        long hotelCount = trainingRepository.countByTitleContaining("Hotel");
        long customerCount = trainingRepository.countByTitleContaining("Customer");
        long receptionCount = trainingRepository.countByTitleContaining("Reception");
        
        assertThat(hotelCount).isEqualTo(3);
        assertThat(customerCount).isEqualTo(1);
        assertThat(receptionCount).isEqualTo(1);
    }

    // ==================== EDGE CASES AND BOUNDARY TESTS ====================

    @Test
    void testFindByTitleWithEmptyString() {
        List<Training> emptyTitleTrainings = trainingRepository.findByTitle("");
        
        assertThat(emptyTitleTrainings).isEmpty();
    }

    @Test
    void testFindByTitleContainingWithEmptyString() {
        List<Training> emptyKeywordTrainings = trainingRepository.findByTitleContaining("");
        
        assertThat(emptyKeywordTrainings).hasSize(6); // All trainings contain empty string
    }

    @Test
    void testFindByStartDateBetweenWithSameDates() {
        LocalDate sameDate = baseDate.plusDays(15);
        List<Training> trainings = trainingRepository.findByStartDateBetween(sameDate, sameDate);
        
        assertThat(trainings).hasSize(1);
        assertThat(trainings).containsExactly(training3);
    }

    @Test
    void testFindOverlappingTrainingsWithSameDates() {
        LocalDate sameDate = baseDate.plusDays(15);
        List<Training> overlappingTrainings = trainingRepository.findOverlappingTrainings(sameDate, sameDate);
        
        assertThat(overlappingTrainings).hasSize(1);
        assertThat(overlappingTrainings).containsExactly(training3);
    }

    @Test
    void testFindByStartDateAfterWithFutureDate() {
        LocalDate futureDate = baseDate.plusDays(100);
        List<Training> futureTrainings = trainingRepository.findByStartDateAfter(futureDate);
        
        assertThat(futureTrainings).isEmpty();
    }

    @Test
    void testFindByEndDateBeforeWithPastDate() {
        LocalDate pastDate = baseDate.minusDays(10);
        List<Training> pastTrainings = trainingRepository.findByEndDateBefore(pastDate);
        
        assertThat(pastTrainings).isEmpty();
    }
}

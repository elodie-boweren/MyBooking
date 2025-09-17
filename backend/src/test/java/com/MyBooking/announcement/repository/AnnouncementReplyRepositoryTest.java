package com.MyBooking.announcement.repository;

import com.MyBooking.announcement.domain.Announcement;
import com.MyBooking.announcement.domain.AnnouncementReply;
import com.MyBooking.announcement.domain.AnnouncementPriority;
import com.MyBooking.announcement.domain.AnnouncementStatus;
import com.MyBooking.auth.domain.Role;
import com.MyBooking.auth.domain.User;
import com.MyBooking.hotel_management.HotelManagementApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

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
    "com.MyBooking.announcement.domain",
    "com.MyBooking.auth.domain"
})
@EnableJpaRepositories(basePackages = "com.MyBooking.announcement.repository")
class AnnouncementReplyRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AnnouncementReplyRepository announcementReplyRepository;

    // Test data
    private User admin1, admin2, employee1, employee2, client1;
    private Announcement announcement1, announcement2, announcement3;
    private AnnouncementReply reply1, reply2, reply3, reply4, reply5, reply6;
    private LocalDateTime baseDateTime;

    @BeforeEach
    void setUp() {
        // Create test users
        admin1 = new User("Admin", "One", "admin.one@hotel.com", "adminpass123", "+1112223333", "Admin Office", LocalDate.of(1980, 1, 1), Role.ADMIN);
        admin2 = new User("Admin", "Two", "admin.two@hotel.com", "adminpass123", "+4445556666", "Admin Office", LocalDate.of(1982, 2, 2), Role.ADMIN);
        employee1 = new User("Employee", "One", "employee.one@hotel.com", "emppass123", "+7778889999", "Employee Office", LocalDate.of(1985, 3, 3), Role.EMPLOYEE);
        employee2 = new User("Employee", "Two", "employee.two@hotel.com", "emppass123", "+3334445555", "Employee Office", LocalDate.of(1987, 4, 4), Role.EMPLOYEE);
        client1 = new User("Client", "One", "client.one@hotel.com", "clientpass123", "+9998887777", "Client Address", LocalDate.of(1990, 5, 5), Role.CLIENT);

        // Create test announcements
        announcement1 = new Announcement("Hotel Renovation", "The hotel will be renovated next month.", admin1, AnnouncementPriority.HIGH, AnnouncementStatus.PUBLISHED);
        announcement2 = new Announcement("New Services", "We are introducing new services.", admin2, AnnouncementPriority.MEDIUM, AnnouncementStatus.PUBLISHED);
        announcement3 = new Announcement("Maintenance Notice", "Scheduled maintenance this weekend.", admin1, AnnouncementPriority.LOW, AnnouncementStatus.PUBLISHED);

        // Set base date time
        baseDateTime = LocalDateTime.now();

        // Persist test data
        entityManager.persistAndFlush(admin1);
        entityManager.persistAndFlush(admin2);
        entityManager.persistAndFlush(employee1);
        entityManager.persistAndFlush(employee2);
        entityManager.persistAndFlush(client1);
        entityManager.persistAndFlush(announcement1);
        entityManager.persistAndFlush(announcement2);
        entityManager.persistAndFlush(announcement3);

        // Create test announcement replies
        reply1 = new AnnouncementReply(announcement1, employee1, "Thank you for the update!");
        reply2 = new AnnouncementReply(announcement1, employee2, "Looking forward to the renovation.");
        reply3 = new AnnouncementReply(announcement1, admin2, "We'll keep you posted on progress.");
        reply4 = new AnnouncementReply(announcement2, employee1, "Great news about the new services!");
        reply5 = new AnnouncementReply(announcement2, client1, "What services will be available?");
        reply6 = new AnnouncementReply(announcement3, employee2, "Thanks for the maintenance notice.");

        // Persist replies
        entityManager.persistAndFlush(reply1);
        entityManager.persistAndFlush(reply2);
        entityManager.persistAndFlush(reply3);
        entityManager.persistAndFlush(reply4);
        entityManager.persistAndFlush(reply5);
        entityManager.persistAndFlush(reply6);
    }

    // ==================== BASIC QUERIES ====================

    @Test
    void testFindByAnnouncement() {
        List<AnnouncementReply> replies = announcementReplyRepository.findByAnnouncement(announcement1);
        assertThat(replies).hasSize(3);
        assertThat(replies).containsExactlyInAnyOrder(reply1, reply2, reply3);
    }

    @Test
    void testFindByAnnouncementWithPagination() {
        Page<AnnouncementReply> page = announcementReplyRepository.findByAnnouncement(announcement1, PageRequest.of(0, 2));
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testFindByAnnouncementId() {
        List<AnnouncementReply> replies = announcementReplyRepository.findByAnnouncementId(announcement1.getId());
        assertThat(replies).hasSize(3);
        assertThat(replies).containsExactlyInAnyOrder(reply1, reply2, reply3);
    }

    @Test
    void testFindByAnnouncementIdWithPagination() {
        Page<AnnouncementReply> page = announcementReplyRepository.findByAnnouncementId(announcement1.getId(), PageRequest.of(0, 2));
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testFindByUser() {
        List<AnnouncementReply> replies = announcementReplyRepository.findByUser(employee1);
        assertThat(replies).hasSize(2);
        assertThat(replies).containsExactlyInAnyOrder(reply1, reply4);
    }

    @Test
    void testFindByUserWithPagination() {
        Page<AnnouncementReply> page = announcementReplyRepository.findByUser(employee1, PageRequest.of(0, 1));
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testFindByUserId() {
        List<AnnouncementReply> replies = announcementReplyRepository.findByUserId(employee1.getId());
        assertThat(replies).hasSize(2);
        assertThat(replies).containsExactlyInAnyOrder(reply1, reply4);
    }

    @Test
    void testFindByUserIdWithPagination() {
        Page<AnnouncementReply> page = announcementReplyRepository.findByUserId(employee1.getId(), PageRequest.of(0, 1));
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    // ==================== DATE-BASED QUERIES ====================

    @Test
    void testFindByCreatedAtBetween() {
        LocalDateTime startDate = baseDateTime.minusMinutes(1);
        LocalDateTime endDate = baseDateTime.plusMinutes(1);
        
        List<AnnouncementReply> replies = announcementReplyRepository.findByCreatedAtBetween(startDate, endDate);
        assertThat(replies).hasSize(6);
        assertThat(replies).containsExactlyInAnyOrder(reply1, reply2, reply3, reply4, reply5, reply6);
    }

    @Test
    void testFindByCreatedAtBetweenWithPagination() {
        LocalDateTime startDate = baseDateTime.minusMinutes(1);
        LocalDateTime endDate = baseDateTime.plusMinutes(1);
        
        Page<AnnouncementReply> page = announcementReplyRepository.findByCreatedAtBetween(startDate, endDate, PageRequest.of(0, 3));
        assertThat(page.getContent()).hasSize(3);
        assertThat(page.getTotalElements()).isEqualTo(6);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testFindByCreatedAtAfter() {
        LocalDateTime date = baseDateTime.minusMinutes(1);
        
        List<AnnouncementReply> replies = announcementReplyRepository.findByCreatedAtAfter(date);
        assertThat(replies).hasSize(6);
        assertThat(replies).containsExactlyInAnyOrder(reply1, reply2, reply3, reply4, reply5, reply6);
    }

    @Test
    void testFindByCreatedAtAfterWithPagination() {
        LocalDateTime date = baseDateTime.minusMinutes(1);
        
        Page<AnnouncementReply> page = announcementReplyRepository.findByCreatedAtAfter(date, PageRequest.of(0, 3));
        assertThat(page.getContent()).hasSize(3);
        assertThat(page.getTotalElements()).isEqualTo(6);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testFindByCreatedAtBefore() {
        LocalDateTime date = baseDateTime.plusMinutes(1);
        
        List<AnnouncementReply> replies = announcementReplyRepository.findByCreatedAtBefore(date);
        assertThat(replies).hasSize(6);
        assertThat(replies).containsExactlyInAnyOrder(reply1, reply2, reply3, reply4, reply5, reply6);
    }

    @Test
    void testFindByCreatedAtBeforeWithPagination() {
        LocalDateTime date = baseDateTime.plusMinutes(1);
        
        Page<AnnouncementReply> page = announcementReplyRepository.findByCreatedAtBefore(date, PageRequest.of(0, 3));
        assertThat(page.getContent()).hasSize(3);
        assertThat(page.getTotalElements()).isEqualTo(6);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    // ==================== COMBINED QUERIES ====================

    @Test
    void testFindByAnnouncementAndUser() {
        List<AnnouncementReply> replies = announcementReplyRepository.findByAnnouncementAndUser(announcement1, employee1);
        assertThat(replies).hasSize(1);
        assertThat(replies).containsExactly(reply1);
    }

    @Test
    void testFindByAnnouncementAndUserWithPagination() {
        Page<AnnouncementReply> page = announcementReplyRepository.findByAnnouncementAndUser(announcement1, employee1, PageRequest.of(0, 1));
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getTotalPages()).isEqualTo(1);
    }

    @Test
    void testFindByAnnouncementIdAndUserId() {
        List<AnnouncementReply> replies = announcementReplyRepository.findByAnnouncementIdAndUserId(announcement1.getId(), employee1.getId());
        assertThat(replies).hasSize(1);
        assertThat(replies).containsExactly(reply1);
    }

    @Test
    void testFindByAnnouncementIdAndUserIdWithPagination() {
        Page<AnnouncementReply> page = announcementReplyRepository.findByAnnouncementIdAndUserId(announcement1.getId(), employee1.getId(), PageRequest.of(0, 1));
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getTotalPages()).isEqualTo(1);
    }

    // ==================== CUSTOM BUSINESS QUERIES ====================

    @Test
    void testFindRecentReplies() {
        LocalDateTime since = baseDateTime.minusMinutes(1);
        
        List<AnnouncementReply> recentReplies = announcementReplyRepository.findRecentReplies(since);
        assertThat(recentReplies).hasSize(6);
        // All replies are recent since they were created within the last minute
    }

    @Test
    void testFindRecentRepliesWithPagination() {
        LocalDateTime since = baseDateTime.minusMinutes(1);
        
        Page<AnnouncementReply> page = announcementReplyRepository.findRecentReplies(since, PageRequest.of(0, 3));
        assertThat(page.getContent()).hasSize(3);
        assertThat(page.getTotalElements()).isEqualTo(6);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testFindByAnnouncementOrderByCreatedAtAsc() {
        List<AnnouncementReply> replies = announcementReplyRepository.findByAnnouncementOrderByCreatedAtAsc(announcement1);
        assertThat(replies).hasSize(3);
        assertThat(replies).containsExactlyInAnyOrder(reply1, reply2, reply3);
    }

    @Test
    void testFindByAnnouncementOrderByCreatedAtAscWithPagination() {
        Page<AnnouncementReply> page = announcementReplyRepository.findByAnnouncementOrderByCreatedAtAsc(announcement1, PageRequest.of(0, 2));
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testFindByAnnouncementIdOrderByCreatedAtAsc() {
        List<AnnouncementReply> replies = announcementReplyRepository.findByAnnouncementIdOrderByCreatedAtAsc(announcement1.getId());
        assertThat(replies).hasSize(3);
        assertThat(replies).containsExactlyInAnyOrder(reply1, reply2, reply3);
    }

    @Test
    void testFindByAnnouncementIdOrderByCreatedAtAscWithPagination() {
        Page<AnnouncementReply> page = announcementReplyRepository.findByAnnouncementIdOrderByCreatedAtAsc(announcement1.getId(), PageRequest.of(0, 2));
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testFindByUserOrderByCreatedAtDesc() {
        List<AnnouncementReply> replies = announcementReplyRepository.findByUserOrderByCreatedAtDesc(employee1);
        assertThat(replies).hasSize(2);
        assertThat(replies).containsExactlyInAnyOrder(reply1, reply4);
    }

    @Test
    void testFindByUserOrderByCreatedAtDescWithPagination() {
        Page<AnnouncementReply> page = announcementReplyRepository.findByUserOrderByCreatedAtDesc(employee1, PageRequest.of(0, 1));
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testFindByUserIdOrderByCreatedAtDesc() {
        List<AnnouncementReply> replies = announcementReplyRepository.findByUserIdOrderByCreatedAtDesc(employee1.getId());
        assertThat(replies).hasSize(2);
        assertThat(replies).containsExactlyInAnyOrder(reply1, reply4);
    }

    @Test
    void testFindByUserIdOrderByCreatedAtDescWithPagination() {
        Page<AnnouncementReply> page = announcementReplyRepository.findByUserIdOrderByCreatedAtDesc(employee1.getId(), PageRequest.of(0, 1));
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    // ==================== COUNT QUERIES ====================

    @Test
    void testCountByAnnouncement() {
        long count = announcementReplyRepository.countByAnnouncement(announcement1);
        assertThat(count).isEqualTo(3);
    }

    @Test
    void testCountByAnnouncementId() {
        long count = announcementReplyRepository.countByAnnouncementId(announcement1.getId());
        assertThat(count).isEqualTo(3);
    }

    @Test
    void testCountByUser() {
        long count = announcementReplyRepository.countByUser(employee1);
        assertThat(count).isEqualTo(2);
    }

    @Test
    void testCountByUserId() {
        long count = announcementReplyRepository.countByUserId(employee1.getId());
        assertThat(count).isEqualTo(2);
    }

    @Test
    void testCountByCreatedAtBetween() {
        LocalDateTime startDate = baseDateTime.minusMinutes(1);
        LocalDateTime endDate = baseDateTime.plusMinutes(1);
        
        long count = announcementReplyRepository.countByCreatedAtBetween(startDate, endDate);
        assertThat(count).isEqualTo(6);
    }

    @Test
    void testCountByCreatedAtAfter() {
        LocalDateTime date = baseDateTime.minusMinutes(1);
        
        long count = announcementReplyRepository.countByCreatedAtAfter(date);
        assertThat(count).isEqualTo(6);
    }

    @Test
    void testCountByCreatedAtBefore() {
        LocalDateTime date = baseDateTime.plusMinutes(1);
        
        long count = announcementReplyRepository.countByCreatedAtBefore(date);
        assertThat(count).isEqualTo(6);
    }

    @Test
    void testCountByAnnouncementAndUser() {
        long count = announcementReplyRepository.countByAnnouncementAndUser(announcement1, employee1);
        assertThat(count).isEqualTo(1);
    }

    @Test
    void testCountByAnnouncementIdAndUserId() {
        long count = announcementReplyRepository.countByAnnouncementIdAndUserId(announcement1.getId(), employee1.getId());
        assertThat(count).isEqualTo(1);
    }

    // ==================== EXISTENCE QUERIES ====================

    @Test
    void testExistsByAnnouncement() {
        boolean exists = announcementReplyRepository.existsByAnnouncement(announcement1);
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByAnnouncementId() {
        boolean exists = announcementReplyRepository.existsByAnnouncementId(announcement1.getId());
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByUser() {
        boolean exists = announcementReplyRepository.existsByUser(employee1);
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByUserId() {
        boolean exists = announcementReplyRepository.existsByUserId(employee1.getId());
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByAnnouncementAndUser() {
        boolean exists = announcementReplyRepository.existsByAnnouncementAndUser(announcement1, employee1);
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByAnnouncementIdAndUserId() {
        boolean exists = announcementReplyRepository.existsByAnnouncementIdAndUserId(announcement1.getId(), employee1.getId());
        assertThat(exists).isTrue();
    }

    // ==================== AGGREGATION QUERIES ====================

    @Test
    void testFindLatestReplyByAnnouncement() {
        List<AnnouncementReply> latestReplies = announcementReplyRepository.findLatestReplyByAnnouncement(announcement1, PageRequest.of(0, 1));
        assertThat(latestReplies).hasSize(1);
        assertThat(latestReplies.get(0)).isIn(reply1, reply2, reply3);
    }

    @Test
    void testFindLatestReplyByAnnouncementId() {
        List<AnnouncementReply> latestReplies = announcementReplyRepository.findLatestReplyByAnnouncementId(announcement1.getId(), PageRequest.of(0, 1));
        assertThat(latestReplies).hasSize(1);
        assertThat(latestReplies.get(0)).isIn(reply1, reply2, reply3);
    }

    @Test
    void testGetReplyCountByAnnouncement() {
        List<Object[]> results = announcementReplyRepository.getReplyCountByAnnouncement();
        assertThat(results).hasSize(3); // 3 announcements
        
        // Find the result for announcement1 (should have 3 replies)
        Optional<Object[]> announcement1Result = results.stream()
            .filter(result -> ((Announcement) result[0]).getId().equals(announcement1.getId()))
            .findFirst();
        
        assertThat(announcement1Result).isPresent();
        assertThat(announcement1Result.get()[1]).isEqualTo(3L);
    }

    // ==================== EDGE CASES ====================

    @Test
    void testFindByNonExistentAnnouncement() {
        Announcement nonExistentAnnouncement = new Announcement("Non-existent", "This announcement doesn't exist.", admin1, AnnouncementPriority.LOW, AnnouncementStatus.PUBLISHED);
        entityManager.persistAndFlush(nonExistentAnnouncement);
        
        List<AnnouncementReply> replies = announcementReplyRepository.findByAnnouncement(nonExistentAnnouncement);
        assertThat(replies).isEmpty();
    }

    @Test
    void testFindByNonExistentUser() {
        User nonExistentUser = new User("Non", "Existent", "non@existent.com", "password123", "+1234567890", "Nowhere", LocalDate.of(1990, 1, 1), Role.CLIENT);
        entityManager.persistAndFlush(nonExistentUser);
        
        List<AnnouncementReply> replies = announcementReplyRepository.findByUser(nonExistentUser);
        assertThat(replies).isEmpty();
    }

    @Test
    void testCountByNonExistentAnnouncement() {
        Announcement nonExistentAnnouncement = new Announcement("Non-existent", "This announcement doesn't exist.", admin1, AnnouncementPriority.LOW, AnnouncementStatus.PUBLISHED);
        entityManager.persistAndFlush(nonExistentAnnouncement);
        
        long count = announcementReplyRepository.countByAnnouncement(nonExistentAnnouncement);
        assertThat(count).isEqualTo(0);
    }

    @Test
    void testExistsByNonExistentAnnouncement() {
        Announcement nonExistentAnnouncement = new Announcement("Non-existent", "This announcement doesn't exist.", admin1, AnnouncementPriority.LOW, AnnouncementStatus.PUBLISHED);
        entityManager.persistAndFlush(nonExistentAnnouncement);
        
        boolean exists = announcementReplyRepository.existsByAnnouncement(nonExistentAnnouncement);
        assertThat(exists).isFalse();
    }

    @Test
    void testFindByDateRangeWithNoResults() {
        LocalDateTime futureDate = baseDateTime.plusDays(1);
        LocalDateTime farFutureDate = baseDateTime.plusDays(2);
        
        List<AnnouncementReply> replies = announcementReplyRepository.findByCreatedAtBetween(futureDate, farFutureDate);
        assertThat(replies).isEmpty();
    }
}
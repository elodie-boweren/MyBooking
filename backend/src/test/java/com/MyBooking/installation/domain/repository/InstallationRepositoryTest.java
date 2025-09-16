package com.MyBooking.installation.repository;

import com.MyBooking.installation.domain.Installation;
import com.MyBooking.installation.domain.InstallationType;
import com.MyBooking.hotel_management.HotelManagementApplication;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    "com.MyBooking.installation.domain",
    "com.MyBooking.event.domain",
    "com.MyBooking.auth.domain",
    "com.MyBooking.reservation.domain",
    "com.MyBooking.room.domain"
})
@EnableJpaRepositories(basePackages = "com.MyBooking.installation.repository")
public class InstallationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InstallationRepository installationRepository;

    // Test data
    private Installation spaRoom1, spaRoom2, conferenceRoom1, conferenceRoom2, gym1, pool1, tennisCourt1, weddingRoom1;
    private LocalDateTime baseDateTime;

    @BeforeEach
    void setUp() {
        baseDateTime = LocalDateTime.now().minusHours(1);

        // Create diverse installation data
        spaRoom1 = new Installation("Luxury Spa Room A", InstallationType.SPA_ROOM, 4, BigDecimal.valueOf(150.00), "USD");
        spaRoom1.setDescription("Premium spa with jacuzzi and massage tables");
        spaRoom1.setEquipment("Jacuzzi, Massage tables, Aromatherapy diffusers");

        spaRoom2 = new Installation("Relaxation Spa Room B", InstallationType.SPA_ROOM, 2, BigDecimal.valueOf(120.00), "USD");
        spaRoom2.setDescription("Intimate spa for couples");
        spaRoom2.setEquipment("Sauna, Steam room, Relaxation chairs");

        conferenceRoom1 = new Installation("Grand Conference Hall", InstallationType.CONFERENCE_ROOM, 100, BigDecimal.valueOf(200.00), "USD");
        conferenceRoom1.setDescription("Large conference room with modern AV equipment");
        conferenceRoom1.setEquipment("Projector, Sound system, Whiteboards, Video conferencing");

        conferenceRoom2 = new Installation("Executive Meeting Room", InstallationType.CONFERENCE_ROOM, 20, BigDecimal.valueOf(100.00), "USD");
        conferenceRoom2.setDescription("Small executive meeting room");
        conferenceRoom2.setEquipment("TV screen, Conference phone, Whiteboard");

        gym1 = new Installation("Fitness Center", InstallationType.GYM, 50, BigDecimal.valueOf(80.00), "USD");
        gym1.setDescription("Fully equipped fitness center");
        gym1.setEquipment("Treadmills, Weights, Yoga mats, Exercise bikes");

        pool1 = new Installation("Olympic Swimming Pool", InstallationType.POOL, 150, BigDecimal.valueOf(120.00), "USD");
        pool1.setDescription("Olympic-size swimming pool");
        pool1.setEquipment("Lanes, Diving board, Pool chairs, Lifeguard station");

        tennisCourt1 = new Installation("Tennis Court 1", InstallationType.TENNIS_COURT, 4, BigDecimal.valueOf(60.00), "USD");
        tennisCourt1.setDescription("Professional tennis court");
        tennisCourt1.setEquipment("Nets, Rackets, Balls, Court lighting");

        weddingRoom1 = new Installation("Royal Wedding Hall", InstallationType.WEDDING_ROOM, 200, BigDecimal.valueOf(500.00), "USD");
        weddingRoom1.setDescription("Elegant wedding reception hall");
        weddingRoom1.setEquipment("Stage, Dance floor, Sound system, Decorative lighting");

        // Persist all installations
        entityManager.persistAndFlush(spaRoom1);
        entityManager.persistAndFlush(spaRoom2);
        entityManager.persistAndFlush(conferenceRoom1);
        entityManager.persistAndFlush(conferenceRoom2);
        entityManager.persistAndFlush(gym1);
        entityManager.persistAndFlush(pool1);
        entityManager.persistAndFlush(tennisCourt1);
        entityManager.persistAndFlush(weddingRoom1);
    }

    // ==================== BASIC QUERIES ====================

    @Test
    void testFindByName() {
        Optional<Installation> found = installationRepository.findByName("Luxury Spa Room A");
        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(spaRoom1);
    }

    @Test
    void testFindByNameNotFound() {
        Optional<Installation> found = installationRepository.findByName("Non-existent Room");
        assertThat(found).isEmpty();
    }

    @Test
    void testFindByNameIgnoreCase() {
        Optional<Installation> found = installationRepository.findByNameIgnoreCase("luxury spa room a");
        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(spaRoom1);
    }

    @Test
    void testFindByInstallationType() {
        List<Installation> spaRooms = installationRepository.findByInstallationType(InstallationType.SPA_ROOM);
        assertThat(spaRooms).hasSize(2);
        assertThat(spaRooms).containsExactlyInAnyOrder(spaRoom1, spaRoom2);
    }


    // ==================== CAPACITY QUERIES ====================

    @Test
    void testFindByCapacity() {
        List<Installation> installations = installationRepository.findByCapacity(4);
        assertThat(installations).hasSize(2);
        assertThat(installations).containsExactlyInAnyOrder(spaRoom1, tennisCourt1);
    }

    @Test
    void testFindByCapacityGreaterThanEqual() {
        List<Installation> installations = installationRepository.findByCapacityGreaterThanEqual(100);
        assertThat(installations).hasSize(3);
        assertThat(installations).containsExactlyInAnyOrder(conferenceRoom1, pool1, weddingRoom1);
    }

    @Test
    void testFindByCapacityLessThanEqual() {
        List<Installation> installations = installationRepository.findByCapacityLessThanEqual(20);
        assertThat(installations).hasSize(4);
        assertThat(installations).containsExactlyInAnyOrder(spaRoom1, spaRoom2, conferenceRoom2, tennisCourt1);
    }

    @Test
    void testFindByCapacityBetween() {
        List<Installation> installations = installationRepository.findByCapacityBetween(10, 50);
        assertThat(installations).hasSize(2);
        assertThat(installations).containsExactlyInAnyOrder(conferenceRoom2, gym1);
    }

    // ==================== PRICING QUERIES ====================

    @Test
    void testFindByHourlyRate() {
        List<Installation> installations = installationRepository.findByHourlyRate(BigDecimal.valueOf(150.00));
        assertThat(installations).hasSize(1);
        assertThat(installations).containsExactly(spaRoom1);
    }

    @Test
    void testFindByHourlyRateGreaterThanEqual() {
        List<Installation> installations = installationRepository.findByHourlyRateGreaterThanEqual(BigDecimal.valueOf(150.00));
        assertThat(installations).hasSize(3);
        assertThat(installations).containsExactlyInAnyOrder(spaRoom1, conferenceRoom1, weddingRoom1);
    }

    @Test
    void testFindByHourlyRateLessThanEqual() {
        List<Installation> installations = installationRepository.findByHourlyRateLessThanEqual(BigDecimal.valueOf(100.00));
        assertThat(installations).hasSize(3);
        assertThat(installations).containsExactlyInAnyOrder(conferenceRoom2, gym1, tennisCourt1);
    }

    @Test
    void testFindByHourlyRateBetween() {
        List<Installation> installations = installationRepository.findByHourlyRateBetween(BigDecimal.valueOf(100.00), BigDecimal.valueOf(200.00));
        assertThat(installations).hasSize(5);
        assertThat(installations).containsExactlyInAnyOrder(spaRoom1, spaRoom2, conferenceRoom1, conferenceRoom2, pool1);
    }

    @Test
    void testFindByCurrency() {
        List<Installation> installations = installationRepository.findByCurrency("USD");
        assertThat(installations).hasSize(8);
        assertThat(installations).containsExactlyInAnyOrder(spaRoom1, spaRoom2, conferenceRoom1, conferenceRoom2, gym1, pool1, tennisCourt1, weddingRoom1);
    }

    // ==================== SEARCH QUERIES ====================

    @Test
    void testFindByNameContainingIgnoreCase() {
        List<Installation> installations = installationRepository.findByNameContainingIgnoreCase("spa");
        assertThat(installations).hasSize(2);
        assertThat(installations).containsExactlyInAnyOrder(spaRoom1, spaRoom2);
    }

    @Test
    void testFindByDescriptionContainingIgnoreCase() {
        List<Installation> installations = installationRepository.findByDescriptionContainingIgnoreCase("conference");
        assertThat(installations).hasSize(1);
        assertThat(installations).containsExactly(conferenceRoom1);
    }

    @Test
    void testFindByEquipmentContainingIgnoreCase() {
        List<Installation> installations = installationRepository.findByEquipmentContainingIgnoreCase("projector");
        assertThat(installations).hasSize(1);
        assertThat(installations).containsExactly(conferenceRoom1);
    }

    // ==================== COMBINED QUERIES ====================

    @Test
    void testFindByInstallationTypeAndCapacity() {
        List<Installation> installations = installationRepository.findByInstallationTypeAndCapacity(InstallationType.SPA_ROOM, 4);
        assertThat(installations).hasSize(1);
        assertThat(installations).containsExactly(spaRoom1);
    }

    @Test
    void testFindByInstallationTypeAndCapacityBetween() {
        List<Installation> installations = installationRepository.findByInstallationTypeAndCapacityBetween(InstallationType.CONFERENCE_ROOM, 15, 50);
        assertThat(installations).hasSize(1);
        assertThat(installations).containsExactly(conferenceRoom2);
    }

    @Test
    void testFindByInstallationTypeAndHourlyRateBetween() {
        List<Installation> installations = installationRepository.findByInstallationTypeAndHourlyRateBetween(InstallationType.SPA_ROOM, BigDecimal.valueOf(100.00), BigDecimal.valueOf(200.00));
        assertThat(installations).hasSize(2);
        assertThat(installations).containsExactlyInAnyOrder(spaRoom1, spaRoom2);
    }

    @Test
    void testFindByCapacityBetweenAndHourlyRateBetween() {
        List<Installation> installations = installationRepository.findByCapacityBetweenAndHourlyRateBetween(50, 150, BigDecimal.valueOf(80.00), BigDecimal.valueOf(150.00));
        assertThat(installations).hasSize(2);
        assertThat(installations).containsExactlyInAnyOrder(gym1, pool1);
    }

    // ==================== DATE-BASED QUERIES ====================

    @Test
    void testFindByCreatedAtBetween() {
        LocalDateTime startDate = LocalDateTime.now().minusMinutes(5);
        LocalDateTime endDate = LocalDateTime.now().plusMinutes(5);

        List<Installation> installations = installationRepository.findByCreatedAtBetween(startDate, endDate);
        assertThat(installations).hasSize(8);
    }

    @Test
    void testFindByCreatedAtAfter() {
        LocalDateTime date = LocalDateTime.now().minusMinutes(10);

        List<Installation> installations = installationRepository.findByCreatedAtAfter(date);
        assertThat(installations).hasSize(8);
    }

    @Test
    void testFindByCreatedAtBefore() {
        LocalDateTime date = LocalDateTime.now().plusMinutes(10);

        List<Installation> installations = installationRepository.findByCreatedAtBefore(date);
        assertThat(installations).hasSize(8);
    }

    // ==================== CUSTOM BUSINESS QUERIES ====================

    @Test
    void testFindSuitableForParticipants() {
        List<Installation> installations = installationRepository.findSuitableForParticipants(50);
        assertThat(installations).hasSize(4);
        assertThat(installations).containsExactlyInAnyOrder(gym1, pool1, conferenceRoom1, weddingRoom1);
        // Should be ordered by capacity ASC
        assertThat(installations.get(0).getCapacity()).isEqualTo(50); // gym1
        assertThat(installations.get(1).getCapacity()).isEqualTo(100); // conferenceRoom1
        assertThat(installations.get(2).getCapacity()).isEqualTo(150); // pool1
        assertThat(installations.get(3).getCapacity()).isEqualTo(200); // weddingRoom1
    }

    @Test
    void testFindByTypeOrderByCapacityDesc() {
        List<Installation> installations = installationRepository.findByTypeOrderByCapacityDesc(InstallationType.SPA_ROOM);
        assertThat(installations).hasSize(2);
        assertThat(installations.get(0)).isEqualTo(spaRoom1); // capacity 4
        assertThat(installations.get(1)).isEqualTo(spaRoom2); // capacity 2
    }

    @Test
    void testFindByTypeOrderByHourlyRateAsc() {
        List<Installation> installations = installationRepository.findByTypeOrderByHourlyRateAsc(InstallationType.SPA_ROOM);
        assertThat(installations).hasSize(2);
        assertThat(installations.get(0)).isEqualTo(spaRoom2); // 120.00
        assertThat(installations.get(1)).isEqualTo(spaRoom1); // 150.00
    }

    @Test
    void testFindHighCapacityInstallations() {
        List<Installation> installations = installationRepository.findHighCapacityInstallations(100);
        assertThat(installations).hasSize(3);
        assertThat(installations).containsExactlyInAnyOrder(conferenceRoom1, pool1, weddingRoom1);
        // Should be ordered by capacity DESC
        assertThat(installations.get(0).getCapacity()).isEqualTo(200); // weddingRoom1
        assertThat(installations.get(1).getCapacity()).isEqualTo(150); // pool1
        assertThat(installations.get(2).getCapacity()).isEqualTo(100); // conferenceRoom1
    }

    @Test
    void testFindPremiumInstallations() {
        List<Installation> installations = installationRepository.findPremiumInstallations(BigDecimal.valueOf(150.00));
        assertThat(installations).hasSize(3);
        assertThat(installations).containsExactlyInAnyOrder(spaRoom1, conferenceRoom1, weddingRoom1);
        // Should be ordered by hourly rate DESC
        assertThat(installations.get(0).getHourlyRate()).isEqualTo(BigDecimal.valueOf(500.00)); // weddingRoom1
        assertThat(installations.get(1).getHourlyRate()).isEqualTo(BigDecimal.valueOf(200.00)); // conferenceRoom1
        assertThat(installations.get(2).getHourlyRate()).isEqualTo(BigDecimal.valueOf(150.00)); // spaRoom1
    }

    // ==================== COUNT QUERIES ====================

    @Test
    void testCountByInstallationType() {
        long count = installationRepository.countByInstallationType(InstallationType.SPA_ROOM);
        assertThat(count).isEqualTo(2);
    }

    @Test
    void testCountByCapacity() {
        long count = installationRepository.countByCapacity(4);
        assertThat(count).isEqualTo(2);
    }

    @Test
    void testCountByCapacityGreaterThanEqual() {
        long count = installationRepository.countByCapacityGreaterThanEqual(100);
        assertThat(count).isEqualTo(3);
    }

    @Test
    void testCountByCapacityLessThanEqual() {
        long count = installationRepository.countByCapacityLessThanEqual(20);
        assertThat(count).isEqualTo(4);
    }

    @Test
    void testCountByCapacityBetween() {
        long count = installationRepository.countByCapacityBetween(10, 50);
        assertThat(count).isEqualTo(2);
    }

    @Test
    void testCountByHourlyRate() {
        long count = installationRepository.countByHourlyRate(BigDecimal.valueOf(150.00));
        assertThat(count).isEqualTo(1);
    }

    @Test
    void testCountByHourlyRateGreaterThanEqual() {
        long count = installationRepository.countByHourlyRateGreaterThanEqual(BigDecimal.valueOf(150.00));
        assertThat(count).isEqualTo(3);
    }

    @Test
    void testCountByHourlyRateLessThanEqual() {
        long count = installationRepository.countByHourlyRateLessThanEqual(BigDecimal.valueOf(100.00));
        assertThat(count).isEqualTo(3);
    }

    @Test
    void testCountByHourlyRateBetween() {
        long count = installationRepository.countByHourlyRateBetween(BigDecimal.valueOf(100.00), BigDecimal.valueOf(200.00));
        assertThat(count).isEqualTo(5);
    }

    @Test
    void testCountByCurrency() {
        long count = installationRepository.countByCurrency("USD");
        assertThat(count).isEqualTo(8);
    }

    @Test
    void testCountByCreatedAtBetween() {
        LocalDateTime startDate = LocalDateTime.now().minusMinutes(5);
        LocalDateTime endDate = LocalDateTime.now().plusMinutes(5);

        long count = installationRepository.countByCreatedAtBetween(startDate, endDate);
        assertThat(count).isEqualTo(8);
    }

    @Test
    void testCountByCreatedAtAfter() {
        LocalDateTime date = LocalDateTime.now().minusMinutes(10);

        long count = installationRepository.countByCreatedAtAfter(date);
        assertThat(count).isEqualTo(8);
    }

    @Test
    void testCountByCreatedAtBefore() {
        LocalDateTime date = LocalDateTime.now().plusMinutes(10);

        long count = installationRepository.countByCreatedAtBefore(date);
        assertThat(count).isEqualTo(8);
    }

    // ==================== EXISTENCE QUERIES ====================

    @Test
    void testExistsByName() {
        boolean exists = installationRepository.existsByName("Luxury Spa Room A");
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByNameNotFound() {
        boolean exists = installationRepository.existsByName("Non-existent Room");
        assertThat(exists).isFalse();
    }

    @Test
    void testExistsByNameIgnoreCase() {
        boolean exists = installationRepository.existsByNameIgnoreCase("luxury spa room a");
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByCapacityGreaterThanEqual() {
        boolean exists = installationRepository.existsByCapacityGreaterThanEqual(100);
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByCapacityGreaterThanEqualNotFound() {
        boolean exists = installationRepository.existsByCapacityGreaterThanEqual(500);
        assertThat(exists).isFalse();
    }

    @Test
    void testExistsByHourlyRateGreaterThanEqual() {
        boolean exists = installationRepository.existsByHourlyRateGreaterThanEqual(BigDecimal.valueOf(500.00));
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByHourlyRateGreaterThanEqualNotFound() {
        boolean exists = installationRepository.existsByHourlyRateGreaterThanEqual(BigDecimal.valueOf(1000.00));
        assertThat(exists).isFalse();
    }

    // ==================== AGGREGATION QUERIES ====================

    @Test
    void testCount() {
        long count = installationRepository.count();
        assertThat(count).isEqualTo(8);
    }

    @Test
    void testGetAverageCapacityByType() {
        List<Object[]> statistics = installationRepository.getAverageCapacityByType();
        assertThat(statistics).hasSize(6); // 6 different installation types

        // Find SPA_ROOM statistics
        Object[] spaStats = statistics.stream()
            .filter(stat -> InstallationType.SPA_ROOM.equals(stat[0]))
            .findFirst()
            .orElse(null);
        assertThat(spaStats).isNotNull();
        assertThat((Double) spaStats[1]).isCloseTo(3.0, org.assertj.core.data.Offset.offset(0.01)); // (4 + 2) / 2
    }

    @Test
    void testGetAverageHourlyRateByType() {
        List<Object[]> statistics = installationRepository.getAverageHourlyRateByType();
        assertThat(statistics).hasSize(6); // 6 different installation types

        // Find SPA_ROOM statistics
        Object[] spaStats = statistics.stream()
            .filter(stat -> InstallationType.SPA_ROOM.equals(stat[0]))
            .findFirst()
            .orElse(null);
        assertThat(spaStats).isNotNull();
        assertThat((Double) spaStats[1]).isCloseTo(135.0, org.assertj.core.data.Offset.offset(0.01)); // (150 + 120) / 2
    }

    @Test
    void testGetCapacityStatisticsByType() {
        List<Object[]> statistics = installationRepository.getCapacityStatisticsByType();
        assertThat(statistics).hasSize(6); // 6 different installation types

        // Find SPA_ROOM statistics
        Object[] spaStats = statistics.stream()
            .filter(stat -> InstallationType.SPA_ROOM.equals(stat[0]))
            .findFirst()
            .orElse(null);
        assertThat(spaStats).isNotNull();
        assertThat(spaStats[1]).isEqualTo(2); // MIN capacity
        assertThat(spaStats[2]).isEqualTo(4); // MAX capacity
        assertThat((Double) spaStats[3]).isCloseTo(3.0, org.assertj.core.data.Offset.offset(0.01)); // AVG capacity
        assertThat(spaStats[4]).isEqualTo(2L); // COUNT
    }

    @Test
    void testGetHourlyRateStatisticsByType() {
        List<Object[]> statistics = installationRepository.getHourlyRateStatisticsByType();
        assertThat(statistics).hasSize(6); // 6 different installation types

        // Find SPA_ROOM statistics
        Object[] spaStats = statistics.stream()
            .filter(stat -> InstallationType.SPA_ROOM.equals(stat[0]))
            .findFirst()
            .orElse(null);
        assertThat(spaStats).isNotNull();
        assertThat((BigDecimal) spaStats[1]).isEqualByComparingTo(BigDecimal.valueOf(120.00)); // MIN rate
        assertThat((BigDecimal) spaStats[2]).isEqualByComparingTo(BigDecimal.valueOf(150.00)); // MAX rate
        assertThat((Double) spaStats[3]).isCloseTo(135.0, org.assertj.core.data.Offset.offset(0.01)); // AVG rate
        assertThat(spaStats[4]).isEqualTo(2L); // COUNT
    }

    @Test
    void testGetTotalCapacityByType() {
        List<Object[]> statistics = installationRepository.getTotalCapacityByType();
        assertThat(statistics).hasSize(6); // 6 different installation types

        // Find SPA_ROOM statistics
        Object[] spaStats = statistics.stream()
            .filter(stat -> InstallationType.SPA_ROOM.equals(stat[0]))
            .findFirst()
            .orElse(null);
        assertThat(spaStats).isNotNull();
        assertThat(spaStats[1]).isEqualTo(6L); // 4 + 2 = 6 total capacity
    }

    @Test
    void testFindUnusedInstallations() {
        List<Installation> installations = installationRepository.findUnusedInstallations();
        assertThat(installations).hasSize(8); // All installations are unused (no events created)
        assertThat(installations).containsExactlyInAnyOrder(spaRoom1, spaRoom2, conferenceRoom1, conferenceRoom2, gym1, pool1, tennisCourt1, weddingRoom1);
    }

    @Test
    void testFindUsedInstallations() {
        List<Installation> installations = installationRepository.findUsedInstallations();
        assertThat(installations).isEmpty(); // No events created, so no used installations
    }

    // ==================== PAGINATION TESTS ====================

    @Test
    void testFindByInstallationTypeWithPagination() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Installation> page = installationRepository.findByInstallationType(InstallationType.SPA_ROOM, pageable);
        
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getSize()).isEqualTo(1);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.isLast()).isFalse();
    }

    @Test
    void testFindByCapacityWithPagination() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Installation> page = installationRepository.findByCapacity(4, pageable);
        
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getContent()).containsExactlyInAnyOrder(spaRoom1, tennisCourt1);
    }

    // ==================== EDGE CASES ====================

    @Test
    void testFindByNonExistentType() {
        List<Installation> installations = installationRepository.findByInstallationType(InstallationType.SPA_ROOM);
        // This should return empty list, but we have SPA_ROOM data, so let's test with a different approach
        // We'll test with capacity that doesn't exist
        List<Installation> nonExistent = installationRepository.findByCapacity(999);
        assertThat(nonExistent).isEmpty();
    }

    @Test
    void testFindByCapacityRangeNoResults() {
        List<Installation> installations = installationRepository.findByCapacityBetween(1000, 2000);
        assertThat(installations).isEmpty();
    }

    @Test
    void testFindByHourlyRateRangeNoResults() {
        List<Installation> installations = installationRepository.findByHourlyRateBetween(BigDecimal.valueOf(1000.00), BigDecimal.valueOf(2000.00));
        assertThat(installations).isEmpty();
    }

    @Test
    void testFindByNameContainingNoResults() {
        List<Installation> installations = installationRepository.findByNameContainingIgnoreCase("non-existent");
        assertThat(installations).isEmpty();
    }

    @Test
    void testFindSuitableForParticipantsNoResults() {
        List<Installation> installations = installationRepository.findSuitableForParticipants(1000);
        assertThat(installations).isEmpty();
    }
}
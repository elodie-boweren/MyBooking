package com.MyBooking.loyalty.repository;

import com.MyBooking.loyalty.domain.LoyaltyAccount;
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
    "com.MyBooking.loyalty.domain",
    "com.MyBooking.auth.domain",
    "com.MyBooking.reservation.domain",
    "com.MyBooking.room.domain"
})
@EnableJpaRepositories(basePackages = "com.MyBooking.loyalty.repository")
class LoyaltyAccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LoyaltyAccountRepository loyaltyAccountRepository;

    // Test data
    private User client1, client2, client3, employee1, admin1;
    private LoyaltyAccount account1, account2, account3, account4, account5;
    private LocalDateTime baseDateTime;

    @BeforeEach
    void setUp() {
        // Create test users
        client1 = new User("Client", "One", "client.one@hotel.com", "clientpass123", "+1112223333", "Client Address 1", LocalDate.of(1990, 1, 1), Role.CLIENT);
        client2 = new User("Client", "Two", "client.two@hotel.com", "clientpass123", "+4445556666", "Client Address 2", LocalDate.of(1992, 2, 2), Role.CLIENT);
        client3 = new User("Client", "Three", "client.three@hotel.com", "clientpass123", "+7778889999", "Client Address 3", LocalDate.of(1994, 3, 3), Role.CLIENT);
        employee1 = new User("Employee", "One", "employee.one@hotel.com", "emppass123", "+3334445555", "Employee Address", LocalDate.of(1985, 4, 4), Role.EMPLOYEE);
        admin1 = new User("Admin", "One", "admin.one@hotel.com", "adminpass123", "+9998887777", "Admin Address", LocalDate.of(1980, 5, 5), Role.ADMIN);

        // Set base date time
        baseDateTime = LocalDateTime.now();

        // Persist test users
        entityManager.persistAndFlush(client1);
        entityManager.persistAndFlush(client2);
        entityManager.persistAndFlush(client3);
        entityManager.persistAndFlush(employee1);
        entityManager.persistAndFlush(admin1);

        // Create test loyalty accounts
        account1 = new LoyaltyAccount(client1, 100);
        account2 = new LoyaltyAccount(client2, 250);
        account3 = new LoyaltyAccount(client3, 0);
        account4 = new LoyaltyAccount(employee1, 500);
        account5 = new LoyaltyAccount(admin1, 1000);

        // Persist accounts
        entityManager.persistAndFlush(account1);
        entityManager.persistAndFlush(account2);
        entityManager.persistAndFlush(account3);
        entityManager.persistAndFlush(account4);
        entityManager.persistAndFlush(account5);
    }

    // ==================== BASIC QUERIES ====================

    @Test
    void testFindByUser() {
        Optional<LoyaltyAccount> found = loyaltyAccountRepository.findByUser(client1);
        assertThat(found).isPresent();
        assertThat(found.get().getUser()).isEqualTo(client1);
        assertThat(found.get().getBalance()).isEqualTo(100);
    }

    @Test
    void testFindByUserId() {
        Optional<LoyaltyAccount> found = loyaltyAccountRepository.findByUserId(client2.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getUser()).isEqualTo(client2);
        assertThat(found.get().getBalance()).isEqualTo(250);
    }

    @Test
    void testFindByUserNotFound() {
        User nonExistentUser = new User("Non", "Existent", "nonexistent@hotel.com", "password123", "+1234567890", "Address", LocalDate.of(2000, 1, 1), Role.CLIENT);
        entityManager.persistAndFlush(nonExistentUser);
        
        Optional<LoyaltyAccount> found = loyaltyAccountRepository.findByUser(nonExistentUser);
        assertThat(found).isEmpty();
    }

    // ==================== BALANCE QUERIES ====================

    @Test
    void testFindByBalance() {
        List<LoyaltyAccount> accounts = loyaltyAccountRepository.findByBalance(100);
        assertThat(accounts).hasSize(1);
        assertThat(accounts.get(0).getUser()).isEqualTo(client1);
    }

    @Test
    void testFindByBalanceGreaterThan() {
        List<LoyaltyAccount> accounts = loyaltyAccountRepository.findByBalanceGreaterThan(200);
        assertThat(accounts).hasSize(3);
        assertThat(accounts).extracting("user").containsExactlyInAnyOrder(client2, employee1, admin1);
    }

    @Test
    void testFindByBalanceGreaterThanEqual() {
        List<LoyaltyAccount> accounts = loyaltyAccountRepository.findByBalanceGreaterThanEqual(250);
        assertThat(accounts).hasSize(3);
        assertThat(accounts).extracting("user").containsExactlyInAnyOrder(client2, employee1, admin1);
    }

    @Test
    void testFindByBalanceLessThan() {
        List<LoyaltyAccount> accounts = loyaltyAccountRepository.findByBalanceLessThan(200);
        assertThat(accounts).hasSize(2);
        assertThat(accounts).extracting("user").containsExactlyInAnyOrder(client1, client3);
    }

    @Test
    void testFindByBalanceLessThanEqual() {
        List<LoyaltyAccount> accounts = loyaltyAccountRepository.findByBalanceLessThanEqual(100);
        assertThat(accounts).hasSize(2);
        assertThat(accounts).extracting("user").containsExactlyInAnyOrder(client1, client3);
    }

    @Test
    void testFindByBalanceBetween() {
        List<LoyaltyAccount> accounts = loyaltyAccountRepository.findByBalanceBetween(100, 500);
        assertThat(accounts).hasSize(3);
        assertThat(accounts).extracting("user").containsExactlyInAnyOrder(client1, client2, employee1);
    }

    // ==================== DATE-BASED QUERIES ====================

    @Test
    void testFindByCreatedAtBetween() {
        LocalDateTime startDate = baseDateTime.minusMinutes(1);
        LocalDateTime endDate = baseDateTime.plusMinutes(1);
        
        List<LoyaltyAccount> accounts = loyaltyAccountRepository.findByCreatedAtBetween(startDate, endDate);
        assertThat(accounts).hasSize(5);
    }

    @Test
    void testFindByCreatedAtAfter() {
        LocalDateTime pastDate = baseDateTime.minusMinutes(1);
        
        List<LoyaltyAccount> accounts = loyaltyAccountRepository.findByCreatedAtAfter(pastDate);
        assertThat(accounts).hasSize(5);
    }

    @Test
    void testFindByCreatedAtBefore() {
        LocalDateTime futureDate = baseDateTime.plusMinutes(1);
        
        List<LoyaltyAccount> accounts = loyaltyAccountRepository.findByCreatedAtBefore(futureDate);
        assertThat(accounts).hasSize(5);
    }

    // ==================== CUSTOM BUSINESS QUERIES ====================

    @Test
    void testFindAccountsWithZeroBalance() {
        List<LoyaltyAccount> accounts = loyaltyAccountRepository.findAccountsWithZeroBalance();
        assertThat(accounts).hasSize(1);
        assertThat(accounts.get(0).getUser()).isEqualTo(client3);
    }

    @Test
    void testFindAccountsWithHighBalance() {
        List<LoyaltyAccount> accounts = loyaltyAccountRepository.findAccountsWithHighBalance(200);
        assertThat(accounts).hasSize(3);
        assertThat(accounts).extracting("user").containsExactlyInAnyOrder(client2, employee1, admin1);
    }

    @Test
    void testFindAllOrderByBalanceDesc() {
        List<LoyaltyAccount> accounts = loyaltyAccountRepository.findAllOrderByBalanceDesc();
        assertThat(accounts).hasSize(5);
        assertThat(accounts.get(0).getBalance()).isEqualTo(1000); // admin1
        assertThat(accounts.get(1).getBalance()).isEqualTo(500);  // employee1
        assertThat(accounts.get(2).getBalance()).isEqualTo(250);  // client2
        assertThat(accounts.get(3).getBalance()).isEqualTo(100);  // client1
        assertThat(accounts.get(4).getBalance()).isEqualTo(0);    // client3
    }

    @Test
    void testFindAllOrderByCreatedAtDesc() {
        List<LoyaltyAccount> accounts = loyaltyAccountRepository.findAllOrderByCreatedAtDesc();
        assertThat(accounts).hasSize(5);
        // All created at same time, so order is not deterministic
        assertThat(accounts).extracting("user").containsExactlyInAnyOrder(client1, client2, client3, employee1, admin1);
    }

    // ==================== COUNT QUERIES ====================

    @Test
    void testCountByBalance() {
        long count = loyaltyAccountRepository.countByBalance(100);
        assertThat(count).isEqualTo(1);
    }

    @Test
    void testCountByBalanceGreaterThan() {
        long count = loyaltyAccountRepository.countByBalanceGreaterThan(200);
        assertThat(count).isEqualTo(3);
    }

    @Test
    void testCountByBalanceBetween() {
        long count = loyaltyAccountRepository.countByBalanceBetween(100, 500);
        assertThat(count).isEqualTo(3);
    }

    @Test
    void testCountByCreatedAtBetween() {
        LocalDateTime startDate = baseDateTime.minusMinutes(1);
        LocalDateTime endDate = baseDateTime.plusMinutes(1);
        
        long count = loyaltyAccountRepository.countByCreatedAtBetween(startDate, endDate);
        assertThat(count).isEqualTo(5);
    }

    // ==================== EXISTENCE QUERIES ====================

    @Test
    void testExistsByUser() {
        boolean exists = loyaltyAccountRepository.existsByUser(client1);
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByUserId() {
        boolean exists = loyaltyAccountRepository.existsByUserId(client2.getId());
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByUserNotFound() {
        User nonExistentUser = new User("Non", "Existent", "nonexistent2@hotel.com", "password123", "+1234567891", "Address", LocalDate.of(2000, 1, 1), Role.CLIENT);
        entityManager.persistAndFlush(nonExistentUser);
        
        boolean exists = loyaltyAccountRepository.existsByUser(nonExistentUser);
        assertThat(exists).isFalse();
    }

    @Test
    void testExistsByBalance() {
        boolean exists = loyaltyAccountRepository.existsByBalance(100);
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByBalanceGreaterThan() {
        boolean exists = loyaltyAccountRepository.existsByBalanceGreaterThan(200);
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByBalanceBetween() {
        boolean exists = loyaltyAccountRepository.existsByBalanceBetween(100, 500);
        assertThat(exists).isTrue();
    }

    // ==================== AGGREGATION QUERIES ====================

    @Test
    void testGetTotalBalance() {
        Long totalBalance = loyaltyAccountRepository.getTotalBalance();
        assertThat(totalBalance).isEqualTo(1850L); // 100 + 250 + 0 + 500 + 1000
    }

    @Test
    void testGetAverageBalance() {
        Double averageBalance = loyaltyAccountRepository.getAverageBalance();
        assertThat(averageBalance).isEqualTo(370.0); // 1850 / 5
    }

    @Test
    void testGetMaximumBalance() {
        Integer maxBalance = loyaltyAccountRepository.getMaximumBalance();
        assertThat(maxBalance).isEqualTo(1000);
    }

    @Test
    void testGetMinimumBalance() {
        Integer minBalance = loyaltyAccountRepository.getMinimumBalance();
        assertThat(minBalance).isEqualTo(0);
    }

    @Test
    void testGetBalanceStatisticsByUserRole() {
        List<Object[]> statistics = loyaltyAccountRepository.getBalanceStatisticsByUserRole();
        assertThat(statistics).hasSize(3); // CLIENT, EMPLOYEE, ADMIN
        
        // Find CLIENT statistics
        Object[] clientStats = statistics.stream()
            .filter(stat -> Role.CLIENT.toString().equals(stat[0].toString()) || Role.CLIENT.equals(stat[0]))
            .findFirst()
            .orElse(null);
        assertThat(clientStats).isNotNull();
        assertThat(clientStats[1]).isEqualTo(350L); // 100 + 250 + 0
        assertThat((Double) clientStats[2]).isCloseTo(116.67, org.assertj.core.data.Offset.offset(0.01)); // 350 / 3 (approximately)
        assertThat(clientStats[3]).isEqualTo(3L);
    }

    // ==================== PAGINATION TESTS ====================

    @Test
    void testFindByBalanceWithPagination() {
        Page<LoyaltyAccount> page = loyaltyAccountRepository.findByBalanceGreaterThan(100, PageRequest.of(0, 2));
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testFindAccountsWithZeroBalanceWithPagination() {
        Page<LoyaltyAccount> page = loyaltyAccountRepository.findAccountsWithZeroBalance(PageRequest.of(0, 10));
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getTotalPages()).isEqualTo(1);
    }

    // ==================== EDGE CASES ====================

    @Test
    void testFindByNonExistentUser() {
        User nonExistentUser = new User("Non", "Existent", "nonexistent3@hotel.com", "password123", "+1234567892", "Address", LocalDate.of(2000, 1, 1), Role.CLIENT);
        entityManager.persistAndFlush(nonExistentUser);
        
        Optional<LoyaltyAccount> found = loyaltyAccountRepository.findByUser(nonExistentUser);
        assertThat(found).isEmpty();
    }

    @Test
    void testCountByNonExistentBalance() {
        long count = loyaltyAccountRepository.countByBalance(9999);
        assertThat(count).isEqualTo(0);
    }

    @Test
    void testExistsByNonExistentBalance() {
        boolean exists = loyaltyAccountRepository.existsByBalance(9999);
        assertThat(exists).isFalse();
    }
}
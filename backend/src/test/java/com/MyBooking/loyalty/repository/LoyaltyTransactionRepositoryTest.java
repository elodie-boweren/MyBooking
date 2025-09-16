package com.MyBooking.loyalty.repository;

import com.MyBooking.auth.domain.Role;
import com.MyBooking.auth.domain.User;
import com.MyBooking.loyalty.domain.LoyaltyAccount;
import com.MyBooking.loyalty.domain.LoyaltyTransaction;
import com.MyBooking.loyalty.domain.LoyaltyTxType;
import com.MyBooking.reservation.domain.Reservation;
import com.MyBooking.room.domain.Room;
import com.MyBooking.room.domain.RoomType;
import com.MyBooking.room.domain.RoomStatus;
import com.MyBooking.reservation.domain.ReservationStatus;
import com.MyBooking.hotel_management.HotelManagementApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Rollback;

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
    "com.MyBooking.loyalty.domain",
    "com.MyBooking.auth.domain",
    "com.MyBooking.reservation.domain",
    "com.MyBooking.room.domain"
})
@EnableJpaRepositories(basePackages = "com.MyBooking.loyalty.repository")
public class LoyaltyTransactionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LoyaltyTransactionRepository loyaltyTransactionRepository;

    @Autowired
    private LoyaltyAccountRepository loyaltyAccountRepository;

    // Test data
    private User client1, client2, employee1, admin1;
    private LoyaltyAccount account1, account2, account3, account4;
    private Room room1, room2;
    private Reservation reservation1, reservation2, reservation3;
    private LoyaltyTransaction transaction1, transaction2, transaction3, transaction4, transaction5, transaction6;
    private LocalDateTime baseDateTime;

    @BeforeEach
    void setUp() {
        baseDateTime = LocalDateTime.now().minusDays(1);

        // Create users
        client1 = new User("John", "Doe", "john.doe@email.com", "password123", "+1234567890", "123 Main St", LocalDate.of(1990, 1, 1), Role.CLIENT);
        client2 = new User("Jane", "Smith", "jane.smith@email.com", "password123", "+1234567891", "456 Oak Ave", LocalDate.of(1985, 5, 15), Role.CLIENT);
        employee1 = new User("Bob", "Johnson", "bob.johnson@email.com", "password123", "+1234567892", "789 Pine St", LocalDate.of(1988, 3, 20), Role.EMPLOYEE);
        admin1 = new User("Alice", "Admin", "alice.admin@email.com", "password123", "+1234567893", "321 Elm St", LocalDate.of(1982, 7, 10), Role.ADMIN);

        entityManager.persistAndFlush(client1);
        entityManager.persistAndFlush(client2);
        entityManager.persistAndFlush(employee1);
        entityManager.persistAndFlush(admin1);

        // Create loyalty accounts
        account1 = new LoyaltyAccount(client1, 100);
        account2 = new LoyaltyAccount(client2, 250);
        account3 = new LoyaltyAccount(employee1, 500);
        account4 = new LoyaltyAccount(admin1, 1000);

        entityManager.persistAndFlush(account1);
        entityManager.persistAndFlush(account2);
        entityManager.persistAndFlush(account3);
        entityManager.persistAndFlush(account4);

        // Create rooms
        room1 = new Room("101", RoomType.SINGLE, 1, BigDecimal.valueOf(100.00), "USD", RoomStatus.AVAILABLE);
        room2 = new Room("201", RoomType.DOUBLE, 2, BigDecimal.valueOf(150.00), "USD", RoomStatus.AVAILABLE);

        entityManager.persistAndFlush(room1);
        entityManager.persistAndFlush(room2);

        // Create reservations
        reservation1 = new Reservation(LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), 1, BigDecimal.valueOf(200.00), "USD", ReservationStatus.CONFIRMED, client1, room1);
        reservation2 = new Reservation(LocalDate.now().plusDays(5), LocalDate.now().plusDays(7), 2, BigDecimal.valueOf(300.00), "USD", ReservationStatus.CONFIRMED, client2, room2);
        reservation3 = new Reservation(LocalDate.now().plusDays(10), LocalDate.now().plusDays(12), 1, BigDecimal.valueOf(200.00), "USD", ReservationStatus.CONFIRMED, employee1, room1);

        entityManager.persistAndFlush(reservation1);
        entityManager.persistAndFlush(reservation2);
        entityManager.persistAndFlush(reservation3);

        // Create loyalty transactions
        transaction1 = new LoyaltyTransaction(account1, LoyaltyTxType.EARN, 50, reservation1);
        transaction2 = new LoyaltyTransaction(account2, LoyaltyTxType.EARN, 75, reservation2);
        transaction3 = new LoyaltyTransaction(account3, LoyaltyTxType.EARN, 100);
        transaction4 = new LoyaltyTransaction(account1, LoyaltyTxType.REDEEM, 25);
        transaction5 = new LoyaltyTransaction(account4, LoyaltyTxType.EARN, 200);
        transaction6 = new LoyaltyTransaction(account2, LoyaltyTxType.REDEEM, 50);

        entityManager.persistAndFlush(transaction1);
        entityManager.persistAndFlush(transaction2);
        entityManager.persistAndFlush(transaction3);
        entityManager.persistAndFlush(transaction4);
        entityManager.persistAndFlush(transaction5);
        entityManager.persistAndFlush(transaction6);
    }

    // ==================== BASIC QUERIES ====================

    @Test
    void testFindByAccount() {
        List<LoyaltyTransaction> transactions = loyaltyTransactionRepository.findByAccount(account1);
        assertThat(transactions).hasSize(2);
        assertThat(transactions).extracting("account").containsOnly(account1);
        assertThat(transactions).extracting("type").containsExactlyInAnyOrder(LoyaltyTxType.EARN, LoyaltyTxType.REDEEM);
    }

    @Test
    void testFindByAccountId() {
        List<LoyaltyTransaction> transactions = loyaltyTransactionRepository.findByAccountId(account2.getId());
        assertThat(transactions).hasSize(2);
        assertThat(transactions).extracting("account").containsOnly(account2);
    }

    @Test
    void testFindByType() {
        List<LoyaltyTransaction> earnTransactions = loyaltyTransactionRepository.findByType(LoyaltyTxType.EARN);
        assertThat(earnTransactions).hasSize(4);
        assertThat(earnTransactions).extracting("type").containsOnly(LoyaltyTxType.EARN);

        List<LoyaltyTransaction> redeemTransactions = loyaltyTransactionRepository.findByType(LoyaltyTxType.REDEEM);
        assertThat(redeemTransactions).hasSize(2);
        assertThat(redeemTransactions).extracting("type").containsOnly(LoyaltyTxType.REDEEM);
    }

    @Test
    void testFindByReservation() {
        List<LoyaltyTransaction> transactions = loyaltyTransactionRepository.findByReservation(reservation1);
        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getAccount()).isEqualTo(account1);
        assertThat(transactions.get(0).getType()).isEqualTo(LoyaltyTxType.EARN);
    }

    @Test
    void testFindByReservationId() {
        List<LoyaltyTransaction> transactions = loyaltyTransactionRepository.findByReservationId(reservation2.getId());
        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getAccount()).isEqualTo(account2);
    }

    // ==================== POINTS QUERIES ====================

    @Test
    void testFindByPointsGreaterThan() {
        List<LoyaltyTransaction> transactions = loyaltyTransactionRepository.findByPointsGreaterThan(75);
        assertThat(transactions).hasSize(2);
        assertThat(transactions).extracting("points").containsExactlyInAnyOrder(100, 200);
    }

    @Test
    void testFindByPointsLessThan() {
        List<LoyaltyTransaction> transactions = loyaltyTransactionRepository.findByPointsLessThan(50);
        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getPoints()).isEqualTo(25);
    }

    @Test
    void testFindByPointsBetween() {
        List<LoyaltyTransaction> transactions = loyaltyTransactionRepository.findByPointsBetween(50, 100);
        assertThat(transactions).hasSize(4); // 50, 75, 100, 50 (transaction1, transaction2, transaction3, transaction6)
        assertThat(transactions).extracting("points").containsExactlyInAnyOrder(50, 75, 100, 50);
    }

    // ==================== DATE-BASED QUERIES ====================

    @Test
    void testFindByCreatedAtBetween() {
        LocalDateTime startDate = LocalDateTime.now().minusMinutes(5);
        LocalDateTime endDate = LocalDateTime.now().plusMinutes(5);
        
        List<LoyaltyTransaction> transactions = loyaltyTransactionRepository.findByCreatedAtBetween(startDate, endDate);
        assertThat(transactions).hasSize(6); // All transactions created in setUp
    }

    @Test
    void testFindByCreatedAtAfter() {
        LocalDateTime date = LocalDateTime.now().minusMinutes(10);
        
        List<LoyaltyTransaction> transactions = loyaltyTransactionRepository.findByCreatedAtAfter(date);
        assertThat(transactions).hasSize(6); // All transactions created after this date
    }

    @Test
    void testFindByCreatedAtBefore() {
        LocalDateTime date = LocalDateTime.now().plusMinutes(10);
        
        List<LoyaltyTransaction> transactions = loyaltyTransactionRepository.findByCreatedAtBefore(date);
        assertThat(transactions).hasSize(6); // All transactions created before this date
    }

    // ==================== COMBINED QUERIES ====================

    @Test
    void testFindByAccountAndType() {
        List<LoyaltyTransaction> transactions = loyaltyTransactionRepository.findByAccountAndType(account1, LoyaltyTxType.EARN);
        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getAccount()).isEqualTo(account1);
        assertThat(transactions.get(0).getType()).isEqualTo(LoyaltyTxType.EARN);
    }

    @Test
    void testFindByAccountIdAndType() {
        List<LoyaltyTransaction> transactions = loyaltyTransactionRepository.findByAccountIdAndType(account2.getId(), LoyaltyTxType.REDEEM);
        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getAccount()).isEqualTo(account2);
        assertThat(transactions.get(0).getType()).isEqualTo(LoyaltyTxType.REDEEM);
    }

    @Test
    void testFindByAccountAndReservation() {
        List<LoyaltyTransaction> transactions = loyaltyTransactionRepository.findByAccountAndReservation(account1, reservation1);
        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getAccount()).isEqualTo(account1);
        assertThat(transactions.get(0).getReservation()).isEqualTo(reservation1);
    }

    @Test
    void testFindByAccountIdAndReservationId() {
        List<LoyaltyTransaction> transactions = loyaltyTransactionRepository.findByAccountIdAndReservationId(account2.getId(), reservation2.getId());
        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getAccount()).isEqualTo(account2);
        assertThat(transactions.get(0).getReservation()).isEqualTo(reservation2);
    }

    @Test
    void testFindHighValueTransactions() {
        List<LoyaltyTransaction> transactions = loyaltyTransactionRepository.findHighValueTransactions(75);
        assertThat(transactions).hasSize(3); // 75, 100, 200 (transaction2, transaction3, transaction5)
        assertThat(transactions).extracting("points").containsExactlyInAnyOrder(75, 100, 200);
    }

    // ==================== CUSTOM BUSINESS QUERIES ====================

    @Test
    void testFindByAccountOrderByCreatedAtDesc() {
        List<LoyaltyTransaction> transactions = loyaltyTransactionRepository.findByAccountOrderByCreatedAtDesc(account1);
        assertThat(transactions).hasSize(2);
        assertThat(transactions).extracting("account").containsOnly(account1);
        // Should be ordered by creation date descending (most recent first)
        assertThat(transactions.get(0).getCreatedAt()).isAfterOrEqualTo(transactions.get(1).getCreatedAt());
    }

    @Test
    void testFindByTypeOrderByCreatedAtDesc() {
        List<LoyaltyTransaction> transactions = loyaltyTransactionRepository.findByTypeOrderByCreatedAtDesc(LoyaltyTxType.EARN);
        assertThat(transactions).hasSize(4);
        assertThat(transactions).extracting("type").containsOnly(LoyaltyTxType.EARN);
    }

    @Test
    void testFindTransactionsWithoutReservation() {
        List<LoyaltyTransaction> transactions = loyaltyTransactionRepository.findTransactionsWithoutReservation();
        assertThat(transactions).hasSize(4); // transaction3, transaction4, transaction5, transaction6
        assertThat(transactions).extracting("reservation").containsOnly((Object) null);
    }

    // ==================== COUNT QUERIES ====================

    @Test
    void testCountByAccount() {
        long count = loyaltyTransactionRepository.countByAccount(account1);
        assertThat(count).isEqualTo(2);
    }

    @Test
    void testCountByAccountId() {
        long count = loyaltyTransactionRepository.countByAccountId(account2.getId());
        assertThat(count).isEqualTo(2);
    }

    @Test
    void testCountByType() {
        long earnCount = loyaltyTransactionRepository.countByType(LoyaltyTxType.EARN);
        assertThat(earnCount).isEqualTo(4);

        long redeemCount = loyaltyTransactionRepository.countByType(LoyaltyTxType.REDEEM);
        assertThat(redeemCount).isEqualTo(2);
    }

    @Test
    void testCountByReservation() {
        long count = loyaltyTransactionRepository.countByReservation(reservation1);
        assertThat(count).isEqualTo(1);
    }

    @Test
    void testCountByReservationId() {
        long count = loyaltyTransactionRepository.countByReservationId(reservation2.getId());
        assertThat(count).isEqualTo(1);
    }

    @Test
    void testCountByCreatedAtBetween() {
        LocalDateTime startDate = LocalDateTime.now().minusMinutes(5);
        LocalDateTime endDate = LocalDateTime.now().plusMinutes(5);
        
        long count = loyaltyTransactionRepository.countByCreatedAtBetween(startDate, endDate);
        assertThat(count).isEqualTo(6);
    }

    // ==================== EXISTENCE QUERIES ====================

    @Test
    void testExistsByAccount() {
        boolean exists = loyaltyTransactionRepository.existsByAccount(account1);
        assertThat(exists).isTrue();

        // Create a new account without transactions
        User newUser = new User("New", "User", "new@email.com", "password123", "+1234567894", "999 New St", LocalDate.of(1995, 1, 1), Role.CLIENT);
        entityManager.persistAndFlush(newUser);
        LoyaltyAccount newAccount = new LoyaltyAccount(newUser, 0);
        entityManager.persistAndFlush(newAccount);

        boolean notExists = loyaltyTransactionRepository.existsByAccount(newAccount);
        assertThat(notExists).isFalse();
    }

    @Test
    void testExistsByAccountId() {
        boolean exists = loyaltyTransactionRepository.existsByAccountId(account1.getId());
        assertThat(exists).isTrue();

        boolean notExists = loyaltyTransactionRepository.existsByAccountId(999L);
        assertThat(notExists).isFalse();
    }

    @Test
    void testExistsByReservation() {
        boolean exists = loyaltyTransactionRepository.existsByReservation(reservation1);
        assertThat(exists).isTrue();

        // Create a new reservation without transactions
        User newUser = new User("New", "User", "new2@email.com", "password123", "+1234567895", "888 New St", LocalDate.of(1995, 1, 1), Role.CLIENT);
        entityManager.persistAndFlush(newUser);
        Reservation newReservation = new Reservation(LocalDate.now().plusDays(20), LocalDate.now().plusDays(22), 1, BigDecimal.valueOf(200.00), "USD", ReservationStatus.CONFIRMED, newUser, room1);
        entityManager.persistAndFlush(newReservation);

        boolean notExists = loyaltyTransactionRepository.existsByReservation(newReservation);
        assertThat(notExists).isFalse();
    }

    @Test
    void testExistsByReservationId() {
        boolean exists = loyaltyTransactionRepository.existsByReservationId(reservation1.getId());
        assertThat(exists).isTrue();

        boolean notExists = loyaltyTransactionRepository.existsByReservationId(999L);
        assertThat(notExists).isFalse();
    }

    // ==================== AGGREGATION QUERIES ====================

    @Test
    void testGetTotalPointsByAccountAndType() {
        Long earnSum = loyaltyTransactionRepository.getTotalPointsByAccountAndType(account1, LoyaltyTxType.EARN);
        assertThat(earnSum).isEqualTo(50L);

        Long redeemSum = loyaltyTransactionRepository.getTotalPointsByAccountAndType(account1, LoyaltyTxType.REDEEM);
        assertThat(redeemSum).isEqualTo(25L);
    }

    @Test
    void testGetTotalPointsByType() {
        Long earnSum = loyaltyTransactionRepository.getTotalPointsByType(LoyaltyTxType.EARN);
        assertThat(earnSum).isEqualTo(425L); // 50 + 75 + 100 + 200

        Long redeemSum = loyaltyTransactionRepository.getTotalPointsByType(LoyaltyTxType.REDEEM);
        assertThat(redeemSum).isEqualTo(75L); // 25 + 50
    }

    @Test
    void testFindLatestTransactionByAccount() {
        Pageable pageable = PageRequest.of(0, 1);
        List<LoyaltyTransaction> transactions = loyaltyTransactionRepository.findLatestTransactionByAccount(account1, pageable);
        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getAccount()).isEqualTo(account1);
    }

    // ==================== PAGINATION TESTS ====================

    @Test
    void testFindByAccountWithPagination() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<LoyaltyTransaction> page = loyaltyTransactionRepository.findByAccount(account1, pageable);
        
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testFindByTypeWithPagination() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<LoyaltyTransaction> page = loyaltyTransactionRepository.findByType(LoyaltyTxType.EARN, pageable);
        
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(4);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testFindByPointsGreaterThanWithPagination() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<LoyaltyTransaction> page = loyaltyTransactionRepository.findByPointsGreaterThan(75, pageable);
        
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
    }

    // ==================== EDGE CASES ====================

    @Test
    void testFindByNonExistentAccount() {
        User nonExistentUser = new User("Non", "Existent", "nonexistent@email.com", "password123", "+1234567896", "777 Non St", LocalDate.of(1990, 1, 1), Role.CLIENT);
        entityManager.persistAndFlush(nonExistentUser);
        LoyaltyAccount nonExistentAccount = new LoyaltyAccount(nonExistentUser, 0);
        entityManager.persistAndFlush(nonExistentAccount);

        List<LoyaltyTransaction> transactions = loyaltyTransactionRepository.findByAccount(nonExistentAccount);
        assertThat(transactions).isEmpty();
    }

    @Test
    void testFindByNonExistentReservation() {
        User nonExistentUser = new User("Non", "Existent2", "nonexistent2@email.com", "password123", "+1234567897", "666 Non St", LocalDate.of(1990, 1, 1), Role.CLIENT);
        entityManager.persistAndFlush(nonExistentUser);
        Reservation nonExistentReservation = new Reservation(LocalDate.now().plusDays(30), LocalDate.now().plusDays(32), 1, BigDecimal.valueOf(200.00), "USD", ReservationStatus.CONFIRMED, nonExistentUser, room1);
        entityManager.persistAndFlush(nonExistentReservation);

        List<LoyaltyTransaction> transactions = loyaltyTransactionRepository.findByReservation(nonExistentReservation);
        assertThat(transactions).isEmpty();
    }

    @Test
    void testCountByNonExistentAccount() {
        long count = loyaltyTransactionRepository.countByAccountId(999L);
        assertThat(count).isEqualTo(0);
    }

    @Test
    void testExistsByNonExistentAccount() {
        boolean exists = loyaltyTransactionRepository.existsByAccountId(999L);
        assertThat(exists).isFalse();
    }

    @Test
    void testSumPointsByNonExistentAccount() {
        User nonExistentUser = new User("Non", "Existent3", "nonexistent3@email.com", "password123", "+1234567898", "555 Non St", LocalDate.of(1990, 1, 1), Role.CLIENT);
        entityManager.persistAndFlush(nonExistentUser);
        LoyaltyAccount nonExistentAccount = new LoyaltyAccount(nonExistentUser, 0);
        entityManager.persistAndFlush(nonExistentAccount);

        Long sum = loyaltyTransactionRepository.getTotalPointsByAccountAndType(nonExistentAccount, LoyaltyTxType.EARN);
        assertThat(sum).isEqualTo(0L);
    }

}

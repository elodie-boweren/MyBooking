package com.MyBooking.auth.repository;

import com.MyBooking.auth.domain.Role;
import com.MyBooking.auth.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * Integration tests for UserRepository.
 * Tests all repository methods with real database operations.
 */
@DataJpaTest
@ActiveProfiles("repository-test")
@EntityScan(basePackages = {
    "com.MyBooking.auth.domain"
})
@EnableJpaRepositories(basePackages = {
    "com.MyBooking.auth.repository"
})
@ContextConfiguration(classes = com.MyBooking.hotel_management.HotelManagementApplication.class)
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User clientUser;
    private User employeeUser;
    private User adminUser;
    private User anotherClientUser;

    @BeforeEach
    void setUp() {
        // Create test users
        clientUser = new User(
                "John", "Doe", "john.doe@example.com", "password123",
                "+1234567890", "123 Main St", LocalDate.of(1990, 5, 15), Role.CLIENT
        );

        employeeUser = new User(
                "Jane", "Smith", "jane.smith@hotel.com", "password456",
                "+1234567891", "456 Oak Ave", LocalDate.of(1985, 8, 20), Role.EMPLOYEE
        );

        adminUser = new User(
                "Admin", "User", "admin@hotel.com", "password789",
                "+1234567892", "789 Pine St", LocalDate.of(1980, 3, 10), Role.ADMIN
        );

        anotherClientUser = new User(
                "Bob", "Johnson", "bob.johnson@example.com", "password101",
                "+1234567893", "321 Elm St", LocalDate.of(1992, 12, 5), Role.CLIENT
        );

        // Persist users
        entityManager.persistAndFlush(clientUser);
        entityManager.persistAndFlush(employeeUser);
        entityManager.persistAndFlush(adminUser);
        entityManager.persistAndFlush(anotherClientUser);
        entityManager.clear();
    }

    @Test
    @DisplayName("Should find user by email")
    void shouldFindUserByEmail() {
        // When
        Optional<User> foundUser = userRepository.findByEmail("john.doe@example.com");

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getFirstName()).isEqualTo("John");
        assertThat(foundUser.get().getLastName()).isEqualTo("Doe");
        assertThat(foundUser.get().getRole()).isEqualTo(Role.CLIENT);
    }

    @Test
    @DisplayName("Should return empty when user not found by email")
    void shouldReturnEmptyWhenUserNotFoundByEmail() {
        // When
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        // Then
        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("Should check if user exists by email")
    void shouldCheckIfUserExistsByEmail() {
        // When & Then
        assertThat(userRepository.existsByEmail("john.doe@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("nonexistent@example.com")).isFalse();
    }

    @Test
    @DisplayName("Should find users by role")
    void shouldFindUsersByRole() {
        // When
        List<User> clients = userRepository.findByRole(Role.CLIENT);
        List<User> employees = userRepository.findByRole(Role.EMPLOYEE);
        List<User> admins = userRepository.findByRole(Role.ADMIN);

        // Then
        assertThat(clients).hasSize(2);
        assertThat(clients).extracting(User::getEmail)
                .containsExactlyInAnyOrder("john.doe@example.com", "bob.johnson@example.com");

        assertThat(employees).hasSize(1);
        assertThat(employees.get(0).getEmail()).isEqualTo("jane.smith@hotel.com");

        assertThat(admins).hasSize(1);
        assertThat(admins.get(0).getEmail()).isEqualTo("admin@hotel.com");
    }

    @Test
    @DisplayName("Should find users by role with pagination")
    void shouldFindUsersByRoleWithPagination() {
        // Given
        Pageable pageable = PageRequest.of(0, 1);

        // When
        Page<User> clientPage = userRepository.findByRole(Role.CLIENT, pageable);

        // Then
        assertThat(clientPage.getContent()).hasSize(1);
        assertThat(clientPage.getTotalElements()).isEqualTo(2);
        assertThat(clientPage.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should find users by birth date range")
    void shouldFindUsersByBirthDateRange() {
        // Given
        LocalDate startDate = LocalDate.of(1990, 1, 1);
        LocalDate endDate = LocalDate.of(1995, 12, 31);

        // When
        List<User> users = userRepository.findByBirthDateBetween(startDate, endDate);

        // Then
        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getEmail)
                .containsExactlyInAnyOrder("john.doe@example.com", "bob.johnson@example.com");
    }

    @Test
    @DisplayName("Should count users by role")
    void shouldCountUsersByRole() {
        // When & Then
        assertThat(userRepository.countByRole(Role.CLIENT)).isEqualTo(2);
        assertThat(userRepository.countByRole(Role.EMPLOYEE)).isEqualTo(1);
        assertThat(userRepository.countByRole(Role.ADMIN)).isEqualTo(1);
    }

    @Test
    @DisplayName("Should find users with specific roles")
    void shouldFindUsersWithSpecificRoles() {
        // Given
        List<Role> roles = List.of(Role.CLIENT, Role.ADMIN);

        // When
        List<User> users = userRepository.findByRoleIn(roles);

        // Then
        assertThat(users).hasSize(3);
        assertThat(users).extracting(User::getRole)
                .containsExactlyInAnyOrder(Role.CLIENT, Role.CLIENT, Role.ADMIN);
    }

    @Test
    @DisplayName("Should find users by email domain")
    void shouldFindUsersByEmailDomain() {
        // When
        List<User> users = userRepository.findByEmailDomain("@hotel.com");

        // Then
        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getEmail)
                .containsExactlyInAnyOrder("jane.smith@hotel.com", "admin@hotel.com");
    }
}

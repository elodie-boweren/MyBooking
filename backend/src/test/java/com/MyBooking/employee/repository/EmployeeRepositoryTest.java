package com.MyBooking.employee.repository;

import com.MyBooking.employee.domain.Employee;
import java.time.LocalDate;
import com.MyBooking.employee.domain.EmployeeStatus;
import com.MyBooking.auth.domain.User;
import com.MyBooking.auth.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Rollback;
import com.MyBooking.hotel_management.HotelManagementApplication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("repository-test")
@ContextConfiguration(classes = HotelManagementApplication.class)
@EntityScan({"com.MyBooking.employee.domain", "com.MyBooking.auth.domain"})
@EnableJpaRepositories("com.MyBooking.employee.repository")
class EmployeeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    // Test data
    private User user1, user2, user3, user4, user5;
    private Employee employee1, employee2, employee3, employee4, employee5;

    @BeforeEach
    @Transactional
    @Rollback
    void setUp() {
        // Create test users
        user1 = new User("John", "Doe", "john.doe@hotel.com", "password123", 
                        "1234567890", "123 Main St", LocalDate.of(1990, 1, 1), Role.EMPLOYEE);
        user2 = new User("Jane", "Smith", "jane.smith@hotel.com", "password123", 
                        "1987654321", "456 Oak Ave", LocalDate.of(1985, 5, 15), Role.EMPLOYEE);
        user3 = new User("Bob", "Johnson", "bob.johnson@hotel.com", "password123", 
                        "1122334455", "789 Pine St", LocalDate.of(1992, 8, 20), Role.EMPLOYEE);
        user4 = new User("Alice", "Brown", "alice.brown@hotel.com", "password123", 
                        "1555666777", "321 Elm St", LocalDate.of(1988, 3, 10), Role.EMPLOYEE);
        user5 = new User("Charlie", "Wilson", "charlie.wilson@hotel.com", "password123", 
                        "1999888777", "654 Maple Ave", LocalDate.of(1995, 12, 5), Role.EMPLOYEE);

        // Save users
        user1 = entityManager.persistAndFlush(user1);
        user2 = entityManager.persistAndFlush(user2);
        user3 = entityManager.persistAndFlush(user3);
        user4 = entityManager.persistAndFlush(user4);
        user5 = entityManager.persistAndFlush(user5);

        // Create test employees
        employee1 = new Employee(user1, EmployeeStatus.ACTIVE, "Manager");
        employee2 = new Employee(user2, EmployeeStatus.ACTIVE, "Receptionist");
        employee3 = new Employee(user3, EmployeeStatus.ACTIVE, "Housekeeper");
        employee4 = new Employee(user4, EmployeeStatus.INACTIVE, "Manager");
        employee5 = new Employee(user5, EmployeeStatus.ACTIVE, "Maintenance");

        // Save employees
        employee1 = entityManager.persistAndFlush(employee1);
        employee2 = entityManager.persistAndFlush(employee2);
        employee3 = entityManager.persistAndFlush(employee3);
        employee4 = entityManager.persistAndFlush(employee4);
        employee5 = entityManager.persistAndFlush(employee5);

    }

    // ==================== BASIC QUERIES TESTS ====================

    @Test
    void testFindByUserId() {
        Optional<Employee> found = employeeRepository.findByUserId(user1.getId());
        
        assertThat(found).isPresent();
        assertThat(found.get().getUser()).isEqualTo(user1);
        assertThat(found.get().getJobTitle()).isEqualTo("Manager");
    }

    @Test
    void testFindByUserIdNotFound() {
        Optional<Employee> found = employeeRepository.findByUserId(999L);
        
        assertThat(found).isEmpty();
    }

    @Test
    void testFindByUser() {
        Optional<Employee> found = employeeRepository.findByUser(user2);
        
        assertThat(found).isPresent();
        assertThat(found.get().getJobTitle()).isEqualTo("Receptionist");
    }

    @Test
    void testExistsByUserId() {
        assertThat(employeeRepository.existsByUserId(user1.getId())).isTrue();
        assertThat(employeeRepository.existsByUserId(999L)).isFalse();
    }

    @Test
    void testExistsByUser() {
        assertThat(employeeRepository.existsByUser(user1)).isTrue();

        assertThat(employeeRepository.existsByUserId(999L)).isFalse();
    }

    // ==================== STATUS-BASED QUERIES TESTS ====================

    @Test
    void testFindByStatus() {
        List<Employee> activeEmployees = employeeRepository.findByStatus(EmployeeStatus.ACTIVE);
        List<Employee> inactiveEmployees = employeeRepository.findByStatus(EmployeeStatus.INACTIVE);
        
        assertThat(activeEmployees).hasSize(4);
        assertThat(inactiveEmployees).hasSize(1);
        
        assertThat(activeEmployees).containsExactlyInAnyOrder(employee1, employee2, employee3, employee5);
        assertThat(inactiveEmployees).containsExactly(employee4);
    }

    @Test
    void testFindByStatusWithPagination() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Employee> activePage = employeeRepository.findByStatus(EmployeeStatus.ACTIVE, pageable);
        
        assertThat(activePage.getContent()).hasSize(2);
        assertThat(activePage.getTotalElements()).isEqualTo(4);
        assertThat(activePage.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testCountByStatus() {
        assertThat(employeeRepository.countByStatus(EmployeeStatus.ACTIVE)).isEqualTo(4);
        assertThat(employeeRepository.countByStatus(EmployeeStatus.INACTIVE)).isEqualTo(1);
    }

    @Test
    void testFindActiveEmployees() {
        List<Employee> activeEmployees = employeeRepository.findActiveEmployees();
        
        assertThat(activeEmployees).hasSize(4);
        assertThat(activeEmployees).allMatch(emp -> emp.getStatus() == EmployeeStatus.ACTIVE);
    }

    @Test
    void testFindInactiveEmployees() {
        List<Employee> inactiveEmployees = employeeRepository.findInactiveEmployees();
        
        assertThat(inactiveEmployees).hasSize(1);
        assertThat(inactiveEmployees).allMatch(emp -> emp.getStatus() == EmployeeStatus.INACTIVE);
    }

    // ==================== JOB TITLE QUERIES TESTS ====================

    @Test
    void testFindByJobTitle() {
        List<Employee> managers = employeeRepository.findByJobTitle("Manager");
        
        assertThat(managers).hasSize(2);
        assertThat(managers).containsExactlyInAnyOrder(employee1, employee4);
    }

    @Test
    void testFindByJobTitleWithPagination() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Employee> managerPage = employeeRepository.findByJobTitle("Manager", pageable);
        
        assertThat(managerPage.getContent()).hasSize(1);
        assertThat(managerPage.getTotalElements()).isEqualTo(2);
    }

    @Test
    void testFindByJobTitleContainingIgnoreCase() {
        List<Employee> containingMan = employeeRepository.findByJobTitleContainingIgnoreCase("man");
        
        assertThat(containingMan).hasSize(2);
        assertThat(containingMan).allMatch(emp -> emp.getJobTitle().toLowerCase().contains("man"));
    }

    @Test
    void testFindByJobTitleStartingWithIgnoreCase() {
        List<Employee> startingWithMan = employeeRepository.findByJobTitleStartingWithIgnoreCase("man");
        
        assertThat(startingWithMan).hasSize(2);
        assertThat(startingWithMan).allMatch(emp -> emp.getJobTitle().toLowerCase().startsWith("man"));
    }

    @Test
    void testFindDistinctJobTitles() {
        List<String> jobTitles = employeeRepository.findDistinctJobTitles();
        
        assertThat(jobTitles).hasSize(4);
        assertThat(jobTitles).containsExactlyInAnyOrder("Manager", "Receptionist", "Housekeeper", "Maintenance");
    }

    @Test
    void testCountByJobTitle() {
        assertThat(employeeRepository.countByJobTitle("Manager")).isEqualTo(2);
        assertThat(employeeRepository.countByJobTitle("Receptionist")).isEqualTo(1);
        assertThat(employeeRepository.countByJobTitle("NonExistent")).isEqualTo(0);
    }

    // ==================== COMBINED CRITERIA QUERIES TESTS ====================

    @Test
    void testFindByStatusAndJobTitle() {
        List<Employee> activeManagers = employeeRepository.findByStatusAndJobTitle(EmployeeStatus.ACTIVE, "Manager");
        List<Employee> inactiveManagers = employeeRepository.findByStatusAndJobTitle(EmployeeStatus.INACTIVE, "Manager");
        
        assertThat(activeManagers).hasSize(1);
        assertThat(activeManagers).containsExactly(employee1);
        
        assertThat(inactiveManagers).hasSize(1);
        assertThat(inactiveManagers).containsExactly(employee4);
    }

    @Test
    void testFindByStatusAndJobTitleWithPagination() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Employee> activeManagerPage = employeeRepository.findByStatusAndJobTitle(EmployeeStatus.ACTIVE, "Manager", pageable);
        
        assertThat(activeManagerPage.getContent()).hasSize(1);
        assertThat(activeManagerPage.getTotalElements()).isEqualTo(1);
    }

    @Test
    void testFindByCriteria() {
        // Test with both criteria
        List<Employee> activeManagers = employeeRepository.findByCriteria(EmployeeStatus.ACTIVE, "Manager");
        assertThat(activeManagers).hasSize(1);
        assertThat(activeManagers).containsExactly(employee1);
        
        // Test with status only
        List<Employee> activeEmployees = employeeRepository.findByCriteria(EmployeeStatus.ACTIVE, null);
        assertThat(activeEmployees).hasSize(4);
        
        // Test with job title only
        List<Employee> managers = employeeRepository.findByCriteria(null, "Manager");
        assertThat(managers).hasSize(2);
        
        // Test with no criteria
        List<Employee> allEmployees = employeeRepository.findByCriteria(null, null);
        assertThat(allEmployees).hasSize(5);
    }

    @Test
    void testFindByCriteriaWithPagination() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Employee> page = employeeRepository.findByCriteria(EmployeeStatus.ACTIVE, null, pageable);
        
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(4);
    }

    // ==================== TIME-BASED QUERIES TESTS ====================

    @Test
    void testFindByCreatedAtAfter() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(1);
        List<Employee> recentEmployees = employeeRepository.findByCreatedAtAfter(cutoff);
        
        assertThat(recentEmployees).hasSize(5);
    }

    @Test
    void testFindByCreatedAtBefore() {
        LocalDateTime future = LocalDateTime.now().plusHours(1);
        List<Employee> pastEmployees = employeeRepository.findByCreatedAtBefore(future);
        
        assertThat(pastEmployees).hasSize(5);
    }

    @Test
    void testFindByCreatedAtBetween() {
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        List<Employee> betweenEmployees = employeeRepository.findByCreatedAtBetween(start, end);
        
        assertThat(betweenEmployees).hasSize(5);
    }

    @Test
    void testFindByUpdatedAtAfter() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(1);
        List<Employee> recentUpdates = employeeRepository.findByUpdatedAtAfter(cutoff);
        
        assertThat(recentUpdates).hasSize(5);
    }

    // ==================== USER-RELATED QUERIES TESTS ====================

    @Test
    void testFindByUserFirstNameContainingIgnoreCase() {
        List<Employee> johnEmployees = employeeRepository.findByUserFirstNameContainingIgnoreCase("john");
        
        assertThat(johnEmployees).hasSize(1);
        assertThat(johnEmployees.get(0).getUser().getFirstName()).isEqualTo("John");
    }

    @Test
    void testFindByUserLastNameContainingIgnoreCase() {
        List<Employee> smithEmployees = employeeRepository.findByUserLastNameContainingIgnoreCase("smith");
        
        assertThat(smithEmployees).hasSize(1);
        assertThat(smithEmployees.get(0).getUser().getLastName()).isEqualTo("Smith");
    }

    @Test
    void testFindByUserEmailContainingIgnoreCase() {
        List<Employee> hotelEmployees = employeeRepository.findByUserEmailContainingIgnoreCase("hotel.com");
        
        assertThat(hotelEmployees).hasSize(5);
    }

    @Test
    void testFindByUserRole() {
        List<Employee> employeeRole = employeeRepository.findByUserRole(Role.EMPLOYEE);
        
        assertThat(employeeRole).hasSize(5);
        assertThat(employeeRole).allMatch(emp -> emp.getUser().getRole() == Role.EMPLOYEE);
    }

    // ==================== STATISTICS AND ANALYTICS TESTS ====================

    @Test
    void testGetTotalEmployeeCount() {
        long totalCount = employeeRepository.getTotalEmployeeCount();
        
        assertThat(totalCount).isEqualTo(5);
    }

    @Test
    void testGetEmployeeCountByStatus() {
        List<Object[]> statusCounts = employeeRepository.getEmployeeCountByStatus();
        
        assertThat(statusCounts).hasSize(2);
        
        // Check that we have both ACTIVE and INACTIVE counts
        boolean hasActive = statusCounts.stream().anyMatch(arr -> arr[0] == EmployeeStatus.ACTIVE && (Long) arr[1] == 4);
        boolean hasInactive = statusCounts.stream().anyMatch(arr -> arr[0] == EmployeeStatus.INACTIVE && (Long) arr[1] == 1);
        
        assertThat(hasActive).isTrue();
        assertThat(hasInactive).isTrue();
    }

    @Test
    void testGetEmployeeCountByJobTitle() {
        List<Object[]> jobTitleCounts = employeeRepository.getEmployeeCountByJobTitle();
        
        assertThat(jobTitleCounts).hasSize(4);
        
        // Check Manager count (should be 2)
        boolean hasManager = jobTitleCounts.stream().anyMatch(arr -> "Manager".equals(arr[0]) && (Long) arr[1] == 2);
        assertThat(hasManager).isTrue();
    }

    @Test
    void testGetEmployeesCreatedInLastDays() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(1);
        List<Employee> recentEmployees = employeeRepository.getEmployeesCreatedInLastDays(cutoff);
        
        assertThat(recentEmployees).hasSize(5);
    }

    @Test
    void testGetEmployeesUpdatedInLastDays() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(1);
        List<Employee> recentUpdates = employeeRepository.getEmployeesUpdatedInLastDays(cutoff);
        
        assertThat(recentUpdates).hasSize(5);
    }

    // ==================== VALIDATION QUERIES TESTS ====================

    @Test
    void testIsUserEmployee() {
        assertThat(employeeRepository.isUserEmployee(user1)).isTrue();
        assertThat(employeeRepository.isUserEmployee(user2)).isTrue();
        assertThat(employeeRepository.isUserIdEmployee(999L)).isFalse();
    }

    @Test
    void testIsUserIdEmployee() {
        assertThat(employeeRepository.isUserIdEmployee(user1.getId())).isTrue();
        assertThat(employeeRepository.isUserIdEmployee(user2.getId())).isTrue();
        assertThat(employeeRepository.isUserIdEmployee(999L)).isFalse();
    }
}
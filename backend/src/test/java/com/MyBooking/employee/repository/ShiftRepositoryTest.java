package com.MyBooking.employee.repository;

import com.MyBooking.employee.domain.Shift;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("repository-test")
@ContextConfiguration(classes = HotelManagementApplication.class)
@EntityScan({"com.MyBooking.employee.domain", "com.MyBooking.auth.domain"})
@EnableJpaRepositories("com.MyBooking.employee.repository")
class ShiftRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ShiftRepository shiftRepository;

    // Test data
    private User employee1, employee2, employee3, employee4, employee5;
    private Shift shift1, shift2, shift3, shift4, shift5, shift6, shift7, shift8;

    @BeforeEach
    @Transactional
    @Rollback
    void setUp() {
        // Create test users
        employee1 = new User("John", "Doe", "john.doe@hotel.com", "password123", 
                            "1234567890", "123 Main St", LocalDate.of(1990, 1, 1), Role.EMPLOYEE);
        employee2 = new User("Jane", "Smith", "jane.smith@hotel.com", "password123", 
                            "1987654321", "456 Oak Ave", LocalDate.of(1985, 5, 15), Role.EMPLOYEE);
        employee3 = new User("Bob", "Johnson", "bob.johnson@hotel.com", "password123", 
                            "1122334455", "789 Pine St", LocalDate.of(1992, 8, 20), Role.EMPLOYEE);
        employee4 = new User("Alice", "Brown", "alice.brown@hotel.com", "password123", 
                            "1555666777", "321 Elm St", LocalDate.of(1988, 3, 10), Role.EMPLOYEE);
        employee5 = new User("Charlie", "Wilson", "charlie.wilson@hotel.com", "password123", 
                            "1999888777", "654 Maple Ave", LocalDate.of(1995, 12, 5), Role.EMPLOYEE);

        // Save users
        employee1 = entityManager.persistAndFlush(employee1);
        employee2 = entityManager.persistAndFlush(employee2);
        employee3 = entityManager.persistAndFlush(employee3);
        employee4 = entityManager.persistAndFlush(employee4);
        employee5 = entityManager.persistAndFlush(employee5);

        // Create test shifts
        LocalDateTime baseDate = LocalDateTime.of(2024, 1, 15, 0, 0);
        
        // Employee1 shifts
        shift1 = new Shift(employee1, baseDate.plusHours(9), baseDate.plusHours(17)); // 9:00-17:00
        shift2 = new Shift(employee1, baseDate.plusDays(1).plusHours(9), baseDate.plusDays(1).plusHours(17)); // Next day
        
        // Employee2 shifts
        shift3 = new Shift(employee2, baseDate.plusHours(17), baseDate.plusHours(1).plusDays(1)); // 17:00-01:00
        shift4 = new Shift(employee2, baseDate.plusDays(1).plusHours(17), baseDate.plusDays(2).plusHours(1)); // Next day
        
        // Employee3 shifts
        shift5 = new Shift(employee3, baseDate.plusHours(1), baseDate.plusHours(9)); // 01:00-09:00
        shift6 = new Shift(employee3, baseDate.plusDays(1).plusHours(1), baseDate.plusDays(1).plusHours(9)); // Next day
        
        // Employee4 shifts (overlapping with shift1)
        shift7 = new Shift(employee4, baseDate.plusHours(14), baseDate.plusHours(22)); // 14:00-22:00
        
        // Employee5 shifts
        shift8 = new Shift(employee5, baseDate.plusHours(10), baseDate.plusHours(18)); // 10:00-18:00

        // Save shifts
        shift1 = entityManager.persistAndFlush(shift1);
        shift2 = entityManager.persistAndFlush(shift2);
        shift3 = entityManager.persistAndFlush(shift3);
        shift4 = entityManager.persistAndFlush(shift4);
        shift5 = entityManager.persistAndFlush(shift5);
        shift6 = entityManager.persistAndFlush(shift6);
        shift7 = entityManager.persistAndFlush(shift7);
        shift8 = entityManager.persistAndFlush(shift8);
    }

    // ==================== BASIC QUERIES TESTS ====================

    @Test
    void testFindByEmployee() {
        List<Shift> employee1Shifts = shiftRepository.findByEmployeeId(employee1.getId());
        
        assertThat(employee1Shifts).hasSize(2);
        assertThat(employee1Shifts).containsExactlyInAnyOrder(shift1, shift2);
    }

    @Test
    void testFindByEmployeeId() {
        List<Shift> employee2Shifts = shiftRepository.findByEmployeeId(employee2.getId());
        
        assertThat(employee2Shifts).hasSize(2);
        assertThat(employee2Shifts).containsExactlyInAnyOrder(shift3, shift4);
    }

    @Test
    void testExistsByEmployee() {
        assertThat(shiftRepository.existsByEmployee(employee1)).isTrue();
        assertThat(shiftRepository.existsByEmployee(employee2)).isTrue();
        assertThat(shiftRepository.existsByEmployeeId(999L)).isFalse();
    }

    @Test
    void testExistsByEmployeeId() {
        assertThat(shiftRepository.existsByEmployeeId(employee1.getId())).isTrue();
        assertThat(shiftRepository.existsByEmployeeId(employee2.getId())).isTrue();
        assertThat(shiftRepository.existsByEmployeeId(999L)).isFalse();
    }

    // ==================== TIME-BASED QUERIES TESTS ====================

    @Test
    void testFindByStartAtAfter() {
        LocalDateTime cutoff = LocalDateTime.of(2024, 1, 15, 12, 0);
        List<Shift> shiftsAfter = shiftRepository.findByStartAtAfter(cutoff);
        
        assertThat(shiftsAfter).hasSize(5); // shift2, shift3, shift4, shift6, shift7, shift8
        assertThat(shiftsAfter).allMatch(shift -> shift.getStartAt().isAfter(cutoff));
    }

    @Test
    void testFindByStartAtBefore() {
        LocalDateTime cutoff = LocalDateTime.of(2024, 1, 15, 12, 0);
        List<Shift> shiftsBefore = shiftRepository.findByStartAtBefore(cutoff);
        
        assertThat(shiftsBefore).hasSize(3); // shift1, shift5, shift8
        assertThat(shiftsBefore).allMatch(shift -> shift.getStartAt().isBefore(cutoff));
    }

    @Test
    void testFindByStartAtBetween() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 8, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 15, 16, 0);
        List<Shift> shiftsBetween = shiftRepository.findByStartAtBetween(start, end);
        
        assertThat(shiftsBetween).hasSize(3); // shift1, shift8, shift2
        assertThat(shiftsBetween).allMatch(shift -> 
            shift.getStartAt().isAfter(start) && shift.getStartAt().isBefore(end));
    }

    @Test
    void testFindByEndAtAfter() {
        LocalDateTime cutoff = LocalDateTime.of(2024, 1, 15, 18, 0);
        List<Shift> shiftsEndAfter = shiftRepository.findByEndAtAfter(cutoff);
        
        assertThat(shiftsEndAfter).hasSize(5); // shift3, shift7, shift8, shift2, shift4, shift6
        assertThat(shiftsEndAfter).allMatch(shift -> shift.getEndAt().isAfter(cutoff));
    }

    @Test
    void testFindByEndAtBefore() {
        LocalDateTime cutoff = LocalDateTime.of(2024, 1, 15, 18, 0);
        List<Shift> shiftsEndBefore = shiftRepository.findByEndAtBefore(cutoff);
        
        assertThat(shiftsEndBefore).hasSize(2); // shift1, shift5
        assertThat(shiftsEndBefore).allMatch(shift -> shift.getEndAt().isBefore(cutoff));
    }

    @Test
    void testFindByEndAtBetween() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 16, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 15, 20, 0);
        List<Shift> shiftsEndBetween = shiftRepository.findByEndAtBetween(start, end);
        
        assertThat(shiftsEndBetween).hasSize(2); // shift1, shift8
        assertThat(shiftsEndBetween).allMatch(shift -> 
            shift.getEndAt().isAfter(start) && shift.getEndAt().isBefore(end));
    }

    // ==================== OVERLAP DETECTION TESTS ====================

    @Test
    void testFindOverlappingShifts() {
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 15, 14, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 1, 15, 18, 0);
        List<Shift> overlappingShifts = shiftRepository.findOverlappingShifts(startTime, endTime);
        
        assertThat(overlappingShifts).hasSize(4); // shift1, shift7, shift8, shift3
        assertThat(overlappingShifts).containsExactlyInAnyOrder(shift1, shift7, shift8, shift3);
    }

    @Test
    void testFindOverlappingShiftsForEmployee() {
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 15, 14, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 1, 15, 18, 0);
        List<Shift> overlappingShifts = shiftRepository.findOverlappingShiftsForEmployee(startTime, endTime, employee1);
        
        assertThat(overlappingShifts).hasSize(1);
        assertThat(overlappingShifts).containsExactly(shift1);
    }

    @Test
    void testFindOverlappingShiftsForEmployeeId() {
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 15, 14, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 1, 15, 18, 0);
        List<Shift> overlappingShifts = shiftRepository.findOverlappingShiftsForEmployeeId(startTime, endTime, employee1.getId());
        
        assertThat(overlappingShifts).hasSize(1);
        assertThat(overlappingShifts).containsExactly(shift1);
    }

    @Test
    void testFindOverlappingShiftsNoOverlap() {
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 15, 2, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 1, 15, 6, 0);
        List<Shift> overlappingShifts = shiftRepository.findOverlappingShifts(startTime, endTime);
        
        assertThat(overlappingShifts).hasSize(1);// shift5 (01:00-09:00) overlaps with 02:00-06:00
    }

    // ==================== EMPLOYEE AND TIME COMBINED QUERIES TESTS ====================

    @Test
    void testFindByEmployeeAndStartAtAfter() {
        LocalDateTime cutoff = LocalDateTime.of(2024, 1, 15, 12, 0);
        List<Shift> shifts = shiftRepository.findByEmployeeAndStartAtAfter(employee1, cutoff);
        
        assertThat(shifts).hasSize(1);
        assertThat(shifts).containsExactly(shift2);
    }

    @Test
    void testFindByEmployeeAndStartAtBetween() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 8, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 15, 16, 0);
        List<Shift> shifts = shiftRepository.findByEmployeeAndStartAtBetween(employee1, start, end);
        
        assertThat(shifts).hasSize(1);
        assertThat(shifts).containsExactly(shift1);
    }

    @Test
    void testFindByEmployeeIdAndStartAtAfter() {
        LocalDateTime cutoff = LocalDateTime.of(2024, 1, 15, 12, 0);
        List<Shift> shifts = shiftRepository.findByEmployeeIdAndStartAtAfter(employee2.getId(), cutoff);
        
        assertThat(shifts).hasSize(2);
        assertThat(shifts).containsExactlyInAnyOrder(shift3, shift4);
    }

    // ==================== PAGINATION TESTS ====================

    @Test
    void testFindByEmployeeWithPagination() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Shift> page = shiftRepository.findByEmployee(employee1, pageable);
        
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void testFindByStartAtAfterWithPagination() {
        LocalDateTime cutoff = LocalDateTime.of(2024, 1, 15, 12, 0);
        Pageable pageable = PageRequest.of(0, 2);
        Page<Shift> page = shiftRepository.findByStartAtAfter(cutoff, pageable);
        
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(5);
    }

    // ==================== STATISTICS TESTS ====================

    @Test
    void testGetTotalShiftCount() {
        long totalCount = shiftRepository.getTotalShiftCount();
        
        assertThat(totalCount).isEqualTo(8);
    }

    @Test
    void testGetShiftCountByEmployee() {
        long count = shiftRepository.getShiftCountByEmployee(employee1);
        
        assertThat(count).isEqualTo(2);
    }

    @Test
    void testGetShiftCountByEmployeeId() {
        long count = shiftRepository.getShiftCountByEmployeeId(employee2.getId());
        
        assertThat(count).isEqualTo(2);
    }

    @Test
    void testGetShiftCountInDateRange() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 15, 23, 59);
        long count = shiftRepository.getShiftCountInDateRange(start, end);
        
        assertThat(count).isEqualTo(4); // All shifts on 2024-01-15
    }

    // ==================== VALIDATION QUERIES TESTS ====================

    @Test
    void testHasOverlappingShift() {
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 15, 14, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 1, 15, 18, 0);
        
        assertThat(shiftRepository.hasOverlappingShift(employee1, startTime, endTime)).isTrue();
        assertThat(shiftRepository.hasOverlappingShift(employee2, startTime, endTime)).isTrue(); // shift3 overlaps
    }

    @Test
    void testHasOverlappingShiftForEmployeeId() {
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 15, 14, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 1, 15, 18, 0);
        
        assertThat(shiftRepository.hasOverlappingShiftForEmployeeId(employee1.getId(), startTime, endTime)).isTrue();
        assertThat(shiftRepository.hasOverlappingShiftForEmployeeId(employee2.getId(), startTime, endTime)).isTrue();
    }

    @Test
    void testHasOverlappingShiftNoOverlap() {
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 15, 2, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 1, 15, 6, 0);
        
        assertThat(shiftRepository.hasOverlappingShift(employee1, startTime, endTime)).isFalse();
        assertThat(shiftRepository.hasOverlappingShiftForEmployeeId(employee1.getId(), startTime, endTime)).isFalse(); 
    }

    // ==================== RECENT SHIFTS TESTS ====================

    @Test
    void testFindRecentShifts() {
        LocalDateTime cutoff = LocalDateTime.of(2024, 1, 15, 12, 0);
        List<Shift> recentShifts = shiftRepository.findRecentShifts(cutoff);
        
        assertThat(recentShifts).hasSize(5);
        assertThat(recentShifts).allMatch(shift -> shift.getStartAt().isAfter(cutoff));
    }

    @Test
    void testFindRecentShiftsByEmployee() {
        LocalDateTime cutoff = LocalDateTime.of(2024, 1, 15, 12, 0);
        List<Shift> recentShifts = shiftRepository.findRecentShiftsByEmployee(employee1, cutoff);
        
        assertThat(recentShifts).hasSize(1);
        assertThat(recentShifts).containsExactly(shift2);
    }

    @Test
    void testFindRecentShiftsByEmployeeId() {
        LocalDateTime cutoff = LocalDateTime.of(2024, 1, 15, 12, 0);
        List<Shift> recentShifts = shiftRepository.findRecentShiftsByEmployeeId(employee2.getId(), cutoff);
        
        assertThat(recentShifts).hasSize(2);
        assertThat(recentShifts).containsExactlyInAnyOrder(shift3, shift4);
    }
}
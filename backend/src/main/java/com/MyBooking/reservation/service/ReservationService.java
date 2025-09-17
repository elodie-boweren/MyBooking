// Service responsibilities: Reservation Management - Create, update, cancel reservations
// Availability Validation - Check room availability and prevent overlaps
// Business Rules - Pricing, dates, guest limits, status transitions
// Integration - Connect with RoomService, AuthService, and other services
// Guest Management - Handle guest information and preferences

package com.MyBooking.reservation.service;

import com.MyBooking.reservation.domain.Reservation;
import com.MyBooking.reservation.domain.ReservationStatus;
import com.MyBooking.reservation.repository.ReservationRepository;
import com.MyBooking.room.domain.Room;
import com.MyBooking.room.domain.RoomStatus;
import com.MyBooking.room.repository.RoomRepository;
import com.MyBooking.room.service.RoomService;
import com.MyBooking.auth.domain.User;
import com.MyBooking.auth.domain.Role;
import com.MyBooking.auth.repository.UserRepository;
import com.MyBooking.common.exception.BusinessRuleException;
import com.MyBooking.common.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoomService roomService;

    // ========== RESERVATION MANAGEMENT ==========

    /**
     * Create a new reservation with comprehensive validation
     */
    public Reservation createReservation(Long roomId, Long clientId, LocalDate checkIn, 
                                       LocalDate checkOut, Integer numberOfGuests, String currency) {
        
        // Validate inputs
        validateReservationInputs(checkIn, checkOut, numberOfGuests, currency);
        
        // Get and validate room
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new NotFoundException("Room not found with ID: " + roomId));
        
        // Get and validate client
        User client = userRepository.findById(clientId)
            .orElseThrow(() -> new NotFoundException("Client not found with ID: " + clientId));
        
        // Validate room capacity
        validateRoomCapacity(room, numberOfGuests);
        
        // Check room availability
        checkRoomAvailability(roomId, checkIn, checkOut);
        
        // Calculate total price
        BigDecimal totalPrice = calculateTotalPrice(room, checkIn, checkOut, numberOfGuests);
        
        // Create reservation
        Reservation reservation = new Reservation();
        reservation.setCheckIn(checkIn);
        reservation.setCheckOut(checkOut);
        reservation.setNumberOfGuests(numberOfGuests);
        reservation.setTotalPrice(totalPrice);
        reservation.setCurrency(currency);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setClient(client);
        reservation.setRoom(room);
        
        Reservation savedReservation = reservationRepository.save(reservation);
        
        // Update room status to occupied for the entire reservation period
        roomService.updateRoomStatusAutomatically(roomId, RoomStatus.OCCUPIED, 
            "Reservation created for " + checkIn + " to " + checkOut);
        
        return savedReservation;
    }

    /**
     * Get reservation by ID
     */
    @Transactional(readOnly = true)
    public Reservation getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId)
            .orElseThrow(() -> new NotFoundException("Reservation not found with ID: " + reservationId));
    }

    // ========== VALIDATION METHODS ==========

    /**
     * Validate reservation inputs
     */
    private void validateReservationInputs(LocalDate checkIn, LocalDate checkOut, 
                                         Integer numberOfGuests, String currency) {
        
        LocalDate today = LocalDate.now();
        
        // Check-in date validation
        if (checkIn.isBefore(today)) {
            throw new BusinessRuleException("Check-in date cannot be in the past");
        }
        
        // Check-out date validation
        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            throw new BusinessRuleException("Check-out date must be after check-in date");
        }
        
        // Maximum stay validation (e.g., 30 days)
        long daysBetween = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (daysBetween > 30) {
            throw new BusinessRuleException("Maximum stay is 30 days");
        }
        
        // Guest count validation
        if (numberOfGuests < 1 || numberOfGuests > 10) {
            throw new BusinessRuleException("Number of guests must be between 1 and 10");
        }
        
        // Currency validation
        if (currency == null || currency.length() != 3) {
            throw new BusinessRuleException("Currency must be a 3-character code (e.g., USD, EUR)");
        }
    }

    /**
     * Validate room capacity
     */
    private void validateRoomCapacity(Room room, Integer numberOfGuests) {
        if (numberOfGuests > room.getCapacity()) {
            throw new BusinessRuleException("Number of guests (" + numberOfGuests + 
                ") exceeds room capacity (" + room.getCapacity() + ")");
        }
    }

    /**
     * Check room availability for new reservation
     */
    private void checkRoomAvailability(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        List<Reservation> conflicts = reservationRepository.checkRoomAvailability(roomId, checkIn, checkOut);
        
        if (!conflicts.isEmpty()) {
            throw new BusinessRuleException("Room is not available for the selected dates. " +
                "Conflicting reservations: " + conflicts.size());
        }
    }

    /**
     * Calculate total price for a reservation
     */
    public BigDecimal calculateTotalPrice(Room room, LocalDate checkIn, LocalDate checkOut, Integer numberOfGuests) {
        long numberOfNights = ChronoUnit.DAYS.between(checkIn, checkOut);
        
        // Base price calculation
        BigDecimal basePrice = room.getPrice().multiply(BigDecimal.valueOf(numberOfNights));
        
        // Additional guest fee (if more than 2 guests)
        BigDecimal additionalGuestFee = BigDecimal.ZERO;
        if (numberOfGuests > 2) {
            int extraGuests = numberOfGuests - 2;
            BigDecimal feePerExtraGuest = BigDecimal.valueOf(25.00); // $25 per extra guest per night
            additionalGuestFee = feePerExtraGuest.multiply(BigDecimal.valueOf(extraGuests))
                                                .multiply(BigDecimal.valueOf(numberOfNights));
        }
        
        // Tax calculation (10% tax)
        BigDecimal subtotal = basePrice.add(additionalGuestFee);
        BigDecimal tax = subtotal.multiply(BigDecimal.valueOf(0.10));
        
        // Total price
        BigDecimal totalPrice = subtotal.add(tax);
        
        return totalPrice.setScale(2, RoundingMode.HALF_UP);
    }

    // ========== UPDATE & CANCEL OPERATIONS ==========

    /**
     * Update an existing reservation
     */
    public Reservation updateReservation(Long reservationId, LocalDate newCheckIn, LocalDate newCheckOut, 
                                       Integer newNumberOfGuests, String currency) {
        
        Reservation reservation = getReservationById(reservationId);
        
        // Validate new inputs
        validateReservationInputs(newCheckIn, newCheckOut, newNumberOfGuests, currency);
        
        // Validate room capacity for new guest count
        validateRoomCapacity(reservation.getRoom(), newNumberOfGuests);
        
        // Check availability for new dates (excluding current reservation)
        checkRoomAvailabilityForUpdate(reservation.getRoom().getId(), newCheckIn, newCheckOut, reservationId);
        
        // Update reservation
        reservation.setCheckIn(newCheckIn);
        reservation.setCheckOut(newCheckOut);
        reservation.setNumberOfGuests(newNumberOfGuests);
        reservation.setCurrency(currency);
        
        // Recalculate price
        BigDecimal newTotalPrice = calculateTotalPrice(reservation.getRoom(), newCheckIn, newCheckOut, newNumberOfGuests);
        reservation.setTotalPrice(newTotalPrice);
        
        return reservationRepository.save(reservation);
    }

    /**
     * Cancel a reservation with business rules
     */
    public void cancelReservation(Long reservationId, String reason) {
        Reservation reservation = getReservationById(reservationId);
        
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new BusinessRuleException("Reservation is already cancelled");
        }
        
        // Check cancellation policy - allow cancellation up to check-in date
        LocalDate today = LocalDate.now();
        if (reservation.getCheckIn().isBefore(today)) {
            throw new BusinessRuleException("Cannot cancel reservation after check-in date has passed");
        }
        
        // Update reservation status
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
        
        // Update room status back to available when reservation is cancelled
        roomService.updateRoomStatusAutomatically(reservation.getRoom().getId(), RoomStatus.AVAILABLE, 
            "Reservation cancelled: " + reason);
    }

    /**
     * Check room availability for reservation update (excluding current reservation)
     */
    private void checkRoomAvailabilityForUpdate(Long roomId, LocalDate checkIn, LocalDate checkOut, Long excludeReservationId) {
        List<Reservation> conflicts = reservationRepository.checkRoomAvailability(roomId, checkIn, checkOut);
        
        // Remove the current reservation from conflicts
        conflicts.removeIf(r -> r.getId().equals(excludeReservationId));
        
        if (!conflicts.isEmpty()) {
            throw new BusinessRuleException("Room is not available for the selected dates. " +
                "Conflicting reservations: " + conflicts.size());
        }
    }

    // ========== AVAILABILITY & SEARCH ==========

    /**
     * Check if a room is available for the given date range
     */
    @Transactional(readOnly = true)
    public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        List<Reservation> conflicts = reservationRepository.checkRoomAvailability(roomId, checkIn, checkOut);
        return conflicts.isEmpty();
    }

    /**
     * Get available rooms for a date range
     * In our simplified model, rooms are available if they don't have conflicting reservations
     */
    @Transactional(readOnly = true)
    public List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut, Integer numberOfGuests) {
        // Get all rooms from the system
        List<Room> allRooms = roomRepository.findAll();
        
        // Filter by capacity and availability
        return allRooms.stream()
            .filter(room -> room.getCapacity() >= numberOfGuests)
            .filter(room -> isRoomAvailable(room.getId(), checkIn, checkOut))
            .toList();
    }

    /**
     * Get reservations by guest
     */
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByGuest(Long clientId) {
        return reservationRepository.findByClientId(clientId);
    }

    /**
     * Get reservations by room
     */
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByRoom(Long roomId) {
        return reservationRepository.findByRoomId(roomId);
    }

    /**
     * Get reservations by status
     */
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }

    // ========== RESERVATION STATUS MANAGEMENT ==========

    /**
     * Confirm a reservation (if needed for future business logic)
     */
    public Reservation confirmReservation(Long reservationId) {
        Reservation reservation = getReservationById(reservationId);
        
        if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            throw new BusinessRuleException("Reservation is already confirmed");
        }
        
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new BusinessRuleException("Cannot confirm a cancelled reservation");
        }
        
        reservation.setStatus(ReservationStatus.CONFIRMED);
        return reservationRepository.save(reservation);
    }

    // ========== SEARCH & FILTERING ==========

    /**
     * Search reservations with multiple criteria
     */
    @Transactional(readOnly = true)
    public Page<Reservation> searchReservations(Long clientId, Long roomId, ReservationStatus status,
                                              BigDecimal minPrice, BigDecimal maxPrice, String currency,
                                              Integer minGuests, Integer maxGuests,
                                              LocalDate checkInFrom, LocalDate checkInTo,
                                              Pageable pageable) {
        return reservationRepository.findByCriteria(clientId, roomId, status, minPrice, maxPrice, 
            currency, minGuests, maxGuests, checkInFrom, checkInTo, pageable);
    }

    /**
     * Get reservations by date range
     */
    @Transactional(readOnly = true)
    public List<Reservation> getReservationsByDateRange(LocalDate startDate, LocalDate endDate) {
        return reservationRepository.findByCheckInBetween(startDate, endDate);
    }

    /**
     * Get upcoming reservations
     */
    @Transactional(readOnly = true)
    public List<Reservation> getUpcomingReservations() {
        LocalDate today = LocalDate.now();
        return reservationRepository.findByCheckInGreaterThanEqual(today);
    }

    /**
     * Get active reservations (current guests)
     * In our simplified model, these are confirmed reservations where today is between check-in and check-out
     */
    @Transactional(readOnly = true)
    public List<Reservation> getActiveReservations() {
        LocalDate today = LocalDate.now();
        return reservationRepository.findByCheckInLessThanEqualAndCheckOutGreaterThanEqual(today, today);
    }

    // ========== STATISTICS & ANALYTICS ==========

    /**
     * Get reservation statistics
     */
    @Transactional(readOnly = true)
    public ReservationStatistics getReservationStatistics() {
        long totalReservations = reservationRepository.count();
        long confirmedReservations = reservationRepository.countByStatus(ReservationStatus.CONFIRMED);
        long cancelledReservations = reservationRepository.countByStatus(ReservationStatus.CANCELLED);
        
        BigDecimal totalRevenue = reservationRepository.getTotalRevenue();
        BigDecimal averagePrice = reservationRepository.getAverageReservationPrice();
        
        return new ReservationStatistics(totalReservations, confirmedReservations, 
            cancelledReservations, totalRevenue, averagePrice);
    }

    /**
     * Get revenue by currency
     */
    @Transactional(readOnly = true)
    public BigDecimal getRevenueByCurrency(String currency) {
        return reservationRepository.getTotalRevenueByCurrency(currency);
    }

    // ========== ADMIN OPERATIONS ==========

    /**
     * Get all reservations (Admin only)
     */
    @Transactional(readOnly = true)
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    /**
     * Get all reservations with pagination (Admin only)
     */
    @Transactional(readOnly = true)
    public Page<Reservation> getAllReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable);
    }

    // ========== INNER CLASSES ==========

    /**
     * Reservation statistics data class
     */
    public static class ReservationStatistics {
        private final long totalReservations;
        private final long confirmedReservations;
        private final long cancelledReservations;
        private final BigDecimal totalRevenue;
        private final BigDecimal averagePrice;

        public ReservationStatistics(long totalReservations, long confirmedReservations, 
                                   long cancelledReservations, BigDecimal totalRevenue, BigDecimal averagePrice) {
            this.totalReservations = totalReservations;
            this.confirmedReservations = confirmedReservations;
            this.cancelledReservations = cancelledReservations;
            this.totalRevenue = totalRevenue;
            this.averagePrice = averagePrice;
        }

        // Getters
        public long getTotalReservations() { return totalReservations; }
        public long getConfirmedReservations() { return confirmedReservations; }
        public long getCancelledReservations() { return cancelledReservations; }
        public BigDecimal getTotalRevenue() { return totalRevenue; }
        public BigDecimal getAveragePrice() { return averagePrice; }
        
        public double getConfirmationRate() {
            return totalReservations > 0 ? (double) confirmedReservations / totalReservations * 100 : 0;
        }
        
        public double getCancellationRate() {
            return totalReservations > 0 ? (double) cancelledReservations / totalReservations * 100 : 0;
        }
    }
}

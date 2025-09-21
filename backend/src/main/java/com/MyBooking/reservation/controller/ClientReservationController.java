package com.MyBooking.reservation.controller;

import com.MyBooking.reservation.dto.*;
import com.MyBooking.reservation.service.ReservationService;
import com.MyBooking.common.security.JwtService;
import com.MyBooking.common.exception.BusinessRuleException;
import com.MyBooking.common.exception.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for client-specific reservation operations
 * Handles reservation creation, viewing, updating, and cancellation by clients
 */
@RestController
@RequestMapping("/api/client/reservations")
public class ClientReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private JwtService jwtService;

    /**
     * Create a new reservation
     * POST /api/client/reservations
     */
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ReservationResponseDto> createReservation(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody ReservationCreateRequestDto request) {
        try {
            Long clientId = extractUserIdFromToken(authHeader);
            ReservationResponseDto response = reservationService.createReservation(request, clientId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (BusinessRuleException e) {
            e.printStackTrace();
            throw e; // Let GlobalExceptionHandler handle it
        } catch (Exception e) {
            e.printStackTrace();
            throw e; // Let GlobalExceptionHandler handle it
        }
    }

    /**
     * Get my reservations with pagination
     * GET /api/client/reservations/my
     */
    @GetMapping("/my")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Page<ReservationResponseDto>> getMyReservations(
            @RequestHeader("Authorization") String authHeader,
            Pageable pageable) {
        try {
            Long clientId = extractUserIdFromToken(authHeader);
            Page<ReservationResponseDto> reservations = reservationService.getReservationsByClientId(clientId, pageable);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get reservation by ID (client can only view their own reservations)
     * GET /api/client/reservations/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ReservationResponseDto> getReservation(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            Long clientId = extractUserIdFromToken(authHeader);
            ReservationResponseDto reservation = reservationService.getReservationByIdAndClientId(id, clientId);
            return ResponseEntity.ok(reservation);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BusinessRuleException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update my reservation
     * PUT /api/client/reservations/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ReservationResponseDto> updateReservation(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody ReservationUpdateRequestDto request) {
        try {
            Long clientId = extractUserIdFromToken(authHeader);
            ReservationResponseDto updatedReservation = reservationService.updateReservation(id, request, clientId);
            return ResponseEntity.ok(updatedReservation);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BusinessRuleException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Cancel my reservation
     * DELETE /api/client/reservations/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> cancelReservation(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            Long clientId = extractUserIdFromToken(authHeader);
            reservationService.cancelReservation(id, clientId);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BusinessRuleException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Extract user ID from JWT token
     */
    private Long extractUserIdFromToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String email = jwtService.extractUsername(token);
            return reservationService.getUserIdByEmail(email);
        }
        throw new BusinessRuleException("Invalid authorization header");
    }
}

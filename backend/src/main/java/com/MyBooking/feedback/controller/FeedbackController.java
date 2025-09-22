package com.MyBooking.feedback.controller;

import com.MyBooking.feedback.dto.*;
import com.MyBooking.feedback.service.FeedbackService;
import com.MyBooking.common.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for feedback management (Client access)
 */
@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final JwtService jwtService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService, JwtService jwtService) {
        this.feedbackService = feedbackService;
        this.jwtService = jwtService;
    }

    // ==================== CLIENT FEEDBACK ENDPOINTS ====================

    /**
     * Create new feedback for a reservation
     */
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<FeedbackResponseDto> createFeedback(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreateFeedbackRequestDto request) {
        
        Long userId = extractUserIdFromToken(token);
        FeedbackResponseDto response = feedbackService.createFeedbackAsDto(
            userId, request.getReservationId(), request.getRating(), request.getComment());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get feedback by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT') or hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<FeedbackResponseDto> getFeedbackById(@PathVariable Long id) {
        FeedbackResponseDto response = feedbackService.getFeedbackByIdAsDto(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Update feedback (only by the user who created it)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<FeedbackResponseDto> updateFeedback(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @Valid @RequestBody UpdateFeedbackRequestDto request) {
        
        Long userId = extractUserIdFromToken(token);
        FeedbackResponseDto response = feedbackService.updateFeedbackAsDto(
            id, userId, request.getRating(), request.getComment());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Delete feedback (only by the user who created it)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> deleteFeedback(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        
        Long userId = extractUserIdFromToken(token);
        feedbackService.deleteFeedback(id, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get my feedbacks (paginated)
     */
    @GetMapping("/my")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Page<FeedbackResponseDto>> getMyFeedbacks(
            @RequestHeader("Authorization") String token,
            Pageable pageable) {
        
        Long userId = extractUserIdFromToken(token);
        Page<FeedbackResponseDto> response = feedbackService.getFeedbacksByUserIdAsDto(userId, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Get feedbacks for a specific reservation
     */
    @GetMapping("/reservation/{reservationId}")
    @PreAuthorize("hasRole('CLIENT') or hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<Page<FeedbackResponseDto>> getFeedbacksByReservation(
            @PathVariable Long reservationId,
            Pageable pageable) {
        
        Page<FeedbackResponseDto> response = feedbackService.getFeedbacksByReservationIdAsDto(reservationId, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Search feedbacks with criteria
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('CLIENT') or hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<Page<FeedbackResponseDto>> searchFeedbacks(
            @ModelAttribute FeedbackSearchCriteriaDto criteria,
            Pageable pageable) {
        
        Page<FeedbackResponseDto> response = feedbackService.searchFeedbacksAsDto(criteria, pageable);
        return ResponseEntity.ok(response);
    }

    // ==================== HELPER METHODS ====================

    private Long extractUserIdFromToken(String token) {
        String cleanToken = token.replace("Bearer ", "");
        String email = jwtService.extractUsername(cleanToken);
        return feedbackService.getUserIdByEmail(email);
    }
}

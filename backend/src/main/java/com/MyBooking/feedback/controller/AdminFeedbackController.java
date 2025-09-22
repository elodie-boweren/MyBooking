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
 * Controller for admin feedback management
 */
@RestController
@RequestMapping("/api/admin/feedback")
public class AdminFeedbackController {

    private final FeedbackService feedbackService;
    private final JwtService jwtService;

    @Autowired
    public AdminFeedbackController(FeedbackService feedbackService, JwtService jwtService) {
        this.feedbackService = feedbackService;
        this.jwtService = jwtService;
    }

    // ==================== ADMIN FEEDBACK MANAGEMENT ====================

    /**
     * Get all feedbacks (admin only)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<FeedbackResponseDto>> getAllFeedbacks(Pageable pageable) {
        Page<FeedbackResponseDto> response = feedbackService.getAllFeedbacksAsDto(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Get feedback by ID (admin only)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FeedbackResponseDto> getFeedbackById(@PathVariable Long id) {
        FeedbackResponseDto response = feedbackService.getFeedbackByIdAsDto(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Update feedback (admin only)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FeedbackResponseDto> updateFeedback(
            @PathVariable Long id,
            @Valid @RequestBody UpdateFeedbackRequestDto request) {
        
        FeedbackResponseDto response = feedbackService.updateFeedbackAsDto(
            id, null, request.getRating(), request.getComment());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Delete feedback (admin only)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id, null);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search all feedbacks with criteria (admin only)
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<FeedbackResponseDto>> searchAllFeedbacks(
            @ModelAttribute FeedbackSearchCriteriaDto criteria,
            Pageable pageable) {
        
        Page<FeedbackResponseDto> response = feedbackService.searchFeedbacksAsDto(criteria, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Get feedbacks by user ID (admin only)
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<FeedbackResponseDto>> getFeedbacksByUser(
            @PathVariable Long userId,
            Pageable pageable) {
        
        Page<FeedbackResponseDto> response = feedbackService.getFeedbacksByUserIdAsDto(userId, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Get feedbacks by reservation ID (admin only)
     */
    @GetMapping("/reservation/{reservationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<FeedbackResponseDto>> getFeedbacksByReservation(
            @PathVariable Long reservationId,
            Pageable pageable) {
        
        Page<FeedbackResponseDto> response = feedbackService.getFeedbacksByReservationIdAsDto(reservationId, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Get feedback statistics (admin only)
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FeedbackStatisticsDto> getFeedbackStatistics() {
        FeedbackStatisticsDto statistics = feedbackService.getFeedbackStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get high-rated feedbacks (4-5 stars) (admin only)
     */
    @GetMapping("/high-rated")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<FeedbackResponseDto>> getHighRatedFeedbacks(Pageable pageable) {
        Page<FeedbackResponseDto> response = feedbackService.getHighRatedFeedbacksAsDto(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Get low-rated feedbacks (1-2 stars) (admin only)
     */
    @GetMapping("/low-rated")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<FeedbackResponseDto>> getLowRatedFeedbacks(Pageable pageable) {
        Page<FeedbackResponseDto> response = feedbackService.getLowRatedFeedbacksAsDto(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Get feedbacks with comments (admin only)
     */
    @GetMapping("/with-comments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<FeedbackResponseDto>> getFeedbacksWithComments(Pageable pageable) {
        Page<FeedbackResponseDto> response = feedbackService.getFeedbacksWithCommentsAsDto(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Get feedbacks without comments (admin only)
     */
    @GetMapping("/without-comments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<FeedbackResponseDto>> getFeedbacksWithoutComments(Pageable pageable) {
        Page<FeedbackResponseDto> response = feedbackService.getFeedbacksWithoutCommentsAsDto(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Get recent feedbacks (admin only)
     */
    @GetMapping("/recent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<FeedbackResponseDto>> getRecentFeedbacks(
            @RequestParam(defaultValue = "7") int days,
            Pageable pageable) {
        
        Page<FeedbackResponseDto> response = feedbackService.getRecentFeedbacksAsDto(days, pageable);
        return ResponseEntity.ok(response);
    }
}

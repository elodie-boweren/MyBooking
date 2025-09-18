package com.MyBooking.feedback.service;

import com.MyBooking.feedback.domain.Feedback;
import com.MyBooking.feedback.domain.FeedbackReply;
import com.MyBooking.feedback.repository.FeedbackRepository;
import com.MyBooking.feedback.repository.FeedbackReplyRepository;
import com.MyBooking.auth.domain.Role;
import com.MyBooking.auth.domain.User;
import com.MyBooking.auth.repository.UserRepository;
import com.MyBooking.reservation.domain.Reservation;
import com.MyBooking.reservation.repository.ReservationRepository;
import com.MyBooking.common.exception.BusinessRuleException;
import com.MyBooking.common.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackReplyRepository feedbackReplyRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository,
                          FeedbackReplyRepository feedbackReplyRepository,
                          UserRepository userRepository,
                          ReservationRepository reservationRepository) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackReplyRepository = feedbackReplyRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    // ==================== FEEDBACK MANAGEMENT ====================

    @Transactional
    public Feedback createFeedback(Long userId, Long reservationId, Integer rating, String comment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));
        
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation not found with ID: " + reservationId));

        // Validate business rules
        validateFeedbackCreation(user, reservation, rating);

        // Check if feedback already exists for this user and reservation
        if (feedbackRepository.existsByUserAndReservation(user, reservation)) {
            throw new BusinessRuleException("Feedback already exists for this reservation.");
        }

        Feedback feedback = new Feedback(reservation, user, rating, comment);
        return feedbackRepository.save(feedback);
    }

    @Transactional
    public Feedback updateFeedback(Long feedbackId, Long currentUserId, Integer rating, String comment) {
        Feedback feedback = getFeedbackById(feedbackId);
        validateFeedbackOwnership(feedback, currentUserId);

        // Validate rating
        if (rating != null && (rating < 1 || rating > 5)) {
            throw new BusinessRuleException("Rating must be between 1 and 5.");
        }

        if (rating != null) {
            feedback.setRating(rating);
        }
        if (comment != null) {
            feedback.setComment(comment);
        }

        return feedbackRepository.save(feedback);
    }

    @Transactional
    public void deleteFeedback(Long feedbackId, Long currentUserId) {
        Feedback feedback = getFeedbackById(feedbackId);
        validateFeedbackOwnership(feedback, currentUserId);

        // Delete all replies first (cascade should handle this, but being explicit)
        List<FeedbackReply> replies = feedbackReplyRepository.findByFeedback(feedback);
        feedbackReplyRepository.deleteAll(replies);

        feedbackRepository.delete(feedback);
    }

    @Transactional(readOnly = true)
    public Feedback getFeedbackById(Long feedbackId) {
        return feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new NotFoundException("Feedback not found with ID: " + feedbackId));
    }

    @Transactional(readOnly = true)
    public Page<Feedback> getAllFeedbacks(Pageable pageable) {
        return feedbackRepository.findAll(pageable);
    }

    // ==================== FEEDBACK QUERIES ====================

    @Transactional(readOnly = true)
    public Page<Feedback> getFeedbacksByUser(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));
        return feedbackRepository.findByUser(user, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Feedback> getFeedbacksByReservation(Long reservationId, Pageable pageable) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation not found with ID: " + reservationId));
        return feedbackRepository.findByReservation(reservation, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Feedback> getFeedbacksByRating(Integer rating, Pageable pageable) {
        if (rating < 1 || rating > 5) {
            throw new BusinessRuleException("Rating must be between 1 and 5.");
        }
        return feedbackRepository.findByRating(rating, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Feedback> getFeedbacksByRatingRange(Integer minRating, Integer maxRating, Pageable pageable) {
        if (minRating < 1 || maxRating > 5 || minRating > maxRating) {
            throw new BusinessRuleException("Invalid rating range. Min: 1-5, Max: 1-5, Min <= Max.");
        }
        return feedbackRepository.findByRatingBetween(minRating, maxRating, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Feedback> getFeedbacksByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        if (startDate.isAfter(endDate)) {
            throw new BusinessRuleException("Start date must be before end date.");
        }
        return feedbackRepository.findByCreatedAtBetween(startDate, endDate, pageable);
    }

    @Transactional(readOnly = true)
    public List<Feedback> getRecentFeedbacks(int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return feedbackRepository.findRecentFeedbacks(since);
    }

    @Transactional(readOnly = true)
    public List<Feedback> getHighRatedFeedbacks() {
        return feedbackRepository.findHighRatedFeedbacks();
    }

    @Transactional(readOnly = true)
    public List<Feedback> getLowRatedFeedbacks() {
        return feedbackRepository.findLowRatedFeedbacks();
    }

    @Transactional(readOnly = true)
    public List<Feedback> getFeedbacksWithComments() {
        return feedbackRepository.findFeedbacksWithComments();
    }

    @Transactional(readOnly = true)
    public List<Feedback> getFeedbacksWithoutComments() {
        return feedbackRepository.findFeedbacksWithoutComments();
    }

    @Transactional(readOnly = true)
    public Optional<Feedback> getFeedbackByUserAndReservation(Long userId, Long reservationId) {
        return feedbackRepository.findByUserIdAndReservationId(userId, reservationId);
    }

    // ==================== FEEDBACK REPLY MANAGEMENT ====================

    @Transactional
    public FeedbackReply addReply(Long feedbackId, Long adminUserId, String message) {
        Feedback feedback = getFeedbackById(feedbackId);
        User adminUser = userRepository.findById(adminUserId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + adminUserId));

        // Validate admin role
        if (adminUser.getRole() != Role.ADMIN) {
            throw new BusinessRuleException("Only admin users can reply to feedback.");
        }

        if (message == null || message.trim().isEmpty()) {
            throw new BusinessRuleException("Reply message cannot be empty.");
        }

        FeedbackReply reply = new FeedbackReply(feedback, adminUser, message.trim());
        return feedbackReplyRepository.save(reply);
    }

    @Transactional
    public FeedbackReply updateReply(Long replyId, Long currentUserId, String message) {
        FeedbackReply reply = feedbackReplyRepository.findById(replyId)
                .orElseThrow(() -> new NotFoundException("Feedback reply not found with ID: " + replyId));

        validateReplyOwnership(reply, currentUserId);

        if (message == null || message.trim().isEmpty()) {
            throw new BusinessRuleException("Reply message cannot be empty.");
        }

        reply.setMessage(message.trim());
        return feedbackReplyRepository.save(reply);
    }

    @Transactional
    public void deleteReply(Long replyId, Long currentUserId) {
        FeedbackReply reply = feedbackReplyRepository.findById(replyId)
                .orElseThrow(() -> new NotFoundException("Feedback reply not found with ID: " + replyId));

        validateReplyOwnership(reply, currentUserId);
        feedbackReplyRepository.delete(reply);
    }

    @Transactional(readOnly = true)
    public List<FeedbackReply> getRepliesForFeedback(Long feedbackId) {
        Feedback feedback = getFeedbackById(feedbackId);
        return feedbackReplyRepository.findByFeedbackOrderByCreatedAtAsc(feedback);
    }

    @Transactional(readOnly = true)
    public Page<FeedbackReply> getRepliesByAdmin(Long adminUserId, Pageable pageable) {
        User adminUser = userRepository.findById(adminUserId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + adminUserId));

        if (adminUser.getRole() != Role.ADMIN) {
            throw new BusinessRuleException("User is not an admin.");
        }

        return feedbackReplyRepository.findByAdminUser(adminUser, pageable);
    }

    // ==================== STATISTICS AND ANALYTICS ====================

    @Transactional(readOnly = true)
    public Double getAverageRatingByReservation(Long reservationId) {
        return feedbackRepository.getAverageRatingByReservationId(reservationId);
    }

    @Transactional(readOnly = true)
    public Double getAverageRatingByUser(Long userId) {
        return feedbackRepository.getAverageRatingByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Double getOverallAverageRating() {
        return feedbackRepository.getAverageRatingOverall();
    }

    @Transactional(readOnly = true)
    public List<Object[]> getRatingDistribution() {
        return feedbackRepository.getRatingDistribution();
    }

    @Transactional(readOnly = true)
    public long getFeedbackCountByRating(Integer rating) {
        if (rating < 1 || rating > 5) {
            throw new BusinessRuleException("Rating must be between 1 and 5.");
        }
        return feedbackRepository.countByRating(rating);
    }

    @Transactional(readOnly = true)
    public long getFeedbackCountByUser(Long userId) {
        return feedbackRepository.countByUserId(userId);
    }

    @Transactional(readOnly = true)
    public long getFeedbackCountByReservation(Long reservationId) {
        return feedbackRepository.countByReservationId(reservationId);
    }

    @Transactional(readOnly = true)
    public long getReplyCountByFeedback(Long feedbackId) {
        return feedbackReplyRepository.countByFeedbackId(feedbackId);
    }

    // ==================== HELPER METHODS ====================

    private void validateFeedbackCreation(User user, Reservation reservation, Integer rating) {
        // Validate rating
        if (rating == null || rating < 1 || rating > 5) {
            throw new BusinessRuleException("Rating must be between 1 and 5.");
        }

        // Validate user role (only clients should give feedback)
        if (user.getRole() != Role.CLIENT) {
            throw new BusinessRuleException("Only clients can provide feedback.");
        }

        // Validate reservation belongs to user
        if (!Objects.equals(reservation.getClient().getId(), user.getId())) {
            throw new BusinessRuleException("User can only provide feedback for their own reservations.");
        }

        // Validate reservation is completed (check-out date has passed)
        if (reservation.getCheckOut().isAfter(LocalDateTime.now().toLocalDate())) {
            throw new BusinessRuleException("Feedback can only be provided after check-out date.");
        }
    }

    private void validateFeedbackOwnership(Feedback feedback, Long currentUserId) {
        if (!Objects.equals(feedback.getUser().getId(), currentUserId)) {
            throw new BusinessRuleException("Only the feedback author can modify their feedback.");
        }
    }

    private void validateReplyOwnership(FeedbackReply reply, Long currentUserId) {
        if (!Objects.equals(reply.getAdminUser().getId(), currentUserId)) {
            throw new BusinessRuleException("Only the reply author can modify their reply.");
        }
    }
}

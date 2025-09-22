package com.MyBooking.feedback.dto;

import java.util.Map;

/**
 * DTO for feedback statistics
 */
public class FeedbackStatisticsDto {

    private Double overallAverageRating;

    private Long totalFeedbacks;

    private Map<Integer, Long> ratingDistribution;

    private Long feedbacksWithComments;

    private Long feedbacksWithoutComments;

    private Long highRatedFeedbacks;

    private Long lowRatedFeedbacks;

    // Constructors
    public FeedbackStatisticsDto() {}

    public FeedbackStatisticsDto(Double overallAverageRating, Long totalFeedbacks, 
                               Map<Integer, Long> ratingDistribution, Long feedbacksWithComments, 
                               Long feedbacksWithoutComments, Long highRatedFeedbacks, 
                               Long lowRatedFeedbacks) {
        this.overallAverageRating = overallAverageRating;
        this.totalFeedbacks = totalFeedbacks;
        this.ratingDistribution = ratingDistribution;
        this.feedbacksWithComments = feedbacksWithComments;
        this.feedbacksWithoutComments = feedbacksWithoutComments;
        this.highRatedFeedbacks = highRatedFeedbacks;
        this.lowRatedFeedbacks = lowRatedFeedbacks;
    }

    // Getters and Setters
    public Double getOverallAverageRating() {
        return overallAverageRating;
    }

    public void setOverallAverageRating(Double overallAverageRating) {
        this.overallAverageRating = overallAverageRating;
    }

    public Long getTotalFeedbacks() {
        return totalFeedbacks;
    }

    public void setTotalFeedbacks(Long totalFeedbacks) {
        this.totalFeedbacks = totalFeedbacks;
    }

    public Map<Integer, Long> getRatingDistribution() {
        return ratingDistribution;
    }

    public void setRatingDistribution(Map<Integer, Long> ratingDistribution) {
        this.ratingDistribution = ratingDistribution;
    }

    public Long getFeedbacksWithComments() {
        return feedbacksWithComments;
    }

    public void setFeedbacksWithComments(Long feedbacksWithComments) {
        this.feedbacksWithComments = feedbacksWithComments;
    }

    public Long getFeedbacksWithoutComments() {
        return feedbacksWithoutComments;
    }

    public void setFeedbacksWithoutComments(Long feedbacksWithoutComments) {
        this.feedbacksWithoutComments = feedbacksWithoutComments;
    }

    public Long getHighRatedFeedbacks() {
        return highRatedFeedbacks;
    }

    public void setHighRatedFeedbacks(Long highRatedFeedbacks) {
        this.highRatedFeedbacks = highRatedFeedbacks;
    }

    public Long getLowRatedFeedbacks() {
        return lowRatedFeedbacks;
    }

    public void setLowRatedFeedbacks(Long lowRatedFeedbacks) {
        this.lowRatedFeedbacks = lowRatedFeedbacks;
    }

    @Override
    public String toString() {
        return "FeedbackStatisticsDto{" +
                "overallAverageRating=" + overallAverageRating +
                ", totalFeedbacks=" + totalFeedbacks +
                ", ratingDistribution=" + ratingDistribution +
                ", feedbacksWithComments=" + feedbacksWithComments +
                ", feedbacksWithoutComments=" + feedbacksWithoutComments +
                ", highRatedFeedbacks=" + highRatedFeedbacks +
                ", lowRatedFeedbacks=" + lowRatedFeedbacks +
                '}';
    }
}

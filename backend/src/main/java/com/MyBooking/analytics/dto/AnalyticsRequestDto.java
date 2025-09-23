package com.MyBooking.analytics.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class AnalyticsRequestDto {
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    @NotNull(message = "End date is required")
    private LocalDate endDate;
    
    private Integer months; // For revenue trends

    // Constructors
    public AnalyticsRequestDto() {}

    public AnalyticsRequestDto(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public AnalyticsRequestDto(LocalDate startDate, LocalDate endDate, Integer months) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.months = months;
    }

    // Getters and Setters
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getMonths() {
        return months;
    }

    public void setMonths(Integer months) {
        this.months = months;
    }
}

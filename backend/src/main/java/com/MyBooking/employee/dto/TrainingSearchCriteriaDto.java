package com.MyBooking.employee.dto;

import java.time.LocalDate;

public class TrainingSearchCriteriaDto {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;

    // Constructors
    public TrainingSearchCriteriaDto() {}

    public TrainingSearchCriteriaDto(String title, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}

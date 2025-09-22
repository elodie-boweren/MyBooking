package com.MyBooking.employee.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class TrainingCreateRequestDto {
    @NotBlank(message = "Training title is required")
    @Size(max = 200, message = "Training title must not exceed 200 characters")
    private String title;
    
    @NotNull(message = "Training start date is required")
    private LocalDate startDate;
    
    @NotNull(message = "Training end date is required")
    private LocalDate endDate;

    // Constructors
    public TrainingCreateRequestDto() {}

    public TrainingCreateRequestDto(String title, LocalDate startDate, LocalDate endDate) {
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

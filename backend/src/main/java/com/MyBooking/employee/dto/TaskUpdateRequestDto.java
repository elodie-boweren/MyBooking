package com.MyBooking.employee.dto;

import com.MyBooking.employee.domain.TaskStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TaskUpdateRequestDto {
    @NotNull(message = "Task status is required")
    private TaskStatus status;
    
    @Size(max = 500, message = "Note must not exceed 500 characters")
    private String note;
    
    @Size(max = 500, message = "Photo URL must not exceed 500 characters")
    private String photoUrl;

    // Constructors
    public TaskUpdateRequestDto() {}

    public TaskUpdateRequestDto(TaskStatus status, String note, String photoUrl) {
        this.status = status;
        this.note = note;
        this.photoUrl = photoUrl;
    }

    // Getters and Setters
    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
}

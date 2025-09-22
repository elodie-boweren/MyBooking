package com.MyBooking.event.controller;

import com.MyBooking.event.dto.EventCreateRequestDto;
import com.MyBooking.event.dto.EventResponseDto;
import com.MyBooking.event.dto.EventUpdateRequestDto;
import com.MyBooking.event.service.EventService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/events")
public class AdminEventController {
    
    private final EventService eventService;
    
    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }
    
    /**
     * Create new event
     * Access: ADMIN only
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponseDto> createEvent(@Valid @RequestBody EventCreateRequestDto request) {
        EventResponseDto createdEvent = eventService.createEventAsDto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }
    
    /**
     * Update event
     * Access: ADMIN only
     */
    @PutMapping("/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponseDto> updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody EventUpdateRequestDto request) {
        EventResponseDto updatedEvent = eventService.updateEventAsDto(eventId, request);
        return ResponseEntity.ok(updatedEvent);
    }
    
    /**
     * Delete event
     * Access: ADMIN only
     */
    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId, "Deleted by admin");
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Get all events (admin view)
     * Access: ADMIN only
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<EventResponseDto>> getAllEvents(Pageable pageable) {
        Page<EventResponseDto> events = eventService.getAllEventsAsDto(pageable);
        return ResponseEntity.ok(events);
    }
    
    /**
     * Get event details (admin view)
     * Access: ADMIN only
     */
    @GetMapping("/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable Long eventId) {
        EventResponseDto event = eventService.getEventByIdAsDto(eventId);
        return ResponseEntity.ok(event);
    }
    
    /**
     * Get event statistics
     * Access: ADMIN only
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getEventStatistics() {
        // This would return event statistics - for now return a simple response
        return ResponseEntity.ok().body("Event statistics endpoint - to be implemented");
    }
}

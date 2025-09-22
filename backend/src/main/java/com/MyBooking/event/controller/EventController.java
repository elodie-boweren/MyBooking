package com.MyBooking.event.controller;

import com.MyBooking.event.dto.EventResponseDto;
import com.MyBooking.event.dto.EventSearchCriteriaDto;
import com.MyBooking.event.domain.EventType;
import com.MyBooking.event.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class EventController {
    
    private final EventService eventService;
    
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }
    
    /**
     * Get all available events (paginated)
     * Access: CLIENT, ADMIN
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<Page<EventResponseDto>> getAllEvents(Pageable pageable) {
        Page<EventResponseDto> events = eventService.getAllEventsAsDto(pageable);
        return ResponseEntity.ok(events);
    }
    
    /**
     * Get event details by ID
     * Access: CLIENT, ADMIN
     */
    @GetMapping("/{eventId}")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable Long eventId) {
        EventResponseDto event = eventService.getEventByIdAsDto(eventId);
        return ResponseEntity.ok(event);
    }
    
    /**
     * Search events with criteria
     * Access: CLIENT, ADMIN
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<Page<EventResponseDto>> searchEvents(
            @ModelAttribute EventSearchCriteriaDto criteria,
            Pageable pageable) {
        Page<EventResponseDto> events = eventService.searchEventsAsDto(criteria, pageable);
        return ResponseEntity.ok(events);
    }
    
    /**
     * Get upcoming events
     * Access: CLIENT, ADMIN
     */
    @GetMapping("/upcoming")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<Page<EventResponseDto>> getUpcomingEvents(Pageable pageable) {
        Page<EventResponseDto> events = eventService.getUpcomingEventsAsDto(pageable);
        return ResponseEntity.ok(events);
    }
    
    /**
     * Get events by type
     * Access: CLIENT, ADMIN
     */
    @GetMapping("/by-type/{eventType}")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<Page<EventResponseDto>> getEventsByType(
            @PathVariable EventType eventType,
            Pageable pageable) {
        Page<EventResponseDto> events = eventService.getEventsByTypeAsDto(eventType, pageable);
        return ResponseEntity.ok(events);
    }
    
    /**
     * Get events by installation
     * Access: CLIENT, ADMIN
     */
    @GetMapping("/by-installation/{installationId}")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<Page<EventResponseDto>> getEventsByInstallation(
            @PathVariable Long installationId,
            Pageable pageable) {
        Page<EventResponseDto> events = eventService.getEventsByInstallationAsDto(installationId, pageable);
        return ResponseEntity.ok(events);
    }
}

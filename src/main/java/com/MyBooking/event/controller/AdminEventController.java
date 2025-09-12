package com.mybooking.event.controller;

import com.mybooking.event.dto.*;
import com.mybooking.event.service.EventService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/events")
public class AdminEventController {
    private final EventService eventService;
    public AdminEventController(EventService eventService) { this.eventService = eventService; }

    @GetMapping
    public List<EventDto> list() { return eventService.getAllEvents(); }

    @PostMapping
    public EventDto create(@RequestBody CreateOrUpdateEventRequest request) {
        return eventService.createEvent(request);
    }

    @PutMapping("/{id}")
    public EventDto update(@PathVariable Long id, @RequestBody CreateOrUpdateEventRequest request) {
        return eventService.updateEvent(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { eventService.deleteEvent(id); }

    @PutMapping("/{id}/open")
    public void open(@PathVariable Long id) { eventService.openEvent(id); }

    @PutMapping("/{id}/close")
    public void close(@PathVariable Long id) { eventService.closeEvent(id); }
}

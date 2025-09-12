package com.mybooking.event.controller;

import com.mybooking.event.dto.*;
import com.mybooking.event.service.EventBookingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events/{eventId}/bookings")
public class EventBookingController {
    private final EventBookingService bookingService;
    public EventBookingController(EventBookingService bookingService) { this.bookingService = bookingService; }

    @PostMapping
    public BookingDto createBooking(@PathVariable Long eventId, @RequestBody CreateBookingRequest request) {
        return bookingService.createBooking(eventId, request);
    }
}

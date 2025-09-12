package com.mybooking.event.service;

import com.mybooking.event.dto.*;

public interface EventBookingService {
    BookingDto createBooking(Long eventId, CreateBookingRequest request);
}

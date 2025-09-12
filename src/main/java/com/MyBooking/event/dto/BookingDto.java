package com.mybooking.event.dto;

public record BookingDto(Long id, Long eventId, Long userId, int participants,
                         double totalPrice, String status) {}

package com.mybooking.event.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "event_booking")
public class EventBooking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private int participants;
    private double totalPrice;

    @Enumerated(EnumType.STRING)
    private EventBookingStatus status;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}

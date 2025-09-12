package com.mybooking.event.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "event_notification")
public class EventNotification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}

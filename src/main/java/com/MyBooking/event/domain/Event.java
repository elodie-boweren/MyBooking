package com.mybooking.event.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "event")
public class Event {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private BigDecimal price;
    private String currency;

    @Enumerated(EnumType.STRING)
    private EventType type;

    private boolean open;
}

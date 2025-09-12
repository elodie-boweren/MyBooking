package com.mybooking.event.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventDto(Long id, String name, LocalDateTime startAt, LocalDateTime endAt,
                       BigDecimal price, String currency, String type, boolean open) {}

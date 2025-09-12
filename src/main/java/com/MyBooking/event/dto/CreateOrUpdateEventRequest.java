package com.mybooking.event.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateOrUpdateEventRequest(String name, LocalDateTime startAt, LocalDateTime endAt,
                                         BigDecimal price, String currency, String type) {}

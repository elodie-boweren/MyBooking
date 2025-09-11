package com.MyBooking.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.time.OffsetDateTime;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CommonMapper {
    
    // Date/Time mapping
    @Named("localDateTimeToOffsetDateTime")
    default OffsetDateTime localDateTimeToOffsetDateTime(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.atOffset(java.time.ZoneOffset.UTC) : null;
    }
    
    @Named("offsetDateTimeToLocalDateTime")
    default LocalDateTime offsetDateTimeToLocalDateTime(OffsetDateTime offsetDateTime) {
        return offsetDateTime != null ? offsetDateTime.toLocalDateTime() : null;
    }
    
    // Pagination mapping
    @Mapping(target = "content", source = "content")
    @Mapping(target = "totalElements", source = "totalElements")
    @Mapping(target = "totalPages", source = "totalPages")
    @Mapping(target = "size", source = "size")
    @Mapping(target = "number", source = "number")
    <T, R> PageResponse<R> toPageResponse(Page<T> page, List<R> content);

    // Money/Currency mapping
    @Named("formatCurrency")
    default String formatCurrency(java.math.BigDecimal amount, String currency) {
        if (amount == null) return null;
        return String.format("%.2f %s", amount, currency != null ? currency : "EUR");
    }

    // String utilities
    @Named("trimString")
    default String trimString(String value) {
        return value != null ? value.trim() : null;
    }

    @Named("toUpperCase")
    default String toUpperCase(String value) {
        return value != null ? value.toUpperCase() : null;
    }

}
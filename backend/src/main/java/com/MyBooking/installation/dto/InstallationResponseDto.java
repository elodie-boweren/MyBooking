package com.MyBooking.installation.dto;

import com.MyBooking.installation.domain.InstallationType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InstallationResponseDto {
    private Long id;
    private String name;
    private String description;
    private InstallationType installationType;
    private Integer capacity;
    private BigDecimal hourlyRate;
    private String currency;
    private String equipment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public InstallationResponseDto() {}

    public InstallationResponseDto(Long id, String name, String description, InstallationType installationType,
                                  Integer capacity, BigDecimal hourlyRate, String currency, String equipment,
                                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.installationType = installationType;
        this.capacity = capacity;
        this.hourlyRate = hourlyRate;
        this.currency = currency;
        this.equipment = equipment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public InstallationType getInstallationType() { return installationType; }
    public void setInstallationType(InstallationType installationType) { this.installationType = installationType; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public BigDecimal getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = hourlyRate; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getEquipment() { return equipment; }
    public void setEquipment(String equipment) { this.equipment = equipment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

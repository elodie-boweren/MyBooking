package com.MyBooking.loyalty.domain;

import com.MyBooking.reservation.domain.Reservation;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "loyalty_transaction")
public class LoyaltyTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    @NotNull(message = "Loyalty account is required")
    private LoyaltyAccount account;
    
    @NotNull(message = "Transaction type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 10)
    private LoyaltyTransactionType type;
    
    @NotNull(message = "Points amount is required")
    @Min(value = 1, message = "Points must be at least 1")
    @Column(name = "points", nullable = false)
    private Integer points;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // Constructors
    public LoyaltyTransaction() {}
    
    public LoyaltyTransaction(LoyaltyAccount account, LoyaltyTransactionType type, Integer points, Reservation reservation) {
        this.account = account;
        this.type = type;
        this.points = points;
        this.reservation = reservation;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public LoyaltyAccount getAccount() { return account; }
    public void setAccount(LoyaltyAccount account) { this.account = account; }
    
    public LoyaltyTransactionType getType() { return type; }
    public void setType(LoyaltyTransactionType type) { this.type = type; }
    
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
    
    public Reservation getReservation() { return reservation; }
    public void setReservation(Reservation reservation) { this.reservation = reservation; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
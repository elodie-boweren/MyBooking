package com.MyBooking.loyalty.controller;

import com.MyBooking.loyalty.dto.*;
import com.MyBooking.loyalty.service.LoyaltyService;
import com.MyBooking.loyalty.domain.LoyaltyTxType;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Controller for loyalty management (Admin access)
 */
@RestController
@RequestMapping("/api/admin/loyalty")
@PreAuthorize("hasRole('ADMIN')")
public class AdminLoyaltyController {

    private final LoyaltyService loyaltyService;

    @Autowired
    public AdminLoyaltyController(LoyaltyService loyaltyService) {
        this.loyaltyService = loyaltyService;
    }

    // ==================== ADMIN LOYALTY ACCOUNT ENDPOINTS ====================

    /**
     * ADMIN: Get all loyalty accounts
     */
    @GetMapping("/accounts")
    public ResponseEntity<Page<LoyaltyAccountResponseDto>> getAllLoyaltyAccounts(Pageable pageable) {
        Page<LoyaltyAccountResponseDto> accounts = loyaltyService.getAllLoyaltyAccountsAsDto(pageable);
        return ResponseEntity.ok(accounts);
    }

    /**
     * ADMIN: Get loyalty account by ID
     */
    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<LoyaltyAccountResponseDto> getLoyaltyAccountById(@PathVariable Long accountId) {
        LoyaltyAccountResponseDto response = loyaltyService.getLoyaltyAccountByIdAsDto(accountId);
        return ResponseEntity.ok(response);
    }

    /**
     * ADMIN: Get loyalty account by user ID
     */
    @GetMapping("/accounts/user/{userId}")
    public ResponseEntity<LoyaltyAccountResponseDto> getLoyaltyAccountByUserId(@PathVariable Long userId) {
        LoyaltyAccountResponseDto response = loyaltyService.getLoyaltyAccountByUserIdAsDto(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * ADMIN: Delete loyalty account
     */
    @DeleteMapping("/accounts/{accountId}")
    public ResponseEntity<Void> deleteLoyaltyAccount(@PathVariable Long accountId) {
        loyaltyService.deleteLoyaltyAccountAsDto(accountId);
        return ResponseEntity.noContent().build();
    }

    /**
     * ADMIN: Get accounts with high balance
     */
    @GetMapping("/accounts/high-balance")
    public ResponseEntity<Page<LoyaltyAccountResponseDto>> getAccountsWithHighBalance(
            @RequestParam(defaultValue = "1000") Integer threshold,
            Pageable pageable) {
        Page<LoyaltyAccountResponseDto> accounts = loyaltyService.getAccountsWithHighBalanceAsDto(threshold, pageable);
        return ResponseEntity.ok(accounts);
    }

    /**
     * ADMIN: Get accounts with zero balance
     */
    @GetMapping("/accounts/zero-balance")
    public ResponseEntity<Page<LoyaltyAccountResponseDto>> getAccountsWithZeroBalance(Pageable pageable) {
        Page<LoyaltyAccountResponseDto> accounts = loyaltyService.getAccountsWithZeroBalanceAsDto(pageable);
        return ResponseEntity.ok(accounts);
    }

    // ==================== ADMIN LOYALTY TRANSACTION ENDPOINTS ====================

    /**
     * ADMIN: Get all transactions
     */
    @GetMapping("/transactions")
    public ResponseEntity<Page<LoyaltyTransactionResponseDto>> getAllTransactions(Pageable pageable) {
        Page<LoyaltyTransactionResponseDto> transactions = loyaltyService.getAllTransactionsAsDto(pageable);
        return ResponseEntity.ok(transactions);
    }

    /**
     * ADMIN: Get transactions by user ID
     */
    @GetMapping("/transactions/user/{userId}")
    public ResponseEntity<Page<LoyaltyTransactionResponseDto>> getTransactionsByUserId(
            @PathVariable Long userId,
            Pageable pageable) {
        Page<LoyaltyTransactionResponseDto> transactions = loyaltyService.getTransactionsByUserIdAsDto(userId, pageable);
        return ResponseEntity.ok(transactions);
    }

    /**
     * ADMIN: Get high-value transactions
     */
    @GetMapping("/transactions/high-value")
    public ResponseEntity<Page<LoyaltyTransactionResponseDto>> getHighValueTransactions(
            @RequestParam(defaultValue = "1000") Integer threshold,
            Pageable pageable) {
        Page<LoyaltyTransactionResponseDto> transactions = loyaltyService.getHighValueTransactionsAsDto(threshold, pageable);
        return ResponseEntity.ok(transactions);
    }

    /**
     * ADMIN: Get transactions by type
     */
    @GetMapping("/transactions/type/{type}")
    public ResponseEntity<Page<LoyaltyTransactionResponseDto>> getTransactionsByType(
            @PathVariable LoyaltyTxType type,
            Pageable pageable) {
        Page<LoyaltyTransactionResponseDto> transactions = loyaltyService.getTransactionsByTypeAsDto(type, pageable);
        return ResponseEntity.ok(transactions);
    }

    /**
     * ADMIN: Earn points for a user
     */
    @PostMapping("/earn-points")
    public ResponseEntity<LoyaltyTransactionResponseDto> earnPoints(
            @Valid @RequestBody EarnPointsRequestDto request) {
        LoyaltyTransactionResponseDto response = loyaltyService.earnPointsAsDto(
                request.getUserId(), request.getAmount(), request.getReason(), request.getReservationId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * ADMIN: Redeem points for a user
     */
    @PostMapping("/redeem-points")
    public ResponseEntity<LoyaltyTransactionResponseDto> redeemPoints(
            @Valid @RequestBody RedeemPointsRequestDto request) {
        LoyaltyTransactionResponseDto response = loyaltyService.redeemPointsAsDto(
                request.getUserId(), request.getPoints(), request.getReason());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ==================== ADMIN STATISTICS ENDPOINTS ====================

    /**
     * ADMIN: Get loyalty program statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<LoyaltyStatisticsDto> getLoyaltyStatistics() {
        LoyaltyStatisticsDto statistics = loyaltyService.getLoyaltyStatisticsAsDto();
        return ResponseEntity.ok(statistics);
    }

    /**
     * ADMIN: Calculate points for an amount
     */
    @GetMapping("/calculate-points")
    public ResponseEntity<PointsCalculationDto> calculatePoints(
            @RequestParam BigDecimal amount) {
        PointsCalculationDto response = loyaltyService.calculatePointsAsDto(amount);
        return ResponseEntity.ok(response);
    }
}

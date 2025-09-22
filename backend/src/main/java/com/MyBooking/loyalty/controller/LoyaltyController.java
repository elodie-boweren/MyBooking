package com.MyBooking.loyalty.controller;

import com.MyBooking.loyalty.dto.*;
import com.MyBooking.loyalty.service.LoyaltyService;
import com.MyBooking.common.security.JwtService;
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
 * Controller for loyalty management (Client access)
 */
@RestController
@RequestMapping("/api/loyalty")
public class LoyaltyController {

    private final LoyaltyService loyaltyService;
    private final JwtService jwtService;

    @Autowired
    public LoyaltyController(LoyaltyService loyaltyService, JwtService jwtService) {
        this.loyaltyService = loyaltyService;
        this.jwtService = jwtService;
    }

    // ==================== CLIENT LOYALTY ENDPOINTS ====================

    /**
     * CLIENT: Get my loyalty account
     * Requires CLIENT role.
     */
    @GetMapping("/account")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<LoyaltyAccountResponseDto> getMyLoyaltyAccount(
            @RequestHeader("Authorization") String authorizationHeader) {
        String userEmail = jwtService.extractUsername(authorizationHeader.substring(7));
        Long userId = loyaltyService.getUserIdByEmail(userEmail);
        LoyaltyAccountResponseDto response = loyaltyService.getLoyaltyAccountByUserIdAsDto(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * CLIENT: Create my loyalty account
     * Requires CLIENT role.
     */
    @PostMapping("/account")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<LoyaltyAccountResponseDto> createMyLoyaltyAccount(
            @RequestHeader("Authorization") String authorizationHeader) {
        String userEmail = jwtService.extractUsername(authorizationHeader.substring(7));
        Long userId = loyaltyService.getUserIdByEmail(userEmail);
        LoyaltyAccountResponseDto response = loyaltyService.createLoyaltyAccountAsDto(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * CLIENT: Get my transaction history
     * Requires CLIENT role.
     */
    @GetMapping("/transactions")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Page<LoyaltyTransactionResponseDto>> getMyTransactions(
            @RequestHeader("Authorization") String authorizationHeader,
            Pageable pageable) {
        String userEmail = jwtService.extractUsername(authorizationHeader.substring(7));
        Long userId = loyaltyService.getUserIdByEmail(userEmail);
        Page<LoyaltyTransactionResponseDto> transactions = loyaltyService.getTransactionsByUserIdAsDto(userId, pageable);
        return ResponseEntity.ok(transactions);
    }

    /**
     * CLIENT: Redeem points
     * Requires CLIENT role.
     */
    @PostMapping("/redeem")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<LoyaltyTransactionResponseDto> redeemPoints(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody RedeemPointsRequestDto request) {
        String userEmail = jwtService.extractUsername(authorizationHeader.substring(7));
        Long userId = loyaltyService.getUserIdByEmail(userEmail);
        // For client operations, ignore the userId in the request and use the authenticated user's ID
        LoyaltyTransactionResponseDto response = loyaltyService.redeemPointsAsDto(
                userId, request.getPoints(), request.getReason());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * CLIENT: Calculate points for an amount
     * Requires CLIENT role.
     */
    @GetMapping("/calculate-points")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<PointsCalculationDto> calculatePoints(
            @RequestParam BigDecimal amount) {
        PointsCalculationDto response = loyaltyService.calculatePointsAsDto(amount);
        return ResponseEntity.ok(response);
    }

    /**
     * ALL ROLES: Get loyalty account by ID
     */
    @GetMapping("/account/{accountId}")
    @PreAuthorize("hasAnyRole('CLIENT', 'EMPLOYEE', 'ADMIN')")
    public ResponseEntity<LoyaltyAccountResponseDto> getLoyaltyAccountById(@PathVariable Long accountId) {
        LoyaltyAccountResponseDto response = loyaltyService.getLoyaltyAccountByIdAsDto(accountId);
        return ResponseEntity.ok(response);
    }

    /**
     * ALL ROLES: Get loyalty account by user ID
     */
    @GetMapping("/account/user/{userId}")
    @PreAuthorize("hasAnyRole('CLIENT', 'EMPLOYEE', 'ADMIN')")
    public ResponseEntity<LoyaltyAccountResponseDto> getLoyaltyAccountByUserId(@PathVariable Long userId) {
        LoyaltyAccountResponseDto response = loyaltyService.getLoyaltyAccountByUserIdAsDto(userId);
        return ResponseEntity.ok(response);
    }
}

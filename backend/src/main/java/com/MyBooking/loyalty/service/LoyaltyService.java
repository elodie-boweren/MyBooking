//Loyalty Account Management: Create, update, get account by user
//Points Operations: Earn points (from reservations), redeem points (for rewards)
//Transaction History: Track all point transactions with audit trail
//Business Rules: Point calculation, redemption limits, expiration policies

package com.MyBooking.loyalty.service;

import com.MyBooking.auth.domain.User;
import com.MyBooking.auth.repository.UserRepository;
import com.MyBooking.common.exception.BusinessRuleException;
import com.MyBooking.common.exception.NotFoundException;
import com.MyBooking.loyalty.domain.LoyaltyAccount;
import com.MyBooking.loyalty.domain.LoyaltyTransaction;
import com.MyBooking.loyalty.domain.LoyaltyTxType;
import com.MyBooking.loyalty.repository.LoyaltyAccountRepository;
import com.MyBooking.loyalty.repository.LoyaltyTransactionRepository;
import com.MyBooking.reservation.domain.Reservation;
import com.MyBooking.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LoyaltyService {

    @Autowired
    private LoyaltyAccountRepository loyaltyAccountRepository;

    @Autowired
    private LoyaltyTransactionRepository loyaltyTransactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    // ========== CORE ACCOUNT MANAGEMENT ==========

    /**
     * Create a new loyalty account for a user
     */
    public LoyaltyAccount createLoyaltyAccount(Long userId) {
        // Validate user exists
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));

        // Check if account already exists
        if (loyaltyAccountRepository.existsByUserId(userId)) {
            throw new BusinessRuleException("Loyalty account already exists for user ID: " + userId);
        }

        LoyaltyAccount account = new LoyaltyAccount(user);
        return loyaltyAccountRepository.save(account);
    }

    /**
     * Get loyalty account by user ID
     */
    @Transactional(readOnly = true)
    public LoyaltyAccount getLoyaltyAccountByUserId(Long userId) {
        return loyaltyAccountRepository.findByUserId(userId)
            .orElseThrow(() -> new NotFoundException("Loyalty account not found for user ID: " + userId));
    }

    /**
     * Get loyalty account by user ID (optional)
     */
    @Transactional(readOnly = true)
    public Optional<LoyaltyAccount> getLoyaltyAccountByUserIdOptional(Long userId) {
        return loyaltyAccountRepository.findByUserId(userId);
    }

    /**
     * Update loyalty account
     */
    public LoyaltyAccount updateLoyaltyAccount(LoyaltyAccount account) {
        if (account.getId() == null) {
            throw new BusinessRuleException("Account ID is required for update");
        }

        if (!loyaltyAccountRepository.existsById(account.getId())) {
            throw new NotFoundException("Loyalty account not found with ID: " + account.getId());
        }

        return loyaltyAccountRepository.save(account);
    }

    /**
     * Delete loyalty account
     */
    public void deleteLoyaltyAccount(Long accountId) {
        LoyaltyAccount account = loyaltyAccountRepository.findById(accountId)
            .orElseThrow(() -> new NotFoundException("Loyalty account not found with ID: " + accountId));

        // Check if account has transactions
        if (loyaltyTransactionRepository.existsByAccountId(accountId)) {
            throw new BusinessRuleException("Cannot delete loyalty account with existing transactions");
        }

        loyaltyAccountRepository.delete(account);
    }

    // ========== POINTS OPERATIONS ==========

    /**
     * Earn points for a user (typically from reservation)
     */
    public LoyaltyTransaction earnPoints(Long userId, BigDecimal amount, String reason) {
        return earnPoints(userId, amount, reason, null);
    }

    /**
     * Earn points for a user with reservation reference
     */
    public LoyaltyTransaction earnPoints(Long userId, BigDecimal amount, String reason, Long reservationId) {
        // Validate inputs
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("Amount must be positive");
        }

        // Get or create loyalty account
        LoyaltyAccount account = getOrCreateLoyaltyAccount(userId);

        // Calculate points (1 point per $1 spent)
        Integer points = calculatePointsFromAmount(amount);

        // Create transaction
        LoyaltyTransaction transaction = new LoyaltyTransaction(account, LoyaltyTxType.EARN, points);
        transaction.setReservation(reservationId != null ? 
            reservationRepository.findById(reservationId).orElse(null) : null);

        // Save transaction
        LoyaltyTransaction savedTransaction = loyaltyTransactionRepository.save(transaction);

        // Update account balance
        account.setBalance(account.getBalance() + points);
        loyaltyAccountRepository.save(account);

        return savedTransaction;
    }

    /**
     * Redeem points for a user
     */
    public LoyaltyTransaction redeemPoints(Long userId, Integer points, String reason) {
        // Validate inputs
        if (points == null || points <= 0) {
            throw new BusinessRuleException("Points must be positive");
        }

        // Get loyalty account
        LoyaltyAccount account = getLoyaltyAccountByUserId(userId);

        // Validate redemption
        validateRedemptionRequest(account, points);

        // Create transaction
        LoyaltyTransaction transaction = new LoyaltyTransaction(account, LoyaltyTxType.REDEEM, points);
        LoyaltyTransaction savedTransaction = loyaltyTransactionRepository.save(transaction);

        // Update account balance
        account.setBalance(account.getBalance() - points);
        loyaltyAccountRepository.save(account);

        return savedTransaction;
    }

    /**
     * Get available points for a user
     */
    @Transactional(readOnly = true)
    public Integer getAvailablePoints(Long userId) {
        LoyaltyAccount account = getLoyaltyAccountByUserId(userId);
        return account.getBalance();
    }

    // ========== TRANSACTION HISTORY ==========

    /**
     * Get transaction history for a user
     */
    @Transactional(readOnly = true)
    public Page<LoyaltyTransaction> getTransactionHistory(Long userId, Pageable pageable) {
        LoyaltyAccount account = getLoyaltyAccountByUserId(userId);
        return loyaltyTransactionRepository.findByAccountOrderByCreatedAtDesc(account, pageable);
    }

    /**
     * Get transactions by type for a user
     */
    @Transactional(readOnly = true)
    public Page<LoyaltyTransaction> getTransactionsByType(Long userId, LoyaltyTxType type, Pageable pageable) {
        LoyaltyAccount account = getLoyaltyAccountByUserId(userId);
        return loyaltyTransactionRepository.findByAccountAndType(account, type, pageable);
    }

    /**
     * Get recent transactions for a user
     */
    @Transactional(readOnly = true)
    public List<LoyaltyTransaction> getRecentTransactions(Long userId, int days) {
        LoyaltyAccount account = getLoyaltyAccountByUserId(userId);
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return loyaltyTransactionRepository.findByAccountIdOrderByCreatedAtDesc(account.getId())
            .stream()
            .filter(transaction -> transaction.getCreatedAt().isAfter(since))
            .toList();
    }

    // ========== BUSINESS OPERATIONS ==========

    /**
     * Calculate points from monetary amount (1 point per $1)
     */
    @Transactional(readOnly = true)
    public Integer calculatePointsFromAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }
        return amount.setScale(0, RoundingMode.DOWN).intValue();
    }

    /**
     * Calculate discount amount from points (1 point = $0.01)
     */
    @Transactional(readOnly = true)
    public BigDecimal calculatePointsDiscount(Long userId, Integer points) {
        if (points == null || points <= 0) {
            return BigDecimal.ZERO;
        }
        
        // Validate user has enough points
        validateRedemptionRequest(userId, points);
        
        // Convert points to dollar amount (1 point = $0.01)
        return BigDecimal.valueOf(points).multiply(new BigDecimal("0.01"));
    }

    /**
     * Calculate discount amount from points without validation (for price calculation)
     */
    @Transactional(readOnly = true)
    public BigDecimal calculatePointsDiscountAmount(Integer points) {
        if (points == null || points <= 0) {
            return BigDecimal.ZERO;
        }
        
        // Convert points to dollar amount (1 point = $0.01)
        return BigDecimal.valueOf(points).multiply(new BigDecimal("0.01"));
    }

    /**
     * Calculate maximum points that can be redeemed for a given amount
     */
    @Transactional(readOnly = true)
    public Integer calculateMaxRedeemablePoints(Long userId, BigDecimal maxAmount) {
        if (maxAmount == null || maxAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }
        
        Integer availablePoints = getAvailablePoints(userId);
        Integer maxPointsByAmount = maxAmount.multiply(new BigDecimal("100")).intValue(); // $1 = 100 points
        Integer maxPointsByBusinessRule = 10000; // Business rule: max 10,000 points per transaction
        
        return Math.min(Math.min(availablePoints, maxPointsByAmount), maxPointsByBusinessRule);
    }

    /**
     * Validate points redemption for reservation (with reservation-specific rules)
     */
    @Transactional(readOnly = true)
    public void validateReservationPointsRedemption(Long userId, Integer points, BigDecimal reservationTotal) {
        if (points == null || points <= 0) {
            throw new BusinessRuleException("Points must be positive");
        }

        LoyaltyAccount account = getLoyaltyAccountByUserId(userId);
        
        // Check if user has enough points
        if (account.getBalance() < points) {
            throw new BusinessRuleException("Insufficient points. Available: " + 
                account.getBalance() + ", Requested: " + points);
        }

        // Business rule: minimum redemption of 100 points
        if (points < 100) {
            throw new BusinessRuleException("Minimum redemption is 100 points");
        }

        // Business rule: maximum redemption of 10,000 points per transaction
        if (points > 10000) {
            throw new BusinessRuleException("Maximum redemption is 10,000 points per transaction");
        }

        // Business rule: points discount cannot exceed reservation total
        BigDecimal pointsDiscount = calculatePointsDiscountAmount(points);
        if (pointsDiscount.compareTo(reservationTotal) > 0) {
            throw new BusinessRuleException("Points discount (" + pointsDiscount + 
                ") cannot exceed reservation total (" + reservationTotal + ")");
        }
    }

    /**
     * Validate redemption request
     */
    @Transactional(readOnly = true)
    public void validateRedemptionRequest(Long userId, Integer points) {
        LoyaltyAccount account = getLoyaltyAccountByUserId(userId);
        validateRedemptionRequest(account, points);
    }

    /**
     * Validate redemption request (private helper)
     */
    private void validateRedemptionRequest(LoyaltyAccount account, Integer points) {
        if (points <= 0) {
            throw new BusinessRuleException("Points must be positive");
        }

        if (account.getBalance() < points) {
            throw new BusinessRuleException("Insufficient points. Available: " + 
                account.getBalance() + ", Requested: " + points);
        }

        // Business rule: minimum redemption of 100 points
        if (points < 100) {
            throw new BusinessRuleException("Minimum redemption is 100 points");
        }

        // Business rule: maximum redemption of 10,000 points per transaction
        if (points > 10000) {
            throw new BusinessRuleException("Maximum redemption is 10,000 points per transaction");
        }
    }

    /**
     * Get or create loyalty account for user
     */
    private LoyaltyAccount getOrCreateLoyaltyAccount(Long userId) {
        return loyaltyAccountRepository.findByUserId(userId)
            .orElseGet(() -> createLoyaltyAccount(userId));
    }

    // ========== ADMIN OPERATIONS ==========

    /**
     * Get all loyalty accounts with pagination
     */
    @Transactional(readOnly = true)
    public Page<LoyaltyAccount> getAllLoyaltyAccounts(Pageable pageable) {
        return loyaltyAccountRepository.findAll(pageable);
    }

    /**
     * Get accounts with high balance
     */
    @Transactional(readOnly = true)
    public Page<LoyaltyAccount> getAccountsWithHighBalance(Integer threshold, Pageable pageable) {
        return loyaltyAccountRepository.findAccountsWithHighBalance(threshold, pageable);
    }

    /**
     * Get accounts with zero balance
     */
    @Transactional(readOnly = true)
    public Page<LoyaltyAccount> getAccountsWithZeroBalance(Pageable pageable) {
        return loyaltyAccountRepository.findAccountsWithZeroBalance(pageable);
    }

    /**
     * Get all transactions with pagination
     */
    @Transactional(readOnly = true)
    public Page<LoyaltyTransaction> getAllTransactions(Pageable pageable) {
        return loyaltyTransactionRepository.findAll(pageable);
    }

    /**
     * Get high-value transactions
     */
    @Transactional(readOnly = true)
    public Page<LoyaltyTransaction> getHighValueTransactions(Integer threshold, Pageable pageable) {
        return loyaltyTransactionRepository.findHighValueTransactions(threshold, pageable);
    }

    /**
     * Get transactions by type
     */
    @Transactional(readOnly = true)
    public Page<LoyaltyTransaction> getTransactionsByType(LoyaltyTxType type, Pageable pageable) {
        return loyaltyTransactionRepository.findByTypeOrderByCreatedAtDesc(type, pageable);
    }

    // ========== STATISTICS & ANALYTICS ==========

    /**
     * Get loyalty program statistics
     */
    @Transactional(readOnly = true)
    public LoyaltyStatistics getLoyaltyStatistics() {
        long totalAccounts = loyaltyAccountRepository.count();
        long totalTransactions = loyaltyTransactionRepository.count();
        
        Long totalBalance = loyaltyAccountRepository.getTotalBalance();
        Double averageBalance = loyaltyAccountRepository.getAverageBalance();
        
        Long totalEarnedPoints = loyaltyTransactionRepository.getTotalPointsByType(LoyaltyTxType.EARN);
        Long totalRedeemedPoints = loyaltyTransactionRepository.getTotalPointsByType(LoyaltyTxType.REDEEM);
        
        return new LoyaltyStatistics(
            totalAccounts,
            totalTransactions,
            totalBalance != null ? totalBalance : 0L,
            averageBalance != null ? averageBalance : 0.0,
            totalEarnedPoints != null ? totalEarnedPoints : 0L,
            totalRedeemedPoints != null ? totalRedeemedPoints : 0L
        );
    }

    /**
     * Get balance statistics by user role
     */
    @Transactional(readOnly = true)
    public List<Object[]> getBalanceStatisticsByUserRole() {
        return loyaltyAccountRepository.getBalanceStatisticsByUserRole();
    }

    /**
     * Get points statistics by transaction type
     */
    @Transactional(readOnly = true)
    public List<Object[]> getPointsStatisticsByType() {
        return loyaltyTransactionRepository.getPointsStatisticsByType();
    }

    // ========== INTEGRATION METHODS ==========

    /**
     * Process points for a confirmed reservation
     */
    public LoyaltyTransaction processReservationPoints(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new NotFoundException("Reservation not found with ID: " + reservationId));

        if (reservation.getClient() == null) {
            throw new BusinessRuleException("Reservation has no associated client");
        }

        return earnPoints(
            reservation.getClient().getId(),
            reservation.getTotalPrice(),
            "Points earned from reservation #" + reservationId,
            reservationId
        );
    }

    /**
     * Refund points for a cancelled reservation
     */
    public LoyaltyTransaction refundReservationPoints(Long reservationId, Integer pointsUsed) {
        if (pointsUsed == null || pointsUsed <= 0) {
            return null; // No points to refund
        }

        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new NotFoundException("Reservation not found with ID: " + reservationId));

        if (reservation.getClient() == null) {
            throw new BusinessRuleException("Reservation has no associated client");
        }

        // Convert points back to monetary amount for earning
        BigDecimal refundAmount = calculatePointsDiscountAmount(pointsUsed);

        return earnPoints(
            reservation.getClient().getId(),
            refundAmount,
            "Points refunded for cancelled reservation #" + reservationId,
            reservationId
        );
    }

    // ========== INNER CLASSES ==========

    /**
     * Loyalty program statistics data class
     */
    public static class LoyaltyStatistics {
        private final long totalAccounts;
        private final long totalTransactions;
        private final long totalBalance;
        private final double averageBalance;
        private final long totalEarnedPoints;
        private final long totalRedeemedPoints;

        public LoyaltyStatistics(long totalAccounts, long totalTransactions, long totalBalance,
                               double averageBalance, long totalEarnedPoints, long totalRedeemedPoints) {
            this.totalAccounts = totalAccounts;
            this.totalTransactions = totalTransactions;
            this.totalBalance = totalBalance;
            this.averageBalance = averageBalance;
            this.totalEarnedPoints = totalEarnedPoints;
            this.totalRedeemedPoints = totalRedeemedPoints;
        }

        // Getters
        public long getTotalAccounts() { return totalAccounts; }
        public long getTotalTransactions() { return totalTransactions; }
        public long getTotalBalance() { return totalBalance; }
        public double getAverageBalance() { return averageBalance; }
        public long getTotalEarnedPoints() { return totalEarnedPoints; }
        public long getTotalRedeemedPoints() { return totalRedeemedPoints; }
    }
}
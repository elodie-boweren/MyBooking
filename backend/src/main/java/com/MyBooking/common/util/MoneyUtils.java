package com.MyBooking.common.util;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;


@Component
public class MoneyUtils {
    
    public BigDecimal calculateTotal(BigDecimal price, int quantity) {
        return price != null ? price.multiply(BigDecimal.valueOf(quantity)) : BigDecimal.ZERO;
    }
    
    public BigDecimal applyDiscount(BigDecimal amount, BigDecimal discountPercentage) {
        if (amount == null || discountPercentage == null) return amount;
        BigDecimal discount = amount.multiply(discountPercentage.divide(BigDecimal.valueOf(100)));
        return amount.subtract(discount);
    }
    
    public String formatCurrency(BigDecimal amount, String currency) {
        if (amount == null) return "0.00 " + currency;
        return String.format("%.2f %s", amount, currency != null ? currency : "EUR");
    }
}
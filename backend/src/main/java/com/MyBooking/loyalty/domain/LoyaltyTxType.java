package com.MyBooking.loyalty.domain;

public enum LoyaltyTxType {
    EARN("Earn points"),
    REDEEM("Redeem points");
    
    private final String description;
    
    LoyaltyTxType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
package com.mybooking.auth.service;

public interface TokenService {
    String generateToken(Long userId);
    boolean validateToken(String token);
    Long getUserIdFromToken(String token);
}

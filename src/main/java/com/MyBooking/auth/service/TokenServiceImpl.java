package com.mybooking.auth.service;

import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Override
    public String generateToken(Long userId) { return "dummy-token"; }

    @Override
    public boolean validateToken(String token) { return true; }

    @Override
    public Long getUserIdFromToken(String token) { return 1L; }
}

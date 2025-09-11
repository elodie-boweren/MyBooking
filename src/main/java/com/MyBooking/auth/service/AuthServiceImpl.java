package com.mybooking.auth.service;

import com.mybooking.auth.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Override
    public ProfileDto getProfile() { return new ProfileDto(); }

    @Override
    public Object register(RegisterRequest request) { return null; }

    @Override
    public Object login(LoginRequest request) { return null; }

    @Override
    public void logout() {}
}

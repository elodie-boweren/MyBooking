package com.mybooking.auth.service;

import com.mybooking.auth.dto.*;

public interface AuthService {
    ProfileDto getProfile();
    Object register(RegisterRequest request);
    Object login(LoginRequest request);
    void logout();
}

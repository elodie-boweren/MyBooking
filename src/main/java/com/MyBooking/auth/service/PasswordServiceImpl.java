package com.mybooking.auth.service;

import com.mybooking.auth.dto.*;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {}

    @Override
    public void resetPassword(ResetPasswordRequest request) {}
}

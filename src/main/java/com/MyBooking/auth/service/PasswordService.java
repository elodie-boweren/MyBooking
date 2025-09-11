package com.mybooking.auth.service;

import com.mybooking.auth.dto.*;

public interface PasswordService {
    void forgotPassword(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);
}

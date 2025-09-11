package com.MyBooking.common.util;

import org.springframework.stereotype.Component;

@Component
public class ValidationUtils {
    
    public boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    public boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.matches("^\\+?[1-9]\\d{1,14}$");
    }
    
    public boolean isStrongPassword(String password) {
        return password != null && password.length() >= 8 
            && password.matches(".*[A-Z].*") 
            && password.matches(".*[a-z].*") 
            && password.matches(".*\\d.*");
    }
}
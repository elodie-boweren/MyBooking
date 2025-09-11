//package com.mybooking.auth.dto;
//
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//
//public class ForgotPasswordRequest {
//    @NotBlank @Email
//    private String email;
//
//    // getters / setters
//}

package com.mybooking.auth.dto;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class ForgotPasswordRequest {
    @Email
    @NotBlank
    private String email;
}

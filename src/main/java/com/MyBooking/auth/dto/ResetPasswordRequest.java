//package com.mybooking.auth.dto;
//
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Size;
//
//public class ResetPasswordRequest {
//    @NotBlank
//    private String token;
//
//    @NotBlank @Size(min = 8)
//    private String newPassword;
//
//    // getters / setters
//}

package com.mybooking.auth.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class ResetPasswordRequest {
    @NotBlank
    private String token;

    @NotBlank
    private String newPassword;
}

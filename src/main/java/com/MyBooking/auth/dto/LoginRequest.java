//package com.mybooking.auth.dto;
//
//import jakarta.validation.constraints.*;
//
//public class LoginRequest {
//    @NotBlank @Email
//    private String email;
//
//    @NotBlank
//    private String password;
//
//    // getters / setters
//}
package com.mybooking.auth.dto;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}

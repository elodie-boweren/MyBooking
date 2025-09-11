//package com.mybooking.auth.dto;
//
//import jakarta.validation.constraints.*;
//import java.time.LocalDate;
//
//public class RegisterRequest {
//    @NotBlank @Size(max = 100)
//    private String firstName;
//
//    @NotBlank @Size(max = 100)
//    private String lastName;
//
//    @NotBlank @Email @Size(max = 255)
//    private String email;
//
//    @NotBlank @Size(min = 8, max = 128)
//    private String password;
//
//    @NotNull
//    private LocalDate birthdate;
//
//    @Size(max = 50)
//    private String phone;
//
//    @Size(max = 255)
//    private String address;
//
//    // getters / setters
//}

package com.mybooking.auth.dto;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class RegisterRequest {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}

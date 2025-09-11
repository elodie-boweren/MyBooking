//package com.mybooking.auth.dto;
//
//import java.time.LocalDate;
//import java.util.Set;
//
//public class ProfileDto {
//    private Long id;
//    private String email;
//    private String firstName;
//    private String lastName;
//    private LocalDate birthdate;
//    private String phone;
//    private String address;
//    private Set<String> roles;
//
//    // getters / setters
//}

package com.mybooking.auth.dto;

import lombok.Data;

@Data
public class ProfileDto {
    private String firstName;
    private String lastName;
    private String email;
}

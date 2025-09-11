//package com.mybooking.auth.controller;
//
//import com.mybooking.auth.dto.NotificationPreferencesDto;
//import com.mybooking.auth.service.NotificationPreferenceService;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/v1/client/notifications")
//public class ProfileController {
//
//    private final NotificationPreferenceService prefService;
//
//    public ProfileController(NotificationPreferenceService prefService) {
//        this.prefService = prefService;
//    }
//
//    @GetMapping("/preferences")
//    public NotificationPreferencesDto getPreferences(@AuthenticationPrincipal UserDetails principal) {
//        return prefService.getPreferences(principal.getUsername());
//    }
//
//    @PutMapping("/preferences")
//    public NotificationPreferencesDto updatePreferences(
//            @AuthenticationPrincipal UserDetails principal,
//            @RequestBody NotificationPreferencesDto dto) {
//        return prefService.updatePreferences(principal.getUsername(), dto);
//    }
//}

package com.mybooking.auth.controller;

import com.mybooking.auth.domain.NotificationPreference;
import com.mybooking.auth.domain.AppUser;
import com.mybooking.auth.dto.NotificationPreferencesDto;
import com.mybooking.auth.dto.ProfileDto;
import com.mybooking.auth.service.AppUserService;
import com.mybooking.auth.service.NotificationPreferenceService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final AppUserService appUserService;
    private final NotificationPreferenceService notificationPreferenceService;

    public ProfileController(AppUserService appUserService,
                             NotificationPreferenceService notificationPreferenceService) {
        this.appUserService = appUserService;
        this.notificationPreferenceService = notificationPreferenceService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable("userId") Long userId) {
        AppUser user = appUserService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        ProfileDto profileDto = ProfileDto.fromEntity(user);
        return ResponseEntity.ok(profileDto);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ProfileDto> updateProfile(@PathVariable("userId") Long userId,
                                                    @Valid @RequestBody ProfileDto profileDto) {
        AppUser updatedUser = appUserService.updateUser(userId, profileDto);
        return ResponseEntity.ok(ProfileDto.fromEntity(updatedUser));
    }

    @GetMapping("/{userId}/notifications")
    public ResponseEntity<NotificationPreferencesDto> getNotificationPreferences(@PathVariable("userId") Long userId) {
        NotificationPreference preferences = notificationPreferenceService.getPreferences(userId);
        if (preferences == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(NotificationPreferencesDto.fromEntity(preferences));
    }

    @PutMapping("/{userId}/notifications")
    public ResponseEntity<NotificationPreferencesDto> updateNotificationPreferences(@PathVariable("userId") Long userId,
                                                                                    @Valid @RequestBody NotificationPreferencesDto dto) {
        NotificationPreference updated = notificationPreferenceService.updatePreferences(userId, dto);
        return ResponseEntity.ok(NotificationPreferencesDto.fromEntity(updated));
    }
}

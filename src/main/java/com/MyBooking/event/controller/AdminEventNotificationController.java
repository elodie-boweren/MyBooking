package com.mybooking.event.controller;

import com.mybooking.event.dto.NotificationRequest;
import com.mybooking.event.service.EventNotificationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/events/{eventId}/notifications")
public class AdminEventNotificationController {
    private final EventNotificationService notificationService;
    public AdminEventNotificationController(EventNotificationService notificationService) { this.notificationService = notificationService; }

    @PostMapping
    public void sendNotification(@PathVariable Long eventId, @RequestBody NotificationRequest request) {
        notificationService.sendNotification(eventId, request);
    }
}

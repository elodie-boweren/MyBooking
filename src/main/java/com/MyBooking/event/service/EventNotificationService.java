package com.mybooking.event.service;

import com.mybooking.event.dto.NotificationRequest;

public interface EventNotificationService {
    void sendNotification(Long eventId, NotificationRequest request);
}

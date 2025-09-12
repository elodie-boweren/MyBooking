package com.mybooking.event.repository;

import com.mybooking.event.domain.EventNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventNotificationRepository extends JpaRepository<EventNotification, Long> {}

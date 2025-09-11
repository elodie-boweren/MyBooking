package com.mybooking.auth.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notification_preference")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationPreference {

    @Id
    private Long userId;

    private boolean emailEnabled = true;
    private boolean smsEnabled = false;
}

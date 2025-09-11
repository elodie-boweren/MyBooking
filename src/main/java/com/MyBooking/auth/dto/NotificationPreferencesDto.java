//package com.mybooking.auth.dto;
//
//public class NotificationPreferencesDto {
//    private boolean emailEnabled;
//    private boolean smsEnabled;
//
//    // getters / setters
//}

package com.mybooking.auth.dto;

import lombok.Data;

@Data
public class NotificationPreferencesDto {
    private boolean emailEnabled;
    private boolean smsEnabled;
}

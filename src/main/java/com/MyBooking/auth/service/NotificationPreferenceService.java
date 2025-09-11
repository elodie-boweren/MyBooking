//package com.mybooking.auth.service;
//
//import com.mybooking.auth.domain.NotificationPreference;
//import com.mybooking.auth.dto.NotificationPreferencesDto;
//import com.mybooking.auth.repository.NotificationPreferenceRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import jakarta.transaction.Transactional;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class NotificationPreferenceService {
//
//    private final NotificationPreferenceRepository repository;
//
//    public NotificationPreferencesDto getPreferences(Long userId) {
//        Optional<NotificationPreference> np = repository.findById(userId);
//        if (np.isPresent()) {
//            NotificationPreference n = np.get();
//            NotificationPreferencesDto dto = new NotificationPreferencesDto();
//            dto.setEmailEnabled(n.isEmailEnabled());
//            dto.setSmsEnabled(n.isSmsEnabled());
//            return dto;
//        }
//        return new NotificationPreferencesDto(); // valeurs par défaut
//    }
//
//    public NotificationPreferencesDto updatePreferences(Long userId, NotificationPreferencesDto dto) {
//        NotificationPreference n = repository.findById(userId).orElseGet(() -> {
//            NotificationPreference newPref = new NotificationPreference();
//            newPref.setUserId(userId);
//            return newPref;
//        });
//
//        n.setEmailEnabled(dto.isEmailEnabled());
//        n.setSmsEnabled(dto.isSmsEnabled());
//        repository.save(n);
//
//        return dto;
//    }
//}

package com.mybooking.auth.service;

import com.mybooking.auth.domain.NotificationPreference;
import com.mybooking.auth.dto.NotificationPreferencesDto;
import com.mybooking.auth.repository.NotificationPreferenceRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotificationPreferenceService {

    private final NotificationPreferenceRepository repository;

    public NotificationPreferenceService(NotificationPreferenceRepository repository) {
        this.repository = repository;
    }

    public NotificationPreference getPreferences(Long userId) {
        return repository.findByUserId(userId).orElse(null);
    }

    public NotificationPreference updatePreferences(Long userId, NotificationPreferencesDto dto) {
        NotificationPreference prefs = repository.findByUserId(userId)
                .orElseGet(() -> new NotificationPreference(userId));
        prefs.setEmail(dto.isEmail());
        prefs.setSms(dto.isSms());
        return repository.save(prefs);
    }
}

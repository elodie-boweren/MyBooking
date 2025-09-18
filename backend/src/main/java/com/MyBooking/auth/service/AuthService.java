// Service responsibilities: User Registration - Create new users with validation
// User Authentication - Login with JWT token generation
// Password Management - Hashing, validation, reset flow
// Profile Management - Update user information
// Role Management - Assign and validate user roles
// Notification Preferences - Manage user notification settings

package com.MyBooking.auth.service;

import com.MyBooking.auth.domain.NotificationPreference;
import com.MyBooking.auth.domain.Role;
import com.MyBooking.auth.domain.User;
import com.MyBooking.auth.repository.NotificationPreferenceRepository;
import com.MyBooking.auth.repository.UserRepository;
import com.MyBooking.common.exception.BusinessRuleException;
import com.MyBooking.common.exception.NotFoundException;
import com.MyBooking.common.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private NotificationPreferenceRepository notificationPreferenceRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService;

    /**
     * Register a new user with default CLIENT role
     */
    public User registerUser(String email, String password, String firstName, String lastName, String phone, String address, LocalDate birthDate) {
        // Validate email uniqueness
        if (userRepository.existsByEmail(email)) {
            throw new BusinessRuleException("Email already exists: " + email);
        }
        
        // Create new user
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setAddress(address);
        user.setBirthDate(birthDate);
        user.setRole(Role.CLIENT);
        
        
        User savedUser = userRepository.save(user);
        
        // Create default notification preferences
        createDefaultNotificationPreferences(savedUser);
        
        return savedUser;
    }

    /**
     * Authenticate user and generate JWT token
     */
    public String authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessRuleException("Invalid password");
        }

        // Generate JWT token
        return jwtService.generateToken(user.getEmail(), user.getRole().toString());
    }

    /**
     * Get user profile by email
     */
    @Transactional(readOnly = true)
    public User getUserProfile(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }

    /**
     * Update user profile
     */
    public User updateUserProfile(String email, String firstName, String lastName, String phone, String address, LocalDate birthDate) {
        User user = getUserProfile(email);
        
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setAddress(address);
        user.setBirthDate(birthDate);

        return userRepository.save(user);
    }

    /**
     * Change user password
     */
    public void changePassword(String email, String currentPassword, String newPassword) {
        User user = getUserProfile(email);
        
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BusinessRuleException("Current password is incorrect");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Reset password (for forgot password flow)
     */
    public void resetPassword(String email, String newPassword) {
        User user = getUserProfile(email);
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }


    /**
     * Get user notification preferences
     */
    @Transactional(readOnly = true)
    public List<NotificationPreference> getUserNotificationPreferences(String email) {
        User user = getUserProfile(email);
        return notificationPreferenceRepository.findByUser(user);
    }

    /**
     * Update notification preferences
     */
    public NotificationPreference updateNotificationPreference(String email, String notificationType, 
                                                              Boolean emailEnabled, Boolean smsEnabled, Boolean pushEnabled) {
        User user = getUserProfile(email);
        
        Optional<NotificationPreference> existing = notificationPreferenceRepository
            .findByUserAndNotificationType(user, notificationType);
        
        if (existing.isPresent()) {
            NotificationPreference preference = existing.get();
            preference.setEmailEnabled(emailEnabled);
            preference.setSmsEnabled(smsEnabled);
            preference.setPushEnabled(pushEnabled);
            return notificationPreferenceRepository.save(preference);
        } else {
            NotificationPreference preference = new NotificationPreference(user, notificationType, emailEnabled, smsEnabled, pushEnabled);
            return notificationPreferenceRepository.save(preference);
        }
    }


    /**
     * Get all users (Admin only)
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get users by role
     */
    @Transactional(readOnly = true)
    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    /**
     * Create default notification preferences for new user
     */
   private void createDefaultNotificationPreferences(User user) {
    if (user.getRole() == Role.CLIENT) {
        // Clients get reservation-related notifications
        String[] clientTypes = {"RESERVATION", "FEEDBACK"};
        
        for (String type : clientTypes) {
            NotificationPreference preference = new NotificationPreference(
                user, type, false, false, true  // email: false, sms: false, push: true
            );
            notificationPreferenceRepository.save(preference);
        }
    } else if (user.getRole() == Role.EMPLOYEE || user.getRole() == Role.ADMIN) {
        // Employees and admins get announcement notifications
        String[] employeeTypes = {"ANNOUNCEMENT"};
        
        for (String type : employeeTypes) {
            NotificationPreference preference = new NotificationPreference(
                user, type, false, false, true  // email: false, sms: false, push: true
            );
            notificationPreferenceRepository.save(preference);
        }
    }
}
}
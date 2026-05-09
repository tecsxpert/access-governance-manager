package com.internship.accessgovernancemanager.service;

import com.internship.accessgovernancemanager.entity.UserAccess;
import com.internship.accessgovernancemanager.exception.ResourceNotFoundException;
import com.internship.accessgovernancemanager.repository.UserAccessRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

// ✅ CACHE IMPORTS
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

// ✅ EMAIL SERVICE
import com.internship.accessgovernancemanager.service.EmailService;

import java.util.List;

@Service
public class UserAccessService {

    @Autowired
    private UserAccessRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // ✅ REGISTER (CLEAR CACHE)
    @CacheEvict(value = "users", allEntries = true)
    public UserAccess registerUser(UserAccess user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserAccess savedUser = repository.save(user);

        // 📧 Send welcome email
        try {
            emailService.sendWelcomeEmail(savedUser);
        } catch (Exception e) {
            // Log error but don't fail registration
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }

        return savedUser;
    }

    // ✅ FIND USER
    public UserAccess findByUsername(String username) {
        return repository.findByUsername(username).orElse(null);
    }

    public UserAccess findByEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }

    public UserAccess findByRefreshToken(String refreshToken) {
        return repository.findByRefreshToken(refreshToken).orElse(null);
    }

    public UserAccess updateRefreshToken(@NonNull UserAccess user, String refreshToken) {
        return repository.save(user);
    }

    // ✅ PASSWORD CHECK
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    // ✅ CACHE: GET ALL USERS
    @Cacheable(value = "users")
    public List<UserAccess> getAllUsers() {
        System.out.println("🔥 DB HIT - getAllUsers");
        return repository.findAll();
    }

    // ✅ CACHE: GET USER BY ID
    @Cacheable(value = "users", key = "#id")
    public UserAccess getUserById(@NonNull Long id) {
        System.out.println("🔥 DB HIT - getUserById");
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    // ✅ UPDATE USER (CLEAR CACHE)
    @CacheEvict(value = "users", allEntries = true)
    public UserAccess updateUser(@NonNull Long id, UserAccess updatedUser) {

        UserAccess existingUser = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setRole(updatedUser.getRole());
        existingUser.setAccessLevel(updatedUser.getAccessLevel());

        return repository.save(existingUser);
    }

    // ✅ DELETE USER (CLEAR CACHE)
    @CacheEvict(value = "users", allEntries = true)
    public void deleteUser(@NonNull Long id) {
        repository.deleteById(id);
    }

    // (Optional cache)
    @Cacheable(value = "users", key = "#role")
    public List<UserAccess> getUsersByRole(String role) {
        System.out.println("🔥 DB HIT - getUsersByRole");
        return repository.findByRole(role);
    }

    public Page<UserAccess> getUsers(@NonNull Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<UserAccess> searchUsers(String username) {
        return repository.findByUsernameContaining(username);
    }
}
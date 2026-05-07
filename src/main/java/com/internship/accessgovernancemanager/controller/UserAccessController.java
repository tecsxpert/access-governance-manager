package com.internship.accessgovernancemanager.controller;

import com.internship.accessgovernancemanager.entity.UserAccess;
import com.internship.accessgovernancemanager.service.UserAccessService;
import com.internship.accessgovernancemanager.service.EmailService;
import com.internship.accessgovernancemanager.dto.ApiResponse;
import com.internship.accessgovernancemanager.dto.RefreshTokenRequest;
import com.internship.accessgovernancemanager.dto.TokenResponse;
import com.internship.accessgovernancemanager.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.lang.NonNull;

// ✅ RBAC IMPORT
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserAccessController {

    @Autowired
    private UserAccessService service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    // ✅ TEST API (PUBLIC)
    @GetMapping("/test")
    public String test() {
        return "Working";
    }

    // 📧 TEST EMAIL ENDPOINT
    @PostMapping("/test-email")
    public ApiResponse<String> testEmail(@RequestParam String to, @RequestParam(required = false) String subject) {
        try {
            String emailSubject = subject != null ? subject : "Test Email from Access Governance Manager";
            String emailBody = "This is a test email from your Access Governance Manager application.\n\n" +
                              "If you received this email, the email configuration is working correctly!\n\n" +
                              "Sent at: " + java.time.LocalDateTime.now();

            emailService.sendSimpleEmail(to, emailSubject, emailBody);
            return new ApiResponse<>("success", "Test email sent successfully to: " + to, null);
        } catch (Exception e) {
            return new ApiResponse<>("error", "Failed to send test email: " + e.getMessage(), null);
        }
    }

    // 🔐 ONLY ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<List<UserAccess>> getAllUsers() {
        return new ApiResponse<>("success", "Users fetched", service.getAllUsers());
    }

    // 🔐 ADMIN + USER
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ApiResponse<UserAccess> getUserById(@PathVariable @NonNull Long id) {
        return new ApiResponse<>("success", "User found", service.getUserById(id));
    }

    // 🔐 ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/role/{role}")
    public ApiResponse<List<UserAccess>> getUsersByRole(@PathVariable String role) {
        return new ApiResponse<>("success", "Users fetched", service.getUsersByRole(role));
    }

    // 🔐 ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<UserAccess> updateUser(@PathVariable @NonNull Long id, @RequestBody UserAccess user) {
        return new ApiResponse<>("success", "User updated", service.updateUser(id, user));
    }

    // 🔐 ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable @NonNull Long id) {
        service.deleteUser(id);
        return new ApiResponse<>("success", "User deleted", null);
    }

    // 🔐 ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/paginated")
    public ApiResponse<Page<UserAccess>> getUsers(@NonNull Pageable pageable) {
        return new ApiResponse<>("success", "Users fetched", service.getUsers(pageable));
    }

    // 🔐 ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ApiResponse<List<UserAccess>> searchUsers(@RequestParam String username) {
        return new ApiResponse<>("success", "Users found", service.searchUsers(username));
    }

    // ✅ LOGIN (PUBLIC)
    @PostMapping("/login")
    public ApiResponse<TokenResponse> login(@RequestBody UserAccess user) {

        UserAccess dbUser = service.findByUsername(user.getUsername());

        if (dbUser == null) {
            throw new RuntimeException("User not found");
        }

        if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String accessToken = jwtUtil.generateAccessToken(dbUser.getUsername(), dbUser.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(dbUser.getUsername());

        service.updateRefreshToken(dbUser, refreshToken);

        TokenResponse response = new TokenResponse(accessToken, refreshToken);
        return new ApiResponse<>("success", "Login successful", response);
    }

    // ✅ REFRESH (PUBLIC)
    @PostMapping("/refresh")
    public ApiResponse<TokenResponse> refresh(@RequestBody RefreshTokenRequest refreshRequest) {

        String refreshToken = refreshRequest.getRefreshToken();

        if (refreshToken == null || refreshToken.isBlank()) {
            throw new RuntimeException("Refresh token is required");
        }

        UserAccess dbUser = service.findByRefreshToken(refreshToken);

        if (dbUser == null) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = jwtUtil.extractUsername(refreshToken);

        if (!dbUser.getUsername().equals(username) || !jwtUtil.validateToken(refreshToken, username)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String accessToken = jwtUtil.generateAccessToken(dbUser.getUsername(), dbUser.getRole());

        TokenResponse response = new TokenResponse(accessToken, refreshToken);

        return new ApiResponse<>("success", "Access token refreshed", response);
    }

    // ✅ REGISTER (PUBLIC)
    @PostMapping("/register")
    public ApiResponse<UserAccess> register(@RequestBody UserAccess user) {

        if (service.findByUsername(user.getUsername()) != null) {
            return new ApiResponse<>("failed", "Username already exists", null);
        }

        if (service.findByEmail(user.getEmail()) != null) {
            return new ApiResponse<>("failed", "Email already exists", null);
        }

        // 🔥 VERY IMPORTANT FIX
        user.setRole("ROLE_" + user.getRole().toUpperCase());

        UserAccess savedUser = service.registerUser(user);

        return new ApiResponse<>("success", "User registered successfully", savedUser);
    }


    // 🔐 ADMIN ONLY TEST
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ApiResponse<String> adminOnly() {
        return new ApiResponse<>("success", "Admin access granted", "Only admin allowed");
    }
}
package com.internship.accessgovernancemanager.controller;

import com.internship.accessgovernancemanager.entity.UserAccess;
import com.internship.accessgovernancemanager.service.UserAccessService;
import com.internship.accessgovernancemanager.dto.ApiResponse;
import com.internship.accessgovernancemanager.dto.RefreshTokenRequest;
import com.internship.accessgovernancemanager.dto.TokenResponse;
import com.internship.accessgovernancemanager.security.JwtUtil;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.lang.NonNull;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserAccessController {

    @Autowired
    private UserAccessService service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;   // ✅ FIXED

    // ✅ TEST API
    @GetMapping("/test")
    public String test() {
        return "Working";
    }

    // ✅ GET ALL USERS
    @GetMapping
    public ApiResponse<List<UserAccess>> getAllUsers() {
        return new ApiResponse<>("success", "Users fetched", service.getAllUsers());
    }

    // ✅ GET USER BY ID
    @GetMapping("/{id}")
    public ApiResponse<UserAccess> getUserById(@PathVariable @NonNull Long id) {
        return new ApiResponse<>("success", "User found", service.getUserById(id));
    }

    // ✅ GET USERS BY ROLE
    @GetMapping("/role/{role}")
    public ApiResponse<List<UserAccess>> getUsersByRole(@PathVariable String role) {
        return new ApiResponse<>("success", "Users fetched", service.getUsersByRole(role));
    }

    // ✅ UPDATE USER
    @PutMapping("/{id}")
    public ApiResponse<UserAccess> updateUser(@PathVariable @NonNull Long id, @RequestBody UserAccess user) {
        return new ApiResponse<>("success", "User updated", service.updateUser(id, user));
    }

    // ✅ DELETE USER
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable @NonNull Long id) {
        service.deleteUser(id);
        return new ApiResponse<>("success", "User deleted", null);
    }

    // ✅ PAGINATION
    @GetMapping("/paginated")
    public ApiResponse<Page<UserAccess>> getUsers(@NonNull Pageable pageable) {
        return new ApiResponse<>("success", "Users fetched", service.getUsers(pageable));
    }

    // ✅ SEARCH
    @GetMapping("/search")
    public ApiResponse<List<UserAccess>> searchUsers(@RequestParam String username) {
        return new ApiResponse<>("success", "Users found", service.searchUsers(username));
    }

    // ✅ LOGIN (JWT)
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

    // ✅ REGISTER
    @PostMapping("/register")
    public ApiResponse<UserAccess> register(@RequestBody UserAccess user) {

        System.out.println("REGISTER HIT");

        // DEBUG
        System.out.println("USERNAME: " + user.getUsername());
        System.out.println("PASSWORD: " + user.getPassword());
        System.out.println("ROLE: " + user.getRole());
        System.out.println("ACCESS: " + user.getAccessLevel());

        // ✅ FIXED (no isEmpty)
        if (service.findByUsername(user.getUsername()) != null) {
            return new ApiResponse<>("failed", "User already exists", null);
        }

        // normalize role
        user.setRole(user.getRole().toUpperCase());

        UserAccess savedUser = service.registerUser(user);

        return new ApiResponse<>("success", "User registered successfully", savedUser);
    }

    // ✅ ADMIN TEST
    @GetMapping("/admin")
    public ApiResponse<String> adminOnly() {
        return new ApiResponse<>("success", "Admin access granted", "Only admin allowed");
    }
}
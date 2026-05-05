package com.internship.accessgovernancemanager.service;

import com.internship.accessgovernancemanager.entity.UserAccess;
import com.internship.accessgovernancemanager.repository.UserAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import java.util.List;

@Service
public class UserAccessService {

    @Autowired
    private UserAccessRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ REGISTER
    public UserAccess registerUser(UserAccess user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    // ✅ FIND USER (FIXED)
    public UserAccess findByUsername(String username) {
        return repository.findByUsername(username).orElse(null); // ✅ no stream
    }

    public UserAccess findByRefreshToken(String refreshToken) {
        return repository.findByRefreshToken(refreshToken).orElse(null);
    }

    public UserAccess updateRefreshToken(UserAccess user, String refreshToken) {
        user.setRefreshToken(refreshToken);
        return repository.save(user);
    }

    // ✅ PASSWORD CHECK
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public List<UserAccess> getAllUsers() {
        return repository.findAll();
    }

    public UserAccess getUserById(@NonNull Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserAccess updateUser(@NonNull Long id, UserAccess updatedUser) {
        UserAccess existingUser = repository.findById(id).orElse(null);

        if (existingUser != null) {
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setRole(updatedUser.getRole());
            existingUser.setAccessLevel(updatedUser.getAccessLevel());
            return repository.save(existingUser);
        }

        return null;
    }

    public void deleteUser(@NonNull Long id) {
        repository.deleteById(id);
    }

    public List<UserAccess> getUsersByRole(String role) {
        return repository.findByRole(role);
    }

    public Page<UserAccess> getUsers(@NonNull Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<UserAccess> searchUsers(String username) {
        return repository.findByUsernameContaining(username);
    }
}
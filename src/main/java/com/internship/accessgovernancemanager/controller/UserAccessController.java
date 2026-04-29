package com.internship.accessgovernancemanager.controller;

import com.internship.accessgovernancemanager.entity.UserAccess;
import com.internship.accessgovernancemanager.service.UserAccessService;
import com.internship.accessgovernancemanager.dto.ApiResponse;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserAccessController {

    @Autowired
    private UserAccessService service;

    @PostMapping
    public ApiResponse<UserAccess> addUser(@Valid @RequestBody UserAccess user) {
    UserAccess savedUser = service.saveUser(user);
    return new ApiResponse<>("success", "User created successfully", savedUser);
}

   @GetMapping
public ApiResponse<List<UserAccess>> getAllUsers() {
    return new ApiResponse<>("success", "Users fetched", service.getAllUsers());
}
@GetMapping("/{id}")
public ApiResponse<UserAccess> getUserById(@PathVariable Long id) {
    return new ApiResponse<>("success", "User found", service.getUserById(id));
}

    @GetMapping("/role/{role}")
    public List<UserAccess> getUsersByRole(@PathVariable String role) {
        return service.getUsersByRole(role);
    }

    @PutMapping("/{id}")
    public UserAccess updateUser(@PathVariable Long id, @RequestBody UserAccess user) {
        return service.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
public ApiResponse<String> deleteUser(@PathVariable Long id) {
    service.deleteUser(id);
    return new ApiResponse<>("success", "User deleted", null);
}

@GetMapping("/paginated")
public ApiResponse<Page<UserAccess>> getUsers(Pageable pageable) {
    return new ApiResponse<>("success", "Users fetched", service.getUsers(pageable));
}

@GetMapping("/search")
public ApiResponse<List<UserAccess>> searchUsers(@RequestParam String username) {
    return new ApiResponse<>("success", "Users found", service.searchUsers(username));
}
}
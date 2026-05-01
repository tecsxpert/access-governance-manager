package com.intership.tool.controller;

import com.intership.tool.entity.User;
import com.intership.tool.model.Role;
import com.intership.tool.repository.UserRepository;
import com.intership.tool.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // 🔥 REGISTER
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER); // 🔥 DEFAULT ROLE

        userRepository.save(user);

        return Map.of("message", "User Registered Successfully");
    }

    // 🔥 LOGIN
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user) {

        User dbUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(dbUser.getUsername());

        return Map.of("token", token);
    }
}
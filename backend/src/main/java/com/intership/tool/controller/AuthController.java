package com.intership.tool.controller;

import com.intership.tool.entity.User;
import com.intership.tool.repository.UserRepository;
import com.intership.tool.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // 🔥 REGISTER API
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody User user) {

        System.out.println("🔥 REGISTER API HIT");

        userRepository.save(user);

        return Map.of("message", "User Registered Successfully");
    }

    // 🔐 LOGIN API
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user) {

        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser.isPresent()) {

            User dbUser = existingUser.get();

            // 👉 password check (simple)
            if (dbUser.getPassword().equals(user.getPassword())) {

                String token = jwtUtil.generateToken(user.getUsername());

                System.out.println("✅ Valid Token for user: " + user.getUsername());

                return Map.of("token", token);
            }
        }

        return Map.of("error", "Invalid username or password");
    }
}
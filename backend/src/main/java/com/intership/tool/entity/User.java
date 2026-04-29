package com.intership.tool.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")   // 🔥 IMPORTANT: user → users
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    // ✅ DEFAULT CONSTRUCTOR
    public User() {
    }

    // ✅ PARAMETERIZED CONSTRUCTOR
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // ✅ GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
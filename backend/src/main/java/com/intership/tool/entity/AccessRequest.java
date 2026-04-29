package com.intership.tool.entity;

import com.intership.tool.model.Status;
import jakarta.persistence.*;

@Entity
public class AccessRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String resourceName;
    private String accessType;

    @Enumerated(EnumType.STRING)
    private Status status;

    // 🔹 Constructors
    public AccessRequest() {}

    public AccessRequest(Long id, String userName, String resourceName, String accessType, Status status) {
        this.id = id;
        this.userName = userName;
        this.resourceName = resourceName;
        this.accessType = accessType;
        this.status = status;
    }

    // 🔹 Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
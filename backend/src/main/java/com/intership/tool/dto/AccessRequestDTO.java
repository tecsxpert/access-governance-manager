package com.intership.tool.dto;

public class AccessRequestDTO {

    private String resourceName;
    private String accessType;

    public AccessRequestDTO() {}

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
}
package com.intership.tool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intership.tool.entity.AccessRequest;

public interface AccessRequestRepository
        extends JpaRepository<AccessRequest, Long> {

    List<AccessRequest> findByUserName(String userName);
}
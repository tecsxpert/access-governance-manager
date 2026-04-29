package com.intership.tool.repository;

import com.intership.tool.entity.AccessRequest;
import com.intership.tool.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessRequestRepository extends JpaRepository<AccessRequest, Long> {

    List<AccessRequest> findByStatus(Status status);
}
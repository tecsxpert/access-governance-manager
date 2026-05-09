package com.intership.tool.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.intership.tool.entity.AccessRequest;
import com.intership.tool.model.Status;
import com.intership.tool.repository.AccessRequestRepository;

@Service
public class AccessRequestService {

    private final AccessRequestRepository repository;

    public AccessRequestService(
            AccessRequestRepository repository
    ) {
        this.repository = repository;
    }

    // ✅ CREATE
    public AccessRequest createRequest(
            AccessRequest request
    ) {

        request.setStatus(Status.PENDING);

        return repository.save(request);
    }

    // ✅ GET ALL
    public List<AccessRequest> getAllRequests() {

        return repository.findAll();
    }

    // ✅ APPROVE
    public AccessRequest approveRequest(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        AccessRequest request =
                repository.findById(id).orElseThrow();

        request.setStatus(Status.APPROVED);

        return repository.save(request);
    }

    // ✅ REJECT
    public AccessRequest rejectRequest(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        AccessRequest request =
                repository.findById(id).orElseThrow();

        request.setStatus(Status.REJECTED);

        return repository.save(request);
    }

    // ✅ MY REQUESTS
    public List<AccessRequest> getRequestsByUsername(
            String username
    ) {

        return repository.findByUserName(username);
    }
}
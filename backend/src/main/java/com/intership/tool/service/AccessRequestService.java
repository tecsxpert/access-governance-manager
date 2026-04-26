package com.intership.tool.service;

import com.intership.tool.entity.AccessRequest;
import com.intership.tool.repository.AccessRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessRequestService {

    private final AccessRequestRepository repository;

    public AccessRequestService(AccessRequestRepository repository) {
        this.repository = repository;
    }

    // CREATE
    public AccessRequest save(AccessRequest request) {
        if (request.getUserName() == null || request.getUserName().isEmpty()) {
            throw new RuntimeException("UserName is required");
        }
        return repository.save(request);
    }

    // READ ALL
    public List<AccessRequest> getAll() {
        return repository.findAll();
    }

    // READ BY ID
    public AccessRequest getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
    }

    // UPDATE
    public AccessRequest update(Long id, AccessRequest request) {
        AccessRequest existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        existing.setUserName(request.getUserName());
        existing.setResourceName(request.getResourceName());
        existing.setAccessType(request.getAccessType());
        existing.setStatus(request.getStatus());

        return repository.save(existing);
    }

    // DELETE
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
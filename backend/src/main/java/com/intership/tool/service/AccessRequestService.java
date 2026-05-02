package com.intership.tool.service;

import com.intership.tool.entity.AccessRequest;
import com.intership.tool.model.Status;
import com.intership.tool.repository.AccessRequestRepository;
import com.intership.tool.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessRequestService {

    private final AccessRequestRepository repository;

    public AccessRequestService(AccessRequestRepository repository) {
        this.repository = repository;
    }

    // ✅ CREATE REQUEST
    public AccessRequest createRequest(String username, String resource, String type) {
        AccessRequest req = new AccessRequest();
        req.setUserName(username);
        req.setResourceName(resource);
        req.setAccessType(type);
        req.setStatus(Status.PENDING);

        return repository.save(req);
    }

    // ✅ GET ALL REQUESTS
    public List<AccessRequest> getAllRequests() {
        return repository.findAll();
    }

    // ✅ APPROVE REQUEST
    public AccessRequest approve(Long id) {
        AccessRequest req = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Request not found with id: " + id)
                );

        req.setStatus(Status.APPROVED);
        return repository.save(req);
    }

    // ✅ REJECT REQUEST
    public AccessRequest reject(Long id) {
        AccessRequest req = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Request not found with id: " + id)
                );

        req.setStatus(Status.REJECTED);
        return repository.save(req);
    }
}
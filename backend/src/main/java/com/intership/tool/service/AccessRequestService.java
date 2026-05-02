package com.intership.tool.service;

import com.intership.tool.entity.AccessRequest;
import com.intership.tool.model.Status;
import com.intership.tool.repository.AccessRequestRepository;
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

    // ✅ GET ALL
    public List<AccessRequest> getAllRequests() {
        return repository.findAll();
    }

    // ✅ APPROVE
    public AccessRequest approve(Long id) {
        AccessRequest req = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        req.setStatus(Status.APPROVED);
        return repository.save(req);
    }

    // ✅ REJECT
    public AccessRequest reject(Long id) {
        AccessRequest req = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        req.setStatus(Status.REJECTED);
        return repository.save(req);
    }
}
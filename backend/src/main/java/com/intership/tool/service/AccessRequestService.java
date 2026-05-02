package com.intership.tool.service;

import com.intership.tool.entity.AccessRequest;
import com.intership.tool.model.Status;
import com.intership.tool.repository.AccessRequestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessRequestService {

    @Autowired
    private AccessRequestRepository repository;

    // ✅ CREATE REQUEST
    public AccessRequest createRequest(AccessRequest request) {
        request.setStatus(Status.PENDING);
        return repository.save(request);
    }

    // ✅ GET ALL
    public List<AccessRequest> getAllRequests() {
        return repository.findAll();
    }

    // ✅ APPROVE
    public AccessRequest approveRequest(Long id) {
        AccessRequest req = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        req.setStatus(Status.APPROVED);
        return repository.save(req);
    }

    // ✅ REJECT
    public AccessRequest rejectRequest(Long id) {
        AccessRequest req = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        req.setStatus(Status.REJECTED);
        return repository.save(req);
    }
}
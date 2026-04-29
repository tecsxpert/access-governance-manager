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

    // ✅ CREATE
    public AccessRequest create(AccessRequest request) {
        // 🔥 Default status fix
        if (request.getStatus() == null) {
            request.setStatus(Status.PENDING);
        }
        return repository.save(request);
    }

    // ✅ GET ALL
    public List<AccessRequest> getAll() {
        return repository.findAll();
    }

    // ✅ GET BY ID
    public AccessRequest getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
    }

    // ✅ UPDATE FULL
    public AccessRequest update(Long id, AccessRequest newRequest) {
        AccessRequest existing = getById(id);

        existing.setUserName(newRequest.getUserName());
        existing.setResourceName(newRequest.getResourceName());
        existing.setAccessType(newRequest.getAccessType());

        // 🔥 status null ah irundha overwrite pannadhe
        if (newRequest.getStatus() != null) {
            existing.setStatus(newRequest.getStatus());
        }

        return repository.save(existing);
    }

    // ✅ DELETE
    public void delete(Long id) {
        repository.deleteById(id);
    }

    // 🔥 DAY 3 FEATURE - UPDATE STATUS
    public AccessRequest updateStatus(Long id, Status status) {
        AccessRequest request = getById(id);
        request.setStatus(status);
        return repository.save(request);
    }

    // 🔥 BONUS - GET ONLY PENDING
    public List<AccessRequest> getPending() {
        return repository.findByStatus(Status.PENDING);
    }
}
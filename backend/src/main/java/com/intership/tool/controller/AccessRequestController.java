package com.intership.tool.controller;

import com.intership.tool.entity.AccessRequest;
import com.intership.tool.model.Status;
import com.intership.tool.repository.AccessRequestRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/access")
public class AccessRequestController {

    private final AccessRequestRepository repo;

    public AccessRequestController(AccessRequestRepository repo) {
        this.repo = repo;
    }

    // 🔥 USER CREATE REQUEST
    @PostMapping("/request")
    public AccessRequest createRequest(@RequestBody AccessRequest request) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        request.setUserName(username);
        request.setStatus(Status.PENDING);

        return repo.save(request);
    }

    // 🔥 VIEW ALL (ANY LOGGED USER)
    @GetMapping("/all")
    public List<AccessRequest> getAll() {
        return repo.findAll();
    }

    // 🔥 ADMIN ONLY APPROVE
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/approve/{id}")
    public AccessRequest approve(@PathVariable Long id) {

        AccessRequest req = repo.findById(id).orElseThrow();
        req.setStatus(Status.APPROVED);

        return repo.save(req);
    }

    // 🔥 ADMIN ONLY REJECT
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/reject/{id}")
    public AccessRequest reject(@PathVariable Long id) {

        AccessRequest req = repo.findById(id).orElseThrow();
        req.setStatus(Status.REJECTED);

        return repo.save(req);
    }
}
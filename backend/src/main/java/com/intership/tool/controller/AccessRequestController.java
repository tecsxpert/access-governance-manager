package com.intership.tool.controller;

import com.intership.tool.dto.AccessRequestDTO;
import com.intership.tool.entity.AccessRequest;
import com.intership.tool.service.AccessRequestService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/access")
public class AccessRequestController {

    private final AccessRequestService service;

    public AccessRequestController(AccessRequestService service) {
        this.service = service;
    }

    // ✅ CREATE REQUEST (USER)
    @PostMapping("/request")
    public AccessRequest create(@RequestBody AccessRequestDTO dto, Principal principal) {
        return service.createRequest(
                principal.getName(),
                dto.getResourceName(),
                dto.getAccessType()
        );
    }

    // ✅ VIEW ALL (ADMIN)
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AccessRequest> getAll() {
        return service.getAllRequests();
    }

    // ✅ APPROVE (ADMIN)
    @PutMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AccessRequest approve(@PathVariable Long id) {
        return service.approve(id);
    }

    // ✅ REJECT (ADMIN)
    @PutMapping("/reject/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AccessRequest reject(@PathVariable Long id) {
        return service.reject(id);
    }
}
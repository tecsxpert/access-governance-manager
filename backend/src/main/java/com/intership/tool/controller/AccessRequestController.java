package com.intership.tool.controller;

import com.intership.tool.entity.AccessRequest;
import com.intership.tool.model.Status;
import com.intership.tool.service.AccessRequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/access")
public class AccessRequestController {

    private final AccessRequestService service;

    public AccessRequestController(AccessRequestService service) {
        this.service = service;
    }

    @PostMapping
    public AccessRequest create(@RequestBody AccessRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<AccessRequest> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public AccessRequest getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public AccessRequest update(@PathVariable Long id,
                                @RequestBody AccessRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PutMapping("/{id}/status")
    public AccessRequest updateStatus(@PathVariable Long id,
                                      @RequestParam String status) {
        return service.updateStatus(id, Status.valueOf(status.toUpperCase()));
    }

    @GetMapping("/pending")
    public List<AccessRequest> getPending() {
        return service.getPending();
    }
}
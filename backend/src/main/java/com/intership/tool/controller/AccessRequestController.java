package com.intership.tool.controller;

import com.intership.tool.entity.AccessRequest;
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

    // CREATE
    @PostMapping
    public AccessRequest create(@RequestBody AccessRequest request) {
        return service.save(request);
    }

    // READ ALL
    @GetMapping
    public List<AccessRequest> getAll() {
        return service.getAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public AccessRequest getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public AccessRequest update(@PathVariable Long id, @RequestBody AccessRequest request) {
        return service.update(id, request);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Deleted successfully";
    }
}
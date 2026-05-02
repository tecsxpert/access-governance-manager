package com.intership.tool.controller;

import com.intership.tool.entity.AccessRequest;
import com.intership.tool.service.AccessRequestService;
import com.intership.tool.dto.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/access")
public class AccessRequestController {

    @Autowired
    private AccessRequestService service;

    // ✅ CREATE REQUEST
    @PostMapping("/request")
    public ApiResponse<AccessRequest> createRequest(@RequestBody AccessRequest request) {

        // ✅ FIX: pass full object
        AccessRequest saved = service.createRequest(request);

        return new ApiResponse<>("Request created successfully", saved);
    }

    // ✅ GET ALL REQUESTS
    @GetMapping("/all")
    public ApiResponse<List<AccessRequest>> getAllRequests() {
        List<AccessRequest> list = service.getAllRequests();
        return new ApiResponse<>("All requests fetched", list);
    }

    // ✅ APPROVE
    @PutMapping("/approve/{id}")
    public ApiResponse<AccessRequest> approve(@PathVariable Long id) {
        AccessRequest req = service.approveRequest(id);
        return new ApiResponse<>("Request approved", req);
    }

    // ✅ REJECT
    @PutMapping("/reject/{id}")
    public ApiResponse<AccessRequest> reject(@PathVariable Long id) {
        AccessRequest req = service.rejectRequest(id);
        return new ApiResponse<>("Request rejected", req);
    }
}
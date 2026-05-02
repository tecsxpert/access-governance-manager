package com.intership.tool.controller;

import com.intership.tool.entity.AccessRequest;
import com.intership.tool.security.JwtUtil;
import com.intership.tool.service.AccessRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/access")
public class AccessRequestController {

    private final AccessRequestService service;
    private final JwtUtil jwtUtil;

    public AccessRequestController(AccessRequestService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    // ✅ CREATE REQUEST (USER ONLY)
    @PostMapping("/request")
    public ResponseEntity<?> createRequest(@RequestBody AccessRequest request,
                                           @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String role = jwtUtil.extractRole(token);
        String username = jwtUtil.extractUsername(token);

        System.out.println("ROLE: " + role);

        if (role == null || !role.equalsIgnoreCase("USER")) {
            return ResponseEntity.status(403).body("Only USER can create request");
        }

        request.setUserName(username);

        return ResponseEntity.ok(service.createRequest(request));
    }

    // ✅ VIEW ALL (ADMIN ONLY)
    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String role = jwtUtil.extractRole(token);

        if (role == null || !role.equalsIgnoreCase("ADMIN")) {
            return ResponseEntity.status(403).body("Only ADMIN can view");
        }

        return ResponseEntity.ok(service.getAllRequests());
    }

    // ✅ APPROVE (ADMIN ONLY)
    @PutMapping("/approve/{id}")
    public ResponseEntity<?> approve(@PathVariable Long id,
                                     @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String role = jwtUtil.extractRole(token);

        if (role == null || !role.equalsIgnoreCase("ADMIN")) {
            return ResponseEntity.status(403).body("Only ADMIN can approve");
        }

        return ResponseEntity.ok(service.approveRequest(id));
    }

    // ✅ REJECT (ADMIN ONLY)
    @PutMapping("/reject/{id}")
    public ResponseEntity<?> reject(@PathVariable Long id,
                                    @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String role = jwtUtil.extractRole(token);

        if (role == null || !role.equalsIgnoreCase("ADMIN")) {
            return ResponseEntity.status(403).body("Only ADMIN can reject");
        }

        return ResponseEntity.ok(service.rejectRequest(id));
    }
}
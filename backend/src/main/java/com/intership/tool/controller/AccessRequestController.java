package com.intership.tool.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intership.tool.dto.ApiResponse;
import com.intership.tool.entity.AccessRequest;
import com.intership.tool.service.AccessRequestService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/access")
public class AccessRequestController {

    private final AccessRequestService service;

    public AccessRequestController(
            AccessRequestService service
    ) {
        this.service = service;
    }

    // ✅ CREATE REQUEST
    @PostMapping("/request")
    public ResponseEntity<?> createRequest(
            @Valid @RequestBody AccessRequest request,
            HttpServletRequest httpRequest
    ) {

        String username =
                (String) httpRequest.getAttribute("username");

        String role =
                (String) httpRequest.getAttribute("role");

        System.out.println(
                "User: " + username +
                " Role: " + role
        );

        if (role == null ||
            (!role.equalsIgnoreCase("USER") &&
             !role.equalsIgnoreCase("ROLE_USER"))) {

            return ResponseEntity.status(403)
                    .body(
                            new ApiResponse<>(
                                    "Only USER can create request",
                                    null
                            )
                    );
        }

        request.setUserName(username);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Request created",
                        service.createRequest(request)
                )
        );
    }

    // ✅ GET ALL REQUESTS
    @GetMapping("/all")
    public ResponseEntity<?> getAll(
            HttpServletRequest httpRequest
    ) {

        String role =
                (String) httpRequest.getAttribute("role");

        if (role == null ||
            (!role.equalsIgnoreCase("ADMIN") &&
             !role.equalsIgnoreCase("ROLE_ADMIN"))) {

            return ResponseEntity.status(403)
                    .body(
                            new ApiResponse<>(
                                    "Only ADMIN can view",
                                    null
                            )
                    );
        }

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "All Requests",
                        service.getAllRequests()
                )
        );
    }

    // ✅ APPROVE
    @PutMapping("/approve/{id}")
    public ResponseEntity<?> approve(
            @PathVariable Long id,
            HttpServletRequest httpRequest
    ) {

        String role =
                (String) httpRequest.getAttribute("role");

        if (role == null ||
            (!role.equalsIgnoreCase("ADMIN") &&
             !role.equalsIgnoreCase("ROLE_ADMIN"))) {

            return ResponseEntity.status(403)
                    .body(
                            new ApiResponse<>(
                                    "Only ADMIN can approve",
                                    null
                            )
                    );
        }

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Approved",
                        service.approveRequest(id)
                )
        );
    }

    // ✅ REJECT
    @PutMapping("/reject/{id}")
    public ResponseEntity<?> reject(
            @PathVariable Long id,
            HttpServletRequest httpRequest
    ) {

        String role =
                (String) httpRequest.getAttribute("role");

        if (role == null ||
            (!role.equalsIgnoreCase("ADMIN") &&
             !role.equalsIgnoreCase("ROLE_ADMIN"))) {

            return ResponseEntity.status(403)
                    .body(
                            new ApiResponse<>(
                                    "Only ADMIN can reject",
                                    null
                            )
                    );
        }

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Rejected",
                        service.rejectRequest(id)
                )
        );
    }

    // ✅ MY REQUESTS
    @GetMapping("/my")
    public ResponseEntity<?> myRequests(
            HttpServletRequest httpRequest
    ) {

        String username =
                (String) httpRequest.getAttribute("username");

        String role =
                (String) httpRequest.getAttribute("role");

        if (role == null ||
            (!role.equalsIgnoreCase("USER") &&
             !role.equalsIgnoreCase("ROLE_USER"))) {

            return ResponseEntity.status(403)
                    .body(
                            new ApiResponse<>(
                                    "Only USER can view own requests",
                                    null
                            )
                    );
        }

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "My Requests",
                        service.getRequestsByUsername(username)
                )
        );
    }
}
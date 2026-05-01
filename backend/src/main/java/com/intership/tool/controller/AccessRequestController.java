package com.intership.tool.controller;

import com.intership.tool.entity.AccessRequest;
import com.intership.tool.model.Status;
import com.intership.tool.repository.AccessRequestRepository;
import com.intership.tool.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/access")
public class AccessRequestController {

    private final AccessRequestRepository repo;
    private final UserRepository userRepo;

    public AccessRequestController(AccessRequestRepository repo, UserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    // ✅ USER create request
    @PostMapping("/request")
    public AccessRequest createRequest(@RequestBody AccessRequest request) {
        request.setStatus(Status.PENDING);
        return repo.save(request);
    }

    // ✅ ADMIN approve
    @PutMapping("/approve/{id}")
    public String approve(@PathVariable Long id, @RequestParam String username) {

        var user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Only ADMIN can approve");
        }

        AccessRequest req = repo.findById(id).orElseThrow();
        req.setStatus(Status.APPROVED);
        repo.save(req);

        return "Approved ✅";
    }

    // ❌ ADMIN reject
    @PutMapping("/reject/{id}")
    public String reject(@PathVariable Long id, @RequestParam String username) {

        var user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Only ADMIN can reject");
        }

        AccessRequest req = repo.findById(id).orElseThrow();
        req.setStatus(Status.REJECTED);
        repo.save(req);

        return "Rejected ❌";
    }

    // 📄 view all
    @GetMapping("/all")
    public List<AccessRequest> getAll() {
        return repo.findAll();
    }
}
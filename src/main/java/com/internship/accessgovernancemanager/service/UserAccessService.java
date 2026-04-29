package com.internship.accessgovernancemanager.service;

import com.internship.accessgovernancemanager.entity.UserAccess;
import com.internship.accessgovernancemanager.repository.UserAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class UserAccessService {

    @Autowired
    private UserAccessRepository repository;

    public UserAccess saveUser(UserAccess user) {
        return repository.save(user);
    }

    public List<UserAccess> getAllUsers() {
    return repository.findAll();
}

public UserAccess getUserById(Long id) {
    return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
}


public UserAccess updateUser(Long id, UserAccess updatedUser) {
    UserAccess existingUser = repository.findById(id).orElse(null);

    if (existingUser != null) {
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setRole(updatedUser.getRole());
        existingUser.setAccessLevel(updatedUser.getAccessLevel());
        return repository.save(existingUser);
    }

    return null;
}

public void deleteUser(Long id) {
    repository.deleteById(id);
}

public List<UserAccess> getUsersByRole(String role) {
    return repository.findByRole(role);
}

public List<UserAccess> getUsersByUsername(String username) {
    return repository.findByUsername(username);
}

public Page<UserAccess> getUsers(Pageable pageable) {
    return repository.findAll(pageable);
}
}
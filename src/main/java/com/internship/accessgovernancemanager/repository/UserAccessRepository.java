package com.internship.accessgovernancemanager.repository;

import com.internship.accessgovernancemanager.entity.UserAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserAccessRepository extends JpaRepository<UserAccess, Long> {

    //  Find by role
    List<UserAccess> findByRole(String role);

    //  Find by username
    Optional<UserAccess>findByUsername(String username);

    //  Find by email
    Optional<UserAccess> findByEmail(String email);

    List<UserAccess> findByUsernameContaining(String username);

    Optional<UserAccess> findByRefreshToken(String refreshToken);

}
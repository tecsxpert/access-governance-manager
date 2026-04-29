package com.internship.accessgovernancemanager.repository;

import com.internship.accessgovernancemanager.entity.UserAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserAccessRepository extends JpaRepository<UserAccess, Long> {

    //  Find by role
    List<UserAccess> findByRole(String role);

    //  Find by username
    List<UserAccess> findByUsername(String username);

    List<UserAccess> findByUsernameContaining(String username);

}
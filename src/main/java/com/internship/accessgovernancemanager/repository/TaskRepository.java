package com.internship.accessgovernancemanager.repository;

import com.internship.accessgovernancemanager.entity.Task;
import com.internship.accessgovernancemanager.entity.UserAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAssignedUser(UserAccess user);

    List<Task> findByStatus(String status);

    List<Task> findByAssignedUserAndStatus(UserAccess user, String status);

    // Find tasks near deadline (within next 24 hours)
    @Query("SELECT t FROM Task t WHERE t.deadline BETWEEN :now AND :tomorrow AND t.status != 'COMPLETED'")
    List<Task> findTasksNearDeadline(@Param("now") LocalDateTime now, @Param("tomorrow") LocalDateTime tomorrow);

    // Find overdue tasks
    @Query("SELECT t FROM Task t WHERE t.deadline < :now AND t.status != 'COMPLETED'")
    List<Task> findOverdueTasks(@Param("now") LocalDateTime now);
}
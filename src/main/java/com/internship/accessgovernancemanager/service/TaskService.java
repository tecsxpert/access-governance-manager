package com.internship.accessgovernancemanager.service;

import com.internship.accessgovernancemanager.entity.Task;
import com.internship.accessgovernancemanager.entity.UserAccess;
import com.internship.accessgovernancemanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> getTasksByUser(UserAccess user) {
        return taskRepository.findByAssignedUser(user);
    }

    public List<Task> getTasksByStatus(String status) {
        return taskRepository.findByStatus(status);
    }

    public List<Task> getTasksByUserAndStatus(UserAccess user, String status) {
        return taskRepository.findByAssignedUserAndStatus(user, status);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    // Get tasks near deadline (next 24 hours)
    public List<Task> getTasksNearDeadline() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);
        return taskRepository.findTasksNearDeadline(now, tomorrow);
    }

    // Get overdue tasks
    public List<Task> getOverdueTasks() {
        LocalDateTime now = LocalDateTime.now();
        return taskRepository.findOverdueTasks(now);
    }
}
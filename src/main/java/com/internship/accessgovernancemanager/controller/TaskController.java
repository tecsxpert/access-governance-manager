package com.internship.accessgovernancemanager.controller;

import com.internship.accessgovernancemanager.entity.Task;
import com.internship.accessgovernancemanager.entity.UserAccess;
import com.internship.accessgovernancemanager.service.TaskService;
import com.internship.accessgovernancemanager.service.UserAccessService;
import com.internship.accessgovernancemanager.dto.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserAccessService userService;

    // 🔐 ADMIN - Get all tasks
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<List<Task>> getAllTasks() {
        return new ApiResponse<>("success", "Tasks fetched", taskService.getAllTasks());
    }

    // 🔐 ADMIN + USER - Get task by ID
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ApiResponse<Task> getTaskById(@PathVariable @NonNull Long id) {
        Optional<Task> task = taskService.getTaskById(id);
        if (task.isPresent()) {
            return new ApiResponse<>("success", "Task found", task.get());
        } else {
            return new ApiResponse<>("failed", "Task not found", null);
        }
    }

    // 🔐 ADMIN + USER - Get tasks by current user
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/my-tasks")
    public ApiResponse<List<Task>> getMyTasks() {
        // TODO: Get current user from security context
        // For now, return empty list
        return new ApiResponse<>("success", "User tasks fetched", List.of());
    }

    // 🔐 ADMIN - Create task
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<Task> createTask(@RequestBody Task task, @RequestParam(required = false) Long userId) {
        if (userId != null) {
            UserAccess user = userService.getUserById(userId);
            task.setAssignedUser(user);
        }
        Task savedTask = taskService.createTask(task);
        return new ApiResponse<>("success", "Task created successfully", savedTask);
    }

    // 🔐 ADMIN - Update task
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<Task> updateTask(@PathVariable @NonNull Long id, @RequestBody Task task) {
        task.setId(id);
        Task updatedTask = taskService.updateTask(task);
        return new ApiResponse<>("success", "Task updated successfully", updatedTask);
    }

    // 🔐 ADMIN - Delete task
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteTask(@PathVariable @NonNull Long id) {
        taskService.deleteTask(id);
        return new ApiResponse<>("success", "Task deleted successfully", null);
    }

    // 🔐 ADMIN - Get tasks by status
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status/{status}")
    public ApiResponse<List<Task>> getTasksByStatus(@PathVariable String status) {
        return new ApiResponse<>("success", "Tasks fetched", taskService.getTasksByStatus(status));
    }

    // 🔐 ADMIN - Get tasks near deadline
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/near-deadline")
    public ApiResponse<List<Task>> getTasksNearDeadline() {
        return new ApiResponse<>("success", "Tasks near deadline fetched", taskService.getTasksNearDeadline());
    }

    // 🔐 ADMIN - Get overdue tasks
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/overdue")
    public ApiResponse<List<Task>> getOverdueTasks() {
        return new ApiResponse<>("success", "Overdue tasks fetched", taskService.getOverdueTasks());
    }
}
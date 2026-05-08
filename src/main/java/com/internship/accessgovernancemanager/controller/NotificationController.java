package com.internship.accessgovernancemanager.controller;

import com.internship.accessgovernancemanager.entity.Task;
import com.internship.accessgovernancemanager.entity.UserAccess;
import com.internship.accessgovernancemanager.service.NotificationService;
import com.internship.accessgovernancemanager.service.TaskService;
import com.internship.accessgovernancemanager.service.UserAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserAccessService userService;

    @Autowired
    private TaskService taskService;

    @PostMapping("/trigger-reminders")
    public String triggerReminders() {
        notificationService.sendDailyReminders();
        return "Daily reminders triggered";
    }

    @PostMapping("/trigger-deadline-alerts")
    public String triggerDeadlineAlerts() {
        notificationService.sendDeadlineAlerts();
        return "Deadline alerts triggered";
    }

    @PostMapping("/trigger-overdue-alerts")
    public String triggerOverdueAlerts() {
        notificationService.sendOverdueAlerts();
        return "Overdue alerts triggered";
    }

    @PostMapping("/trigger-all")
    public String triggerAllNotifications() {
        notificationService.sendDailyReminders();
        notificationService.sendDeadlineAlerts();
        notificationService.sendOverdueAlerts();
        return "All notification jobs triggered";
    }

    @PostMapping("/setup-demo-data")
    public String setupDemoData(@RequestParam String username, @RequestParam String email) {
        if (userService.findByUsername(username) != null) {
            return "User already exists: " + username;
        }

        UserAccess user = new UserAccess();
        user.setUsername(username);
        user.setPassword("Password123!");
        user.setEmail(email);
        user.setRole("ROLE_USER");
        user.setAccessLevel("USER");

        UserAccess savedUser = userService.registerUser(user);

        createTask("Pending Task 1", "Pending task due in 2 days", LocalDateTime.now().plusDays(2), "PENDING", savedUser);
        createTask("Pending Task 2", "Pending task due in 3 days", LocalDateTime.now().plusDays(3), "PENDING", savedUser);
        createTask("Near Deadline Task", "This task is due very soon", LocalDateTime.now().plusMinutes(10), "PENDING", savedUser);
        createTask("Overdue Task", "This task is already overdue", LocalDateTime.now().minusMinutes(10), "PENDING", savedUser);

        return "Demo data created for user " + username + " with email " + email + ". Password = Password123!";
    }

    private Task createTask(String title, String description, LocalDateTime deadline, String status, UserAccess user) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setDeadline(deadline);
        task.setStatus(status);
        task.setAssignedUser(user);
        return taskService.createTask(task);
    }
}

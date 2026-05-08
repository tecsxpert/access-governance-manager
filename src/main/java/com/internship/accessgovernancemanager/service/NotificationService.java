package com.internship.accessgovernancemanager.service;

import com.internship.accessgovernancemanager.entity.Task;
import com.internship.accessgovernancemanager.entity.UserAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private UserAccessService userService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private EmailService emailService;

    // 🕘 DAILY REMINDER - Every day at 9 AM
    @Scheduled(fixedRate=900000)
    public void sendDailyReminders() {
        System.out.println("🔔 Sending daily reminders...");


        List<UserAccess> users = userService.getAllUsers();

        for (UserAccess user : users) {
            // Get pending tasks for this user
            List<Task> pendingTasks = taskService.getTasksByUserAndStatus(user, "PENDING");

            if (!pendingTasks.isEmpty()) {
                try {
                    emailService.sendTaskReminderEmail(user, pendingTasks);
                    System.out.println("✅ Sent daily reminder to: " + user.getEmail());
                } catch (Exception e) {
                    System.err.println("❌ Failed to send reminder to: " + user.getEmail() + " - " + e.getMessage());
                }
            }
        }
    }

    // ⏰ DEADLINE ALERT - Every 6 hours (check for tasks due in next 24 hours)
    @Scheduled(fixedRate = 1800000) // 6 hours in milliseconds
    public void sendDeadlineAlerts() {
        System.out.println("⚠️ Checking for deadline alerts...");

        List<Task> tasksNearDeadline = taskService.getTasksNearDeadline();

        // Group tasks by assigned user
        tasksNearDeadline.stream()
            .collect(Collectors.groupingBy(Task::getAssignedUser))
            .forEach((user, userTasks) -> {
                if (user != null) {
                    try {
                        emailService.sendDeadlineAlertEmail(user, userTasks);
                        System.out.println("✅ Sent deadline alert to: " + user.getEmail() + " for " + userTasks.size() + " tasks");
                    } catch (Exception e) {
                        System.err.println("❌ Failed to send deadline alert to: " + user.getEmail() + " - " + e.getMessage());
                    }
                }
            });
    }

    // 🚨 OVERDUE TASKS ALERT - Every 12 hours
    @Scheduled(fixedRate = 43200000) // 12 hours in milliseconds
    public void sendOverdueAlerts() {
        System.out.println("🚨 Checking for overdue tasks...");

        List<Task> overdueTasks = taskService.getOverdueTasks();

        // Group tasks by assigned user
        overdueTasks.stream()
            .collect(Collectors.groupingBy(Task::getAssignedUser))
            .forEach((user, userTasks) -> {
                if (user != null) {
                    try {
                        emailService.sendOverdueTasksEmail(user, userTasks);
                        System.out.println("✅ Sent overdue alert to: " + user.getEmail() + " for " + userTasks.size() + " tasks");
                    } catch (Exception e) {
                        System.err.println("❌ Failed to send overdue alert to: " + user.getEmail() + " - " + e.getMessage());
                    }
                }
            });
    }
}
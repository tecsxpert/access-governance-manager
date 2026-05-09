package com.internship.accessgovernancemanager.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.internship.accessgovernancemanager.entity.Task;
import com.internship.accessgovernancemanager.entity.UserAccess;
import com.internship.accessgovernancemanager.repository.TaskRepository;
import com.internship.accessgovernancemanager.repository.UserAccessRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserAccessRepository userRepository;
    private final TaskRepository taskRepository;

    public DataSeeder(UserAccessRepository userRepository,
                      TaskRepository taskRepository) {

        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public void run(String... args) throws Exception {
      System.out.println("DATA SEEDER STARTED");

        List<UserAccess> users = new ArrayList<>();

        // ✅ Create 10 Users
        for(int i = 1; i <= 10; i++) {

            UserAccess user = new UserAccess();

            user.setUsername("user" + i);
            user.setPassword("password123");
            user.setRole(i % 2 == 0 ? "ADMIN" : "USER");
            user.setAccessLevel(i % 2 == 0 ? "HIGH" : "LOW");
            user.setEmail("user" + i + "@gmail.com");

            users.add(user);
        }

        userRepository.saveAll(users);

        List<Task> tasks = new ArrayList<>();

        // ✅ Create 20 Tasks
        for(int i = 1; i <= 20; i++) {

            Task task = new Task();

            task.setTitle("Task " + i);
            task.setDescription("Sample task description " + i);

            task.setStatus(i % 2 == 0 ? "COMPLETED" : "PENDING");

            task.setDeadline(LocalDateTime.now().plusDays(i));

            // Assign random user
            task.setAssignedUser(users.get(i % users.size()));

            tasks.add(task);
        }

        taskRepository.saveAll(tasks);

        System.out.println("✅ 30 Sample Records Inserted Successfully");
    }
}

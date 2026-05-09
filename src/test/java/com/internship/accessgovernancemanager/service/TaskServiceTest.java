package com.internship.accessgovernancemanager.service;

import com.internship.accessgovernancemanager.entity.Task;
import com.internship.accessgovernancemanager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService service;

    @Test
    void getTasksNearDeadline_returnsUpcomingTasks() {
        Task task = new Task();
        task.setTitle("Deadline soon");
        task.setDeadline(LocalDateTime.now().plusHours(12));

        when(taskRepository.findTasksNearDeadline(any(), any())).thenReturn(List.of(task));

        List<Task> result = service.getTasksNearDeadline();

        assertEquals(1, result.size());
        assertEquals("Deadline soon", result.get(0).getTitle());
        verify(taskRepository).findTasksNearDeadline(any(), any());
    }

    @Test
    void getOverdueTasks_returnsOverdueTasks() {
        Task overdue = new Task();
        overdue.setTitle("Overdue task");
        overdue.setDeadline(LocalDateTime.now().minusHours(2));

        when(taskRepository.findOverdueTasks(any())).thenReturn(List.of(overdue));

        List<Task> result = service.getOverdueTasks();

        assertEquals(1, result.size());
        assertEquals("Overdue task", result.get(0).getTitle());
        verify(taskRepository).findOverdueTasks(any());
    }
}

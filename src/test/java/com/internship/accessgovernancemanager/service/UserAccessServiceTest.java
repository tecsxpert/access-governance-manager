package com.internship.accessgovernancemanager.service;

import com.internship.accessgovernancemanager.entity.UserAccess;
import com.internship.accessgovernancemanager.exception.ResourceNotFoundException;
import com.internship.accessgovernancemanager.repository.UserAccessRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAccessServiceTest {

    @Mock
    private UserAccessRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserAccessService service;

    @Test
    void registerUser_encodesPasswordAndSendsWelcomeEmail() {
        UserAccess user = new UserAccess();
        user.setUsername("testuser");
        user.setPassword("plainPassword");
        user.setEmail("user@example.com");
        user.setRole("ROLE_USER");

        UserAccess saved = new UserAccess();
        saved.setId(1L);
        saved.setUsername("testuser");
        saved.setPassword("encodedPassword");
        saved.setEmail("user@example.com");
        saved.setRole("ROLE_USER");

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(repository.save(any(UserAccess.class))).thenReturn(saved);

        UserAccess result = service.registerUser(user);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("encodedPassword", result.getPassword());
        verify(passwordEncoder).encode("plainPassword");
        verify(emailService).sendWelcomeEmail(saved);
    }

    @Test
    void getUserById_whenNotFound_throwsResourceNotFoundException() {
        when(repository.findById(42L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> service.getUserById(42L));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void deleteUser_callsRepositoryDeleteById() {
        doNothing().when(repository).deleteById(10L);

        service.deleteUser(10L);

        verify(repository).deleteById(10L);
    }
}

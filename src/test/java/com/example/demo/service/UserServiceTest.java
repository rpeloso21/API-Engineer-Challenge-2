package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Test
    void testRegisterUser_whenUsernameIsUnique_shouldSaveUser() {
        // Arrange
        UserRepository mockRepo = mock(UserRepository.class);
        UserService userService = new UserService(mockRepo);

        User newUser = new User();
        newUser.setUsername("uniqueUser");
        newUser.setPassword("password");

        when(mockRepo.findByUsername("uniqueUser")).thenReturn(null);
        when(mockRepo.save(newUser)).thenReturn(newUser);

        // Act
        User result = userService.registerUser(newUser);

        // Assert
        assertEquals("uniqueUser", result.getUsername());
        verify(mockRepo).save(newUser);
    }

    @Test
    void testRegisterUser_whenUsernameExists_shouldThrow() {
        // Arrange
        UserRepository mockRepo = mock(UserRepository.class);
        UserService userService = new UserService(mockRepo);

        User existingUser = new User();
        existingUser.setUsername("duplicateUser");

        when(mockRepo.findByUsername("duplicateUser")).thenReturn(existingUser);

        // Act & Assert
        User newUser = new User();
        newUser.setUsername("duplicateUser");

        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(newUser);
        });
    }
}

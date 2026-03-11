package com.example.accountservice.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;

import com.example.accountservice.repository.UserRepository;
import com.example.accountservice.entity.User;
import com.example.accountservice.dto.UserRequestDTO;
import com.example.accountservice.dto.UserResponseDTO;
import com.example.accountservice.dto.UserPatchDTO;
import com.example.accountservice.exception.EmailAlreadyExistsException;
import com.example.accountservice.exception.UserNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void CreateUser_Success() {

        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setName("John");
        requestDTO.setEmail("john@test.com");

        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@test.com");

        when(userRepository.findByEmail("john@test.com"))
                .thenReturn(Optional.empty());

        when(userRepository.save(any(User.class)))
                .thenReturn(user);


        UserResponseDTO response = userService.createUser(requestDTO);


        assertNotNull(response);
        assertEquals("John", response.getName());

        verify(userRepository).save(any(User.class));
    }
    @Test
    void CreateUser_EmailAlreadyExists() {


        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setName("John");
        requestDTO.setEmail("john@test.com");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("John");
        existingUser.setEmail("john@test.com");

        when(userRepository.findByEmail("john@test.com"))
                .thenReturn(Optional.of(existingUser));


        assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.createUser(requestDTO);
        });

        verify(userRepository, never()).save(any(User.class));
    }
    @Test
    void GetUser_Success() {


        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setName("Alice");
        user.setEmail("alice@test.com");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));


        UserResponseDTO response = userService.getUser(userId);


        assertNotNull(response);
        assertEquals("Alice", response.getName());

        verify(userRepository).findById(userId);
    }
    @Test
    void GetUser_UserNotFound() {


        Long userId = 99L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());


        assertThrows(UserNotFoundException.class, () -> {
            userService.getUser(userId);
        });

        verify(userRepository).findById(userId);
    }
    @Test
    void DeleteUser() {

        Long userId = 1L;

        userService.deleteUser(userId);

        verify(userRepository).deleteById(userId);
    }
    @Test
    void PatchUser_UpdateName() {

        // Arrange
        Long userId = 1L;

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Old Name");
        existingUser.setEmail("old@test.com");

        UserPatchDTO patchDTO = new UserPatchDTO();
        patchDTO.setName("New Name");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(existingUser));

        when(userRepository.save(any(User.class)))
                .thenReturn(existingUser);

        // Act
        UserResponseDTO response = userService.patchUser(userId, patchDTO);

        // Assert
        assertNotNull(response);
        verify(userRepository).save(existingUser);
    }
}
package com.example.LibraryManagementSystem.Services;

import com.example.LibraryManagementSystem.Entity.User;
import com.example.LibraryManagementSystem.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .userId(1L)  // Not required in DB, but needed for testing
                .name("Hardik Sharma")
                .gender("Male")
                .email("hardik.sharma@example.com")
                .dob(LocalDate.of(2000, 5, 10))
                .phone("9876543210")
                .build();
    }

    @Test
    void testAddUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.addUser(user);

        assertNotNull(savedUser);
        assertEquals("Hardik Sharma", savedUser.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testAddUsers() {
        List<User> users = Arrays.asList(user);
        when(userRepository.saveAll(users)).thenReturn(users);

        List<User> savedUsers = userService.addUsers(users);

        assertFalse(savedUsers.isEmpty());
        assertEquals(1, savedUsers.size());
        verify(userRepository, times(1)).saveAll(users);
    }

    @Test
    void testGetUsers() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);

        List<User> retrievedUsers = userService.getUsers();

        assertFalse(retrievedUsers.isEmpty());
        assertEquals(1, retrievedUsers.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_Found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> retrievedUser = userService.getUser(1L);

        assertTrue(retrievedUser.isPresent());
        assertEquals("Hardik Sharma", retrievedUser.get().getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<User> retrievedUser = userService.getUser(2L);

        assertFalse(retrievedUser.isPresent());
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    void testGetUserByName_Found() {
        when(userRepository.findByName("Hardik Sharma")).thenReturn(Optional.of(user));

        Optional<User> retrievedUser = userService.getUserByName("Hardik Sharma");

        assertTrue(retrievedUser.isPresent());
        assertEquals("Hardik Sharma", retrievedUser.get().getName());
        verify(userRepository, times(1)).findByName("Hardik Sharma");
    }

    @Test
    void testGetUserByGender() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findAllByGender("Male")).thenReturn(users);

        List<User> retrievedUsers = userService.getUsersByGender("Male");

        assertFalse(retrievedUsers.isEmpty());
        assertEquals(1, retrievedUsers.size());
        verify(userRepository, times(1)).findAllByGender("Male");
    }

    @Test
    void testUpdateUser_Success() {
        when(userRepository.updateUser(
                anyString(), anyString(), any(LocalDate.class),
                anyString(), anyString(), anyLong()
        )).thenReturn(1);

        assertDoesNotThrow(() -> userService.updateUser(user, 1L));

        verify(userRepository, times(1)).updateUser(
                eq(user.getName()), eq(user.getEmail()), eq(user.getDob()),
                eq(user.getGender()), eq(user.getPhone()), eq(1L)
        );
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userRepository.updateUser(
                anyString(), anyString(), any(LocalDate.class),
                anyString(), anyString(), anyLong()
        )).thenReturn(0);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.updateUser(user, 1L));
        assertEquals("404 NOT_FOUND \"User not found or no changes made!\"", exception.getMessage());

        verify(userRepository, times(1)).updateUser(
                eq(user.getName()), eq(user.getEmail()), eq(user.getDob()),
                eq(user.getGender()), eq(user.getPhone()), eq(1L)
        );
    }
}

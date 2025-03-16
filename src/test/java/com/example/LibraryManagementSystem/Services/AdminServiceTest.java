package com.example.LibraryManagementSystem.Services;

import com.example.LibraryManagementSystem.Entity.Admin;
import com.example.LibraryManagementSystem.Repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminService adminService;

    private Admin admin;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        admin = Admin.builder()
                .adminId(1L)
                .adminName("ShardulAdmin")
                .adminPass(passwordEncoder.encode("securePassword"))
                .build();
    }

    @Test
    void testAdminLogin_Success() {
        when(adminRepository.findByAdminName("ShardulAdmin")).thenReturn(admin);

        ResponseEntity<String> response = adminService.adminLogin("ShardulAdmin", "securePassword");

        assertEquals("Login Successfull", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testAdminLogin_WrongPassword() {
        when(adminRepository.findByAdminName("ShardulAdmin")).thenReturn(admin);

        ResponseEntity<String> response = adminService.adminLogin("ShardulAdmin", "wrongPassword");

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Password Mismatch", response.getBody());
    }

    @Test
    void testAdminLogin_AdminNotFound() {
        when(adminRepository.findByAdminName("NonExistentAdmin")).thenReturn(null);

        ResponseEntity<String> response = adminService.adminLogin("NonExistentAdmin", "anyPassword");

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Username or Password Incorrect", response.getBody());
    }

    @Test
    void testAdminSignup_Success() {
        when(adminRepository.findByAdminName("NewAdmin")).thenReturn(null);
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        Admin savedAdmin = adminService.adminSignup("NewAdmin", "newSecurePass");

        assertNotNull(savedAdmin);
        verify(adminRepository, times(1)).save(any(Admin.class));
    }

    @Test
    void testAdminSignup_DuplicateAdmin() {
        when(adminRepository.findByAdminName("ShardulAdmin")).thenReturn(admin);

        assertThrows(ResponseStatusException.class, () -> {
            adminService.adminSignup("ShardulAdmin", "newPassword");
        });
    }

    @Test
    void testAdminSignup_EmptyPassword() {
        assertThrows(ResponseStatusException.class, () -> {
            adminService.adminSignup("NewAdmin", "");
        });
    }

    @Test
    void testGetAdmins() {
        List<Admin> admins = Arrays.asList(admin);
        when(adminRepository.findAll()).thenReturn(admins);

        List<Admin> retrievedAdmins = adminService.getAdmins();

        assertFalse(retrievedAdmins.isEmpty());
        assertEquals(1, retrievedAdmins.size());
        verify(adminRepository, times(1)).findAll();
    }

    @Test
    void testDeleteAdmin() {
        doNothing().when(adminRepository).deleteById(1L);

        adminService.deleteAdmin(1L);

        verify(adminRepository, times(1)).deleteById(1L);
    }
}

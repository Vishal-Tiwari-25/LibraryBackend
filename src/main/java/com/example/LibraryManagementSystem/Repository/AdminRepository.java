package com.example.LibraryManagementSystem.Repository;

import com.example.LibraryManagementSystem.Entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {
    Admin findByAdminName(String adminName);
}

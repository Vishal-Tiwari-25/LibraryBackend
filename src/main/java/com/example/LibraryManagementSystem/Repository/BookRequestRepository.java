package com.example.LibraryManagementSystem.Repository;

import com.example.LibraryManagementSystem.Entity.BookRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRequestRepository extends JpaRepository<BookRequest,Long> {
}

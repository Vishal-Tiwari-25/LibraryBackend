package com.example.LibraryManagementSystem.Repository;

import com.example.LibraryManagementSystem.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DashBoardRepository extends JpaRepository<User, Long> {

    @Query("SELECT COUNT(u) FROM User u")
    Long getTotalUsers();

    @Query("SELECT COUNT(b) FROM Book b")
    Long getTotalBooks();

    @Query("SELECT COUNT(bi) FROM BookIssued bi")
    Long getTotalIssuedBooks();

    @Query("SELECT b.genre, COUNT(b) FROM Book b GROUP BY b.genre")
    List<Object[]> getBookCountByGenre();
}


package com.example.LibraryManagementSystem.Repository;

import com.example.LibraryManagementSystem.Entity.BookIssued;
import com.example.LibraryManagementSystem.Entity.BookIssuedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookIssuedRepository extends JpaRepository<BookIssued, BookIssuedId> {

    @Query("SELECT b FROM BookIssued b WHERE b.bookIssuedId.userId = :userId")
    List<BookIssued> findAllByUserId(@Param("userId") Long userId);

//    @Query("SELECT b FROM BookIssued b WHERE b.bookIssued.bookId = :bookId AND b.bookIssued.userId = :userId")
//    BookIssued findByBookIdAndUserId(@Param("bookId") Long bookId, @Param("userId") Long userId);
//@Query("SELECT b FROM BookIssued b WHERE b.bookIssuedId = :bookIssuedId")
//Optional<BookIssued> findByBookIssuedId(@Param("bookIssuedId") BookIssuedId bookIssuedId);

    //Optional<BookIssued> findByBookIssuedId(BookIssuedId bookIssuedId);
//@Query("SELECT b FROM BookIssued b WHERE b.bookIssuedId.userId = :userId AND b.bookIssuedId.bookId = :bookId")
//Optional<BookIssued> findByBookIssuedId(@Param("userId") Long userId, @Param("bookId") Long bookId);

    @Query("SELECT b FROM BookIssued b WHERE b.bookIssuedId.bookId=:bookId")
    List<BookIssued> findAllByBookId(@Param("bookId") Long bookId);

    @Query("SELECT b FROM BookIssued b WHERE b.dueDate < :currentDate")
    List<BookIssued> findOverdueBooks(@Param("currentDate") LocalDateTime currentDate);

    @Query("SELECT MONTH(b.issueDate), COUNT(b) FROM BookIssued b GROUP BY MONTH(b.issueDate)")
    List<Object[]> getBookIssueCountByMonth();
}

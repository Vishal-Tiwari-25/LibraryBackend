package com.example.LibraryManagementSystem.Repository;

import com.example.LibraryManagementSystem.DTO.DashboardIssuedBookDTO;
import com.example.LibraryManagementSystem.Entity.BookIssued;
import com.example.LibraryManagementSystem.Entity.BookIssuedId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;

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

//    @Query("SELECT new com.example.LibraryManagementSystem.DTO.DashboardIssuedBookDTO(" +
//            "b.user.name, b.book.bookTitle, b.dueDate) " +
//            "FROM BookIssued b " +
//            "ORDER BY b.dueDate ASC")
//    List<DashboardIssuedBookDTO> findTop5ByDueDate(PageRequest pageable);
//@Query("SELECT new com.example.LibraryManagementSystem.DTO.DashboardIssuedBookDTO(" +
//        "b.user.name, b.book.bookTitle, b.dueDate) " +
//        "FROM BookIssued b " +
//        "ORDER BY b.dueDate ASC")
//List<DashboardIssuedBookDTO> findTop5ByDueDate(Pageable pageable);

}


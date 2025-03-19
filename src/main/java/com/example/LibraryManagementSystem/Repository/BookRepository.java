package com.example.LibraryManagementSystem.Repository;

import com.example.LibraryManagementSystem.Entity.Book;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    Optional<Book> findByBookTitle(String bookTitle);
    Optional<List<Book>> findAllByAuthorName(String authorName);
    List<Book> findAllByOrderByBookIdAsc();

    Optional<List<Book>> findAllByGenre(String genre);
    @Transactional
    @Modifying
    @Query(
            value = "UPDATE Book b SET b.quantity=:quantity where b.bookId=:bookId"
    )
    void updateBookQuantity(@Param("quantity") Integer quantity,@Param("bookId") Long bookId);

    @Transactional
    @Modifying
    @Query(
            value = "UPDATE Book b SET b.ratings=:ratings where b.bookId=:bookId"
    )
    void updateBookRatings(@Param("ratings") Float ratings,@Param("bookId") Long bookId);

    boolean existsByBookTitle(String bookTitle);

    @Query("SELECT b.quantity FROM Book b WHERE b.bookId = :bookId")
    int findQuantityById(@Param("bookId") Long bookId);

//    @Query("SELECT b.genre, COUNT(b) FROM Book b GROUP BY b.genre")
//    List<Object[]> getBookCountByGenre();


//    @Query("UPDATE Book b SET b.quantity=:quantity where b.bookId=:bookId")
//    void decrementBookQuantityById(@Param("quantity") Integer quantity,@Param("bookId") Long bookId);
}

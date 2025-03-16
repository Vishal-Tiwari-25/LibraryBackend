package com.example.LibraryManagementSystem.Services;

import com.example.LibraryManagementSystem.Entity.Book;
import com.example.LibraryManagementSystem.Entity.BookIssued;
import com.example.LibraryManagementSystem.Entity.BookIssuedId;
import com.example.LibraryManagementSystem.Entity.Fine;
import com.example.LibraryManagementSystem.Repository.BookIssuedRepository;
import com.example.LibraryManagementSystem.Repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookReturnServiceTest {

    @Mock
    private FineService fineService;

    @Mock
    private BookIssuedRepository bookIssuedRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookReturnService bookReturnService;

    private Book book;
    private BookIssued bookIssued;
    private BookIssuedId bookIssuedId;
    private Fine fine;

    @BeforeEach
    void setUp() {
        book = Book.builder()
                .bookId(1L)
                .bookTitle("The Alchemist")
                .authorName("Paulo Coelho")
                .edition("1st")
                .genre("Fiction")
                .quantity(5)
                .build();

        bookIssuedId = BookIssuedId.builder()
                .userId(1L)
                .bookId(book.getBookId())
                .build();

        bookIssued = BookIssued.builder()
                .bookIssuedId(bookIssuedId)
                .book(book)
                .issueDate(java.time.LocalDateTime.now().minusDays(10)) // Simulate issued book
                .dueDate(java.time.LocalDateTime.now().minusDays(5)) // Overdue book
                .build();

        fine = Fine.builder()
                .fineId(1L)
                .amount(10.0)
                .reason("Late return")
                .isPaid(false)
                .user(bookIssued.getUser())  // Properly assigning user
                .book(book)                 // Properly assigning book
                .build();
    }

    @Test
    void testReturnBook_Success() {
        when(bookIssuedRepository.findById(bookIssuedId)).thenReturn(Optional.of(bookIssued));
        when(fineService.getUnpaidFines(1L)).thenReturn(Collections.emptyList());

        ResponseEntity<Object> response = bookReturnService.returnBook(1L, 1L, false, 0.0, null);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Book returned successfully!", response.getBody());
        verify(bookRepository, times(1)).save(book);
        verify(bookIssuedRepository, times(1)).delete(bookIssued);
    }

    @Test
    void testReturnBook_BookNotIssued() {
        when(bookIssuedRepository.findById(bookIssuedId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                bookReturnService.returnBook(1L, 1L, false, 0.0, null));

        assertEquals(404, exception.getStatusCode().value());
        assertEquals("Issued book record not found 1", exception.getReason());
    }

    @Test
    void testReturnBook_WithUnpaidFines() {
        when(bookIssuedRepository.findById(bookIssuedId)).thenReturn(Optional.of(bookIssued));
        when(fineService.getUnpaidFines(1L)).thenReturn(List.of(fine));

        ResponseEntity<Object> response = bookReturnService.returnBook(1L, 1L, false, 0.0, null);

        assertEquals(402, response.getStatusCode().value());
        assertTrue(response.getBody().toString().contains("User has unpaid fines"));
    }

    @Test
    void testReturnBook_BookDamaged() {
        when(bookIssuedRepository.findById(bookIssuedId)).thenReturn(Optional.of(bookIssued));
        when(fineService.getUnpaidFines(1L)).thenReturn(Collections.emptyList());

        ResponseEntity<Object> response = bookReturnService.returnBook(1L, 1L, true, 15.0, "Torn pages");

        assertEquals(200, response.getStatusCode().value());
        verify(fineService, times(1)).addDamageFine(1L, 1L, 15.0, "Torn pages");
    }
}

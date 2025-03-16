package com.example.LibraryManagementSystem.Services;

import com.example.LibraryManagementSystem.Entity.Book;
import com.example.LibraryManagementSystem.Repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        book = Book.builder()
                .bookTitle("The Secret of the Druids")
                .authorName("Christopher C. Doyle")
                .edition("1st")
                .ratings(0.0f)
                .genre("Mythological Thriller")
                .quantity(10)
                .totalRatingsCount(0L)
                .ratingsSum(0.0f)
                .build();
    }

    @Test
    void testAddBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book savedBook = bookService.addBook(book);

        assertNotNull(savedBook);
        assertEquals("The Secret of the Druids", savedBook.getBookTitle());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testGetBooks() {
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> retrievedBooks = bookService.getBooks();

        assertFalse(retrievedBooks.isEmpty());
        assertEquals(1, retrievedBooks.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testGetBookById_Found() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<Book> retrievedBook = bookService.getBookById(1L);

        assertTrue(retrievedBook.isPresent());
        assertEquals("The Secret of the Druids", retrievedBook.get().getBookTitle());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBookById_NotFound() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            bookService.getBookById(2L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(bookRepository, times(1)).findById(2L);
    }

    @Test
    void testUpdateBookQuantity() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        bookService.updateBookQuantity(1L, 20);

        assertEquals(30, book.getQuantity()); // 10 (old) + 20 (new) = 30
        verify(bookRepository, times(1)).save(book);
    }
}

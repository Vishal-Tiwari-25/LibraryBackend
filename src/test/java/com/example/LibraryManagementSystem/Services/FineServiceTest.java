package com.example.LibraryManagementSystem.Services;

import com.example.LibraryManagementSystem.Entity.*;
import com.example.LibraryManagementSystem.Repository.BookIssuedRepository;
import com.example.LibraryManagementSystem.Repository.BookRepository;
import com.example.LibraryManagementSystem.Repository.FineRepository;
import com.example.LibraryManagementSystem.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FineServiceTest {

    @Mock
    private FineRepository fineRepository;

    @Mock
    private BookIssuedRepository bookIssuedRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private FineService fineService;

    private User user;
    private Book book;
    private BookIssued bookIssued;
    private Fine fine;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .userId(1L)
                .name("Shardul Kedar")
                .email("shardul@example.com")
                .gender("Male")
                .dob(java.time.LocalDate.of(2000, 1, 1))
                .phone("9876543210")
                .build();

        book = Book.builder()
                .bookId(1L)
                .bookTitle("The Alchemist")
                .authorName("Paulo Coelho")
                .edition("1st")
                .genre("Fiction")
                .quantity(5)
                .build();

        bookIssued = BookIssued.builder()
                .bookIssuedId(new BookIssuedId(user.getUserId(), book.getBookId()))
                .user(user)
                .book(book)
                .issueDate(LocalDateTime.now().minusDays(10))  // 10 days ago
                .dueDate(LocalDateTime.now().minusDays(5))  // 5 days overdue
                .build();

        fine = Fine.builder()
                .fineId(1L)
                .user(user)
                .book(book)
                .amount(50.0)
                .reason("Late return")
                .isPaid(false)
                .build();
    }

    @Test
    void testCalculateFineForSingleBook_Success() {
        when(bookIssuedRepository.findById(any(BookIssuedId.class))).thenReturn(Optional.of(bookIssued));
        when(fineRepository.findByUserIdAndReason(user.getUserId(), "Late return")).thenReturn(null);
        when(fineRepository.save(any(Fine.class))).thenReturn(fine);

        assertDoesNotThrow(() -> fineService.calculateFineForSingleBook(book.getBookId(), user.getUserId()));
        verify(fineRepository, times(1)).save(any(Fine.class));
    }

    @Test
    void testCalculateFineForSingleBook_NoFineNeeded() {
        bookIssued.setDueDate(LocalDateTime.now().plusDays(1)); // Not overdue
        when(bookIssuedRepository.findById(any(BookIssuedId.class))).thenReturn(Optional.of(bookIssued));

        assertDoesNotThrow(() -> fineService.calculateFineForSingleBook(book.getBookId(), user.getUserId()));
        verify(fineRepository, never()).save(any(Fine.class));
    }

    @Test
    void testCalculateFineForSingleBook_NotFound() {
        when(bookIssuedRepository.findById(any(BookIssuedId.class))).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                fineService.calculateFineForSingleBook(book.getBookId(), user.getUserId()));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Issued book record not found 2", exception.getReason());
    }

    @Test
    void testAddDamageFine_Success() {
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(bookRepository.findById(book.getBookId())).thenReturn(Optional.of(book));

        Fine expectedFine = Fine.builder()
                .fineId(1L)
                .user(user)
                .book(book)
                .amount(100.0)
                .reason("Damage/Misuse - Torn pages")
                .isPaid(false)
                .build();

        when(fineRepository.save(any(Fine.class))).thenReturn(expectedFine);

        Fine addedFine = fineService.addDamageFine(user.getUserId(), book.getBookId(), 100.0, "Torn pages");

        assertNotNull(addedFine);
        assertEquals(100.0, addedFine.getAmount()); // Now correctly checks for 100.0
        assertEquals("Damage/Misuse - Torn pages", addedFine.getReason());
        verify(fineRepository, times(1)).save(any(Fine.class));
    }


    @Test
    void testAddDamageFine_UserNotFound() {
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                fineService.addDamageFine(user.getUserId(), book.getBookId(), 100.0, "Torn pages"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());
    }

    @Test
    void testAddDamageFine_BookNotFound() {
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(bookRepository.findById(book.getBookId())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                fineService.addDamageFine(user.getUserId(), book.getBookId(), 100.0, "Torn pages"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Book not found", exception.getReason());
    }

    @Test
    void testGetUnpaidFines() {
        when(fineRepository.findAllByIsPaid(false, user.getUserId())).thenReturn(Collections.singletonList(fine));

        List<Fine> unpaidFines = fineService.getUnpaidFines(user.getUserId());

        assertFalse(unpaidFines.isEmpty());
        assertEquals(1, unpaidFines.size());
        verify(fineRepository, times(1)).findAllByIsPaid(false, user.getUserId());
    }

    @Test
    void testMarkFineAsPaid_Success() {
        when(fineRepository.findById(fine.getFineId())).thenReturn(Optional.of(fine));

        fineService.markFineAsPaid(fine.getFineId());

        assertTrue(fine.isPaid());
        verify(fineRepository, times(1)).save(fine);
    }

    @Test
    void testMarkFineAsPaid_FineNotFound() {
        when(fineRepository.findById(fine.getFineId())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                fineService.markFineAsPaid(fine.getFineId()));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Fine record not found", exception.getReason());
    }
}

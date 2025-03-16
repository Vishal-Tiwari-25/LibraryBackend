//package com.example.LibraryManagementSystem.Services;
//
//import com.example.LibraryManagementSystem.Entity.Book;
//import com.example.LibraryManagementSystem.Entity.BookIssued;
//import com.example.LibraryManagementSystem.Entity.BookIssuedId;
//import com.example.LibraryManagementSystem.Entity.User;
//import com.example.LibraryManagementSystem.Repository.BookIssuedRepository;
//import com.example.LibraryManagementSystem.Repository.BookRepository;
//import com.example.LibraryManagementSystem.Repository.FineRepository;
//import com.example.LibraryManagementSystem.Repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class BookIssuedServiceTest {
//
//    @Mock
//    private BookIssuedRepository bookIssuedRepository;
//
//    @Mock
//    private BookRepository bookRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private FineRepository fineRepository;
//
//    @InjectMocks
//    private BookIssuedService bookIssuedService;
//
//    private User user;
//    private Book book;
//    private BookIssued bookIssued;
//    private BookIssuedId bookIssuedId;
//
//    @BeforeEach
//    void setUp() {
//        user = User.builder()
//                .userId(1L)
//                .name("Shardul Kedar")
//                .email("shardul@example.com")
//                .gender("Male")
//                .dob(java.time.LocalDate.of(2000, 1, 1))
//                .phone("9876543210")
//                .build();
//
//        book = Book.builder()
//                .bookId(1L)
//                .bookTitle("The Alchemist")
//                .authorName("Paulo Coelho")
//                .edition("1st")
//                .genre("Fiction")
//                .quantity(5) // Initially 5 copies available
//                .build();
//
//        bookIssuedId = BookIssuedId.builder()
//                .userId(user.getUserId())
//                .bookId(book.getBookId())
//                .build();
//
//        bookIssued = BookIssued.builder()
//                .bookIssuedId(bookIssuedId)
//                .user(user)
//                .book(book)
//                .issueDate(LocalDateTime.now().minusDays(10)) // Simulate an issued book
//                .dueDate(LocalDateTime.now().minusDays(5)) // Overdue book
//                .build();
//
//        // ✅ Always Mock fineRepository so it returns expected values
//        when(fineRepository.existsByUserIdAndIsPaid(anyLong(), anyBoolean())).thenReturn(false);
//    }
//
//    @Test
//    void testIssueBook_BookOutOfStock() {
//        book.setQuantity(0);
//        when(bookRepository.findById(book.getBookId())).thenReturn(Optional.of(book));
//        when(fineRepository.existsByUserIdAndIsPaid(anyLong(), anyBoolean())).thenReturn(false); // ✅ Add this
//
//        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
//                bookIssuedService.issueBook(user.getUserId(), book.getBookId()));
//
//        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
//        assertEquals("Book is out of stock", exception.getReason());
//    }
//
//    @Test
//    void testIssueBook_Success() {
//        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
//        when(bookRepository.findById(book.getBookId())).thenReturn(Optional.of(book));
//        when(bookIssuedRepository.findById(bookIssuedId)).thenReturn(Optional.empty());
//        when(fineRepository.existsByUserIdAndIsPaid(user.getUserId(), false)).thenReturn(false);
//        when(bookIssuedRepository.save(any(BookIssued.class))).thenReturn(bookIssued);
//
//        BookIssued issuedBook = bookIssuedService.issueBook(user.getUserId(), book.getBookId());
//
//        assertNotNull(issuedBook);
//        assertEquals(user.getUserId(), issuedBook.getUser().getUserId());
//        assertEquals(book.getBookId(), issuedBook.getBook().getBookId());
//        verify(bookRepository, times(1)).save(book);
//        verify(bookIssuedRepository, times(1)).save(any(BookIssued.class));
//    }
//
//    @Test
//    void testIssueBook_UserHasUnpaidFine() {
//        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
//        when(bookRepository.findById(book.getBookId())).thenReturn(Optional.of(book));
//        when(fineRepository.existsByUserIdAndIsPaid(user.getUserId(), false)).thenReturn(true);
//
//        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
//                bookIssuedService.issueBook(user.getUserId(), book.getBookId()));
//
//        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
//        assertEquals("User has an unpaid fine. Cannot issue a new book.", exception.getReason());
//    }
//
//    @Test
//    void testIssueBook_ExtendDueDate() {
//        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
//        when(bookRepository.findById(book.getBookId())).thenReturn(Optional.of(book));
//        when(bookIssuedRepository.findById(bookIssuedId)).thenReturn(Optional.of(bookIssued));
//        when(fineRepository.existsByUserIdAndIsPaid(user.getUserId(), false)).thenReturn(false);
//        when(bookIssuedRepository.save(any(BookIssued.class))).thenReturn(bookIssued);
//
//        BookIssued issuedBook = bookIssuedService.issueBook(user.getUserId(), book.getBookId());
//
//        assertNotNull(issuedBook);
//        assertEquals(user.getUserId(), issuedBook.getUser().getUserId());
//        assertEquals(book.getBookId(), issuedBook.getBook().getBookId());
//        assertTrue(issuedBook.getDueDate().isAfter(LocalDateTime.now())); // Due date should extend
//    }
//
//    @Test
//    void testGetIssuedBooks() {
//        when(bookIssuedRepository.findAll()).thenReturn(Collections.singletonList(bookIssued));
//
//        List<BookIssued> issuedBooks = bookIssuedService.getIssuedBooks();
//
//        assertFalse(issuedBooks.isEmpty());
//        assertEquals(1, issuedBooks.size());
//        verify(bookIssuedRepository, times(1)).findAll();
//    }
//
//    @Test
//    void testGetIssuedBooksByUserId() {
//        when(bookIssuedRepository.findAllByUserId(user.getUserId())).thenReturn(Collections.singletonList(bookIssued));
//
//        List<BookIssued> issuedBooks = bookIssuedService.getIssuedBooksByUserId(user.getUserId());
//
//        assertFalse(issuedBooks.isEmpty());
//        assertEquals(1, issuedBooks.size());
//        verify(bookIssuedRepository, times(1)).findAllByUserId(user.getUserId());
//    }
//
//    @Test
//    void testGetIssuedBooksByBookId() {
//        when(bookIssuedRepository.findAllByBookId(book.getBookId())).thenReturn(Collections.singletonList(bookIssued));
//
//        List<BookIssued> issuedBooks = bookIssuedService.getIssuedBooksByBookId(book.getBookId());
//
//        assertFalse(issuedBooks.isEmpty());
//        assertEquals(1, issuedBooks.size());
//        verify(bookIssuedRepository, times(1)).findAllByBookId(book.getBookId());
//    }
//
//    @Test
//    void testGetIssuedBooksByDueDate() {
//        when(bookIssuedRepository.findOverdueBooks(any(LocalDateTime.class)))
//                .thenReturn(Collections.singletonList(bookIssued));
//
//        List<BookIssued> overdueBooks = bookIssuedService.getIssuedBooksByDueDate();
//
//        assertFalse(overdueBooks.isEmpty());
//        assertEquals(1, overdueBooks.size());
//        verify(bookIssuedRepository, times(1)).findOverdueBooks(any(LocalDateTime.class));
//    }
//}

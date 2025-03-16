package com.example.LibraryManagementSystem.Services;

import com.example.LibraryManagementSystem.Entity.Book;
import com.example.LibraryManagementSystem.Entity.BookIssued;
import com.example.LibraryManagementSystem.Entity.BookIssuedId;
import com.example.LibraryManagementSystem.Entity.User;
import com.example.LibraryManagementSystem.Repository.BookIssuedRepository;
import com.example.LibraryManagementSystem.Repository.BookRepository;
import com.example.LibraryManagementSystem.Repository.FineRepository;
import com.example.LibraryManagementSystem.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookIssuedService {
    @Autowired
    private BookIssuedRepository bookIssuedRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FineRepository fineRepository;


    private final Map<String, Integer> monthlyIssueCount = new HashMap<>();


    public BookIssuedService(BookIssuedRepository bookIssuedRepository) {
        this.bookIssuedRepository = bookIssuedRepository;
        initializeMonthlyCounts();
    }

    private void initializeMonthlyCounts() {
        // Initialize all months to 0
        for (Month month : Month.values()) {
            monthlyIssueCount.putIfAbsent(month.name(), 0);
        }

        // Fetch count of issued books per month from DB
        List<Object[]> results = bookIssuedRepository.getBookIssueCountByMonth();

        if (results != null) {
            results.forEach(obj -> {
                int monthNumber = (Integer) obj[0]; // Month number (1 = Jan, 2 = Feb, ...)
                int count = ((Number) obj[1]).intValue();
                String monthName = Month.of(monthNumber).name();
                monthlyIssueCount.put(monthName, count);
            });
        }
    }

    public void incrementBookIssueCount(LocalDateTime issueDate) {
        String month = issueDate.getMonth().name(); // e.g., "MARCH"
        monthlyIssueCount.put(month, monthlyIssueCount.get(month) + 1);
    }

    public int getMonthlyIssueCount(String month) {
        return monthlyIssueCount.getOrDefault(month, 0);
    }
    @Transactional
    public BookIssued issueBook(Long userId, Long bookId) {
        boolean hasUnpaidFine = fineRepository.existsByUserIdAndIsPaid(userId, false); // Unpaid fine exists
        if (hasUnpaidFine) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User has an unpaid fine. Cannot issue a new book.");
        }
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book doesn't exist"));
        //bookRepository.findQuantityById(bookId)==0
        if (book.getQuantity() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book is out of stock");
            //add notification feature;
        }
        BookIssuedId id = BookIssuedId.builder().userId(userId)
                .bookId(bookId).build();
        Optional<BookIssued> bookIssued = bookIssuedRepository.findById(id);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (bookIssued.isPresent()) {
            //add fine check as well.

            //if fine is paid
            BookIssued existingBook = bookIssued.get();
            existingBook.setDueDate(existingBook.getDueDate().plusDays(5));
            return bookIssuedRepository.save(existingBook);
        } else {
            LocalDateTime issueDate = LocalDateTime.now();
            LocalDateTime dueDate = issueDate.plusDays(5);
            BookIssued issueBook = BookIssued.builder()
                    .issueDate(issueDate).book(book).user(user).dueDate(dueDate).bookIssuedId(id).build();
            incrementBookIssueCount(issueDate);
            book.setQuantity(book.getQuantity() - 1);
            bookRepository.save(book);
            return bookIssuedRepository.save(issueBook);
        }
    }
    public BookIssued issueBookTemp(Long userId,Long bookId){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book doesn't exist"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        BookIssuedId id=BookIssuedId.builder()
                .userId(userId).bookId(bookId).build();
        LocalDateTime issueDate = LocalDateTime.now().minusDays(5);
        LocalDateTime dueDate = LocalDateTime.now().minusDays(1);
        BookIssued issueBook = BookIssued.builder()
                .issueDate(issueDate).book(book).user(user).dueDate(dueDate).bookIssuedId(id).build();
        incrementBookIssueCount(issueDate);
        book.setQuantity(book.getQuantity() - 1);
        bookRepository.save(book);
        return bookIssuedRepository.save(issueBook);
    }

    //getbooks method
    public List<BookIssued> getIssuedBooks() {
        return bookIssuedRepository.findAll();
    }

    //getbooks by userid
    public List<BookIssued> getIssuedBooksByUserId(Long userId) {
        List<BookIssued> issuedBooks = bookIssuedRepository.findAllByUserId(userId);
        if (!issuedBooks.isEmpty()) {
            return issuedBooks;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User hasn't issued any Book");
        }
    }

    public List<BookIssued> getIssuedBooksByBookId(Long bookId) {
        List<BookIssued> issuedBooks = bookIssuedRepository.findAllByBookId(bookId);
        if (!issuedBooks.isEmpty()) {
            return issuedBooks;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book hasn't been issued by any User");
        }
    }
    //get data where dueDate is surpassed directly from the database;
    public List<BookIssued> getIssuedBooksByDueDate() {
        return bookIssuedRepository.findOverdueBooks(LocalDateTime.now());
    }
}
//get data where dueDate has surpassed
//    public List<BookIssued> getIssuedBooksByDueDate(){
//        List<BookIssued> delayedBooks=new ArrayList<>();
//        List<BookIssued> issuedBooks=bookIssuedRepository.findAll();
//        for(BookIssued bi:issuedBooks){
//            if(bi.getDueDate().isBefore(LocalDateTime.now())){
//                delayedBooks.add(bi);
//            }
//}  return delayedBooks;
//        } not good idea
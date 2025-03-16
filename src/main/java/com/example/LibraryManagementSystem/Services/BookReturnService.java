package com.example.LibraryManagementSystem.Services;

import com.example.LibraryManagementSystem.Entity.Book;
import com.example.LibraryManagementSystem.Entity.BookIssued;
import com.example.LibraryManagementSystem.Entity.BookIssuedId;
import com.example.LibraryManagementSystem.Entity.Fine;
import com.example.LibraryManagementSystem.Repository.BookIssuedRepository;
import com.example.LibraryManagementSystem.Repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookReturnService {
    @Autowired
    private FineService fineService;
    @Autowired
    private BookIssuedRepository bookIssuedRepository;
    @Autowired
    private BookRepository bookRepository;

    public BookReturnService(FineService fineService, BookIssuedRepository bookIssuedRepository, BookRepository bookRepository) {
        this.fineService = fineService;
        this.bookIssuedRepository = bookIssuedRepository;
        this.bookRepository = bookRepository;
    }

    /*bookReturnRequest.getBookId(),
                    bookReturnRequest.getUserId(),
                    bookReturnRequest.isDamaged(),
                    bookReturnRequest.getDamageFineAmount(),
                    bookReturnRequest.getDamageReason()*/
    @Transactional
    public ResponseEntity<Object> returnBook(Long bookId, Long userId, boolean isDamaged,
                                             double damageFineAmount, String damageReason) {
         BookIssuedId issuedId = BookIssuedId.builder().userId(userId).bookId(bookId).build();
        // 🔹 Debugging Statements
        System.out.println("Searching for BookIssued entry: " + issuedId);

        bookIssuedRepository.findAll().forEach(entry -> {
            System.out.println("DB Entry Found: " + entry.getBookIssuedId());
        });
        // Fetch issued book entry
        BookIssued bookIssued = bookIssuedRepository.findById(issuedId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Issued book record not found 1"));

        // 🔹 1. Calculate late return fine (if applicable)
        fineService.calculateFineForSingleBook(bookId, userId);

        // 🔹 2. Check if admin marked the book as damaged & apply fine
        if (isDamaged) {
            fineService.addDamageFine(userId, bookId, damageFineAmount, damageReason);
        }

        // 🔹 3. Check for unpaid fines
        List<Fine> unpaidFines = fineService.getUnpaidFines(userId);
        if (!unpaidFines.isEmpty()) {
            double totalFine = unpaidFines.stream().mapToDouble(Fine::getAmount).sum();

            // Prepare response for unpaid fines
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User has unpaid fines. Redirecting to payment page.");
            response.put("totalFine", totalFine);
//            response.put("fineDetails", unpaidFines);
            response.put("paymentUrl", "/fines/unpaid/" + userId);
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(response);
        }

        // 🔹 4. Increment book quantity & remove bookIssued entry
        Book book = bookIssued.getBook();
        book.setQuantity(book.getQuantity() + 1);
        bookRepository.save(book);
        bookIssuedRepository.delete(bookIssued);

        return ResponseEntity.ok("Book returned successfully!");
    }
}

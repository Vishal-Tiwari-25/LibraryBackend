package com.example.LibraryManagementSystem.Services;

import com.example.LibraryManagementSystem.Entity.*;
import com.example.LibraryManagementSystem.Repository.BookIssuedRepository;
import com.example.LibraryManagementSystem.Repository.BookRepository;
import com.example.LibraryManagementSystem.Repository.FineRepository;
import com.example.LibraryManagementSystem.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FineService {

    @Autowired
    private FineRepository fineRepository;

    @Autowired
    private BookIssuedRepository bookIssuedRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    private final double FINE_PER_DAY = 10.0; // Fine per day for late return

    // 🔹 Auto-Calculate Late Return Fines (Runs Daily at Midnight)
   // @Scheduled(cron = "0 0 0 * * ?")
    @Scheduled(fixedRate = 120000)
    public void calculateLateReturnFines() {
        LocalDateTime now = LocalDateTime.now();
        List<BookIssued> overdueBooks = bookIssuedRepository.findOverdueBooks(now);

        for (BookIssued bookIssued : overdueBooks) {
            applyFine(bookIssued);
        }
    }

    // 🔹 Calculate Fine for a Specific Book during Return
    public void calculateFineForSingleBook(Long bookId, Long userId) {
        BookIssued bookIssued = bookIssuedRepository.findById(new BookIssuedId(userId, bookId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Issued book record not found 2"));
        applyFine(bookIssued);
    }

    // 🔹 Apply Fine Logic (Reused in both Scheduled Task & Single Book Return)
    private void applyFine(BookIssued bookIssued) {
        LocalDateTime now = LocalDateTime.now();
        long daysLate = Duration.between(bookIssued.getDueDate(), now).toDays();
        double fineAmount = daysLate * FINE_PER_DAY;

            // Check if a fine already exists for late return
            Fine existingFine = fineRepository.findByUserIdAndReason(bookIssued.getUser().getUserId(), "Late return");
           if(daysLate>0){
            if (existingFine == null) {
                Fine fine = Fine.builder()
                        .user(bookIssued.getUser())
                        .amount(fineAmount)
                        .book(bookIssued.getBook())
                        .reason("Late return")
                        .isPaid(false)
                        .build();
                fineRepository.save(fine);
                System.out.println("2 min run new fine added");
            } else {
                // 🔹 Fix: Accumulate the fine instead of overwriting
                existingFine.setAmount(existingFine.getAmount() + fineAmount);
                fineRepository.save(existingFine);
                System.out.println("2 mmin run no new fine added");
            }
    }
    }

    // 🔹 Manually Add Fine for Damaged/Misused Books
    public Fine addDamageFine(Long userId, Long bookId, double fineAmount, String reason) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        Fine fine = Fine.builder()
                .user(user)
                .book(book)
                .amount(fineAmount)
                .reason("Damage/Misuse - " + reason)
                .isPaid(false)
                .build();
//        System.out.println(fine);
        return fineRepository.save(fine);
    }

    // 🔹 Fetch Unpaid Fines for a User
    public List<Fine> getUnpaidFines(Long userId) {
        return fineRepository.findAllByIsPaidAndUser_UserId(false, userId);
    }
    public void markFineAsPaid(Long fineId) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fine record not found"));

        fine.setPaid(true);  // ✅ Mark fine as paid
        fineRepository.save(fine);
    }
//    public void payFine(Long userId,Long bookId){
//        Fine fine=fineRepository.findByUserId(userId,bookId);
////                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fine record not found"));
//        fine.setPaid(true);
//        fineRepository.save(fine);
//    }
public void payFine(Long userId, Long bookId) {
    Fine fine = fineRepository.findByUserIdAndBookId(userId, bookId);
    if (fine == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fine record not found");
    }
    fine.setPaid(true);
    fineRepository.save(fine);
}
}

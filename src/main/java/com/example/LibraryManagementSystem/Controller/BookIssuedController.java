package com.example.LibraryManagementSystem.Controller;

import com.example.LibraryManagementSystem.Entity.Book;
import com.example.LibraryManagementSystem.Entity.BookIssued;
import com.example.LibraryManagementSystem.Entity.BookIssuedId;
import com.example.LibraryManagementSystem.Services.BookIssuedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/issueBook")
public class BookIssuedController {
    @Autowired
    private BookIssuedService bookIssuedService;

    @GetMapping("/get-books")
    public ResponseEntity<List<BookIssued>> getBooks(){
        return ResponseEntity.ok(bookIssuedService.getIssuedBooks());
    }
    @GetMapping("/get-books/by-userID/{id}")
    public ResponseEntity<List<BookIssued>> getIssuedBooksByUserId(@PathVariable Long id){
        return ResponseEntity.ok(bookIssuedService.getIssuedBooksByUserId(id));
    }
    @GetMapping("/get-books/by-bookID/{id}")
    public ResponseEntity<List<BookIssued>> getIssuedBooksByBookId(@PathVariable Long id){
        return ResponseEntity.ok(bookIssuedService.getIssuedBooksByBookId(id));
    }
    @GetMapping("/monthly-count/{month}")
    public ResponseEntity<Integer> getMonthlyIssueCount(@PathVariable String month) {
        int count = bookIssuedService.getMonthlyIssueCount(month);
        return ResponseEntity.ok(count);
    }

    @PostMapping("/issue-books")
    public ResponseEntity<BookIssued> issueBook(@RequestBody BookIssuedId id){
        return ResponseEntity.ok(bookIssuedService.issueBook(id.getBookId(),id.getUserId()));
    }
    @PostMapping("/issue-temp-books")
    public ResponseEntity<BookIssued> issueTempBook(@RequestBody BookIssuedId id){
        return ResponseEntity.ok(bookIssuedService.issueBookTemp(id.getUserId(),id.getBookId()));
    }

}

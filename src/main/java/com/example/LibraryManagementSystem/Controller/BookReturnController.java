package com.example.LibraryManagementSystem.Controller;

import com.example.LibraryManagementSystem.DTO.BookReturnRequest;
import com.example.LibraryManagementSystem.Services.BookReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Book")
public class BookReturnController {
    @Autowired
    private BookReturnService bookReturnService;

    @PostMapping("/return-book")
    public ResponseEntity<Object> returnBook(@RequestBody BookReturnRequest bookReturnRequest) {
        return bookReturnService.returnBook(
                bookReturnRequest.getBookId(),
                bookReturnRequest.getUserId(),
                bookReturnRequest.isDamaged(),
                bookReturnRequest.getDamageFineAmount(),
                bookReturnRequest.getDamageReason()
        );
    }

}

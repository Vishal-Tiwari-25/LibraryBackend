package com.example.LibraryManagementSystem.Controller;

import com.example.LibraryManagementSystem.Entity.BookRequest;
import com.example.LibraryManagementSystem.Services.BookRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/BookRequest")
public class BookRequestController {
    @Autowired
    private BookRequestService bookRequestService;
    @PostMapping("/request-book")
    public ResponseEntity<String> requestBook(@RequestBody BookRequest bookRequest){
        bookRequestService.requestBook(bookRequest);
        return ResponseEntity.ok("Book Requested Successfully");
    }
}

package com.example.LibraryManagementSystem.Services;
import com.example.LibraryManagementSystem.Entity.BookRequest;
import com.example.LibraryManagementSystem.Repository.BookRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BookRequestService {
    @Autowired
    private BookRequestRepository bookRequestRepository;

    public void requestBook(BookRequest bookRequest) {
        if (bookRequest.getBookRequestId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "BookRequestId should not be provided for a new request");
        }
        bookRequestRepository.save(bookRequest);
    }
}
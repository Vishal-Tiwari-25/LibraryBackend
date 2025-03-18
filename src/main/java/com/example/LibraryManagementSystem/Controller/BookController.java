package com.example.LibraryManagementSystem.Controller;

import com.example.LibraryManagementSystem.Entity.Book;
import com.example.LibraryManagementSystem.Services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Book")
public class BookController {
    @Autowired
    private BookService bookService;
    //All the different GET Methods
    @GetMapping("/get-books")
    public ResponseEntity<List<Book>> getBooks(){
        return ResponseEntity.ok(bookService.getBooks());
    }
    @GetMapping("/get-book/book-byId/{id}")
    public ResponseEntity<Optional<Book>> getBookById(@PathVariable Long id){
        return ResponseEntity.ok(bookService.getBookById(id));
    }
    @GetMapping("/get-book/book-byTitle/{bookTitle}")
    public ResponseEntity<Optional<Book>> getBookByTitle(@PathVariable String bookTitle){
        return ResponseEntity.ok(bookService.getBookByTitle(bookTitle));
    }
    @GetMapping("/get-book/book-byAuthor/{authorName}")
    public ResponseEntity<Optional<List<Book>>> getBookByAuthorName(@PathVariable String authorName){
        return ResponseEntity.ok(bookService.getBookByAuthorName(authorName));
    }
    @GetMapping("/get-book/book-byGenre/{genre}")
    public ResponseEntity<Optional<List<Book>>> getBookByGenre(@PathVariable String genre){
        return ResponseEntity.ok(bookService.getBookByGenre(genre));
    }
    @GetMapping("/get-book/book-byRatings")
    public ResponseEntity<List<Book>> getBooksByRatings(){
        return ResponseEntity.ok(bookService.getBookByRatings());
    }

    //All the different Post methods
    @PostMapping("/add-book")
    public ResponseEntity<Book> addBooks(@RequestBody Book book){
        return ResponseEntity.ok(bookService.addBook(book));
    }
    @PostMapping("/add-books")
    public ResponseEntity<List<Book>> addBookstemp(@RequestBody List<Book> books){
        return ResponseEntity.ok(bookService.addBooks(books));
    }
    @PutMapping("/update-book/update-quantity/{id}")
    public ResponseEntity<String> updateBookByQuantity(@PathVariable Long id,@RequestBody Integer newQuantity){
        bookService.updateBookQuantity(id,newQuantity);
        return ResponseEntity.ok("Quantity Update Successfull!!");
    }
    @PutMapping("/update-book/update-ratings/{id}")
    public ResponseEntity<String> updateBookRatings(@PathVariable Long id,@RequestBody Float newRatings){
        bookService.updateBookRatings(id,newRatings);
        return ResponseEntity.ok("Updated Ratings Successfully!!");
    }
    @DeleteMapping("/delete-book/by-Id/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id){
        bookService.deleteBookById(id);
        return ResponseEntity.ok("Deleted Successfully!!");
    }

}

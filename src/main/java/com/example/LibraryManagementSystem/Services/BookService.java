package com.example.LibraryManagementSystem.Services;

import com.example.LibraryManagementSystem.Entity.Book;
import com.example.LibraryManagementSystem.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    //Adding book in the DB
    public Book addBook(Book book){
        Optional<Book> bookexist=bookRepository.findByBookTitle(book.getBookTitle());
        if (bookexist.isPresent()) {
            Book existingBook = bookexist.get();
            existingBook.setQuantity(existingBook.getQuantity() + book.getQuantity());
            return bookRepository.save(existingBook); // Updates the existing book
        } else {
            return bookRepository.save(book); //Inserts a new book
        }
    }
    public List<Book> addBooks(List<Book> books) {
        List<Book> savedBooks = new ArrayList<>();

        for (Book book : books) {
            Optional<Book> bookexist=bookRepository.findByBookTitle(book.getBookTitle());
            bookexist.ifPresent(value -> book.setQuantity(book.getQuantity() + value.getQuantity()));
            savedBooks.add(bookRepository.save(book));
        }
        return savedBooks;
    }

    //Updating the book quantity
    public void updateBookQuantity(Long bookId,Integer newQuantity){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book Not Found!"));

        book.setQuantity(book.getQuantity() + newQuantity);
        bookRepository.save(book);  // Save the updated entity
    }
    //Updating the book ratings
    public void updateBookRatings(Long bookId,Float ratings){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book Not Found!"));
        book.setRatingsSum(book.getRatingsSum()+ratings);
        book.setTotalRatingsCount(book.getTotalRatingsCount()+1);
        book.setRatings(book.getRatingsSum()/book.getTotalRatingsCount());
        bookRepository.save(book);
    }

    //All get methods, using different filter values
    public List<Book> getBooks(){
        return bookRepository.findAll();
    }
    public Optional<Book> getBookById(Long id){
        Optional<Book> book=bookRepository.findById(id);
        if(book.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Book doesn't exist");
        }
        return book;
    }
    public Optional<Book> getBookByTitle(String bookTitle){
        Optional<Book> book=bookRepository.findByBookTitle(bookTitle);
        if(book.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Book title doesn't exist");
        }
        return book;
    }
    public Optional<List<Book>> getBookByAuthorName(String authorName){
        Optional<List<Book>> books=bookRepository.findAllByAuthorName(authorName);
        if(books.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Author doesn't exist");
        }
        return books;
    }
    public Optional<List<Book>> getBookByGenre(String genre){
        return bookRepository.findAllByGenre(genre);
    }
    public List<Book> getBookByRatings(){
        return bookRepository.findAll(Sort.by(Sort.Direction.DESC,"ratings"));
    }
}

package com.example.LibraryManagementSystem.Services;

import com.example.LibraryManagementSystem.DTO.DashboardIssuedBookDTO;
import com.example.LibraryManagementSystem.Entity.Book;
import com.example.LibraryManagementSystem.Entity.BookIssued;
import com.example.LibraryManagementSystem.Entity.User;
import com.example.LibraryManagementSystem.Repository.BookIssuedRepository;
import com.example.LibraryManagementSystem.Repository.BookRepository;
import com.example.LibraryManagementSystem.Repository.DashBoardRepository;
import com.example.LibraryManagementSystem.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.*;

@Service
public class DashBoardService {
    private DashBoardRepository dashboardRepository;

    private BookIssuedRepository bookIssuedRepository;
    private UserRepository userRepository;
    private BookRepository bookRepository;
    @Autowired
    public DashBoardService(DashBoardRepository dashboardRepository, BookIssuedRepository bookIssuedRepository,UserRepository userRepository,BookRepository bookRepository) {
        this.bookIssuedRepository = bookIssuedRepository;
        this.dashboardRepository= dashboardRepository;
        this.userRepository=userRepository;
        this.bookRepository=bookRepository;
    }

    public List<DashboardIssuedBookDTO> getUpcomingDueBooks() {
//        return bookIssuedRepository.findTop5ByDueDate(PageRequest.of(0, 5));
        //return bookIssuedRepository.findTop5ByDueDate((Pageable) PageRequest.of(0, 5));
        List<BookIssued> booksIssued=bookIssuedRepository.findAll();
        List<DashboardIssuedBookDTO> dashboardIssuedBookDTOS=new ArrayList<>();
        for(BookIssued bookIss:booksIssued){
            Optional<User> user=userRepository.findById(bookIss.getUser().getUserId());
            Optional<Book> book=bookRepository.findById(bookIss.getBook().getBookId());
            DashboardIssuedBookDTO dash=DashboardIssuedBookDTO.builder()
                    .dueDate(bookIss.getDueDate()).userName(user.get().getName())
                    .bookTitle(book.get().getBookTitle()).build();
            dashboardIssuedBookDTOS.add(dash);
        }
        return dashboardIssuedBookDTOS;
    }

    public Map<String, Long> getDashboardStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalUsers", dashboardRepository.getTotalUsers());
        stats.put("totalBooks", dashboardRepository.getTotalBooks());
        stats.put("totalIssuedBooks", dashboardRepository.getTotalIssuedBooks());
        return stats;
    }
    public Map<String, Long> getGenreCounts() {
        List<Object[]> results = dashboardRepository.getBookCountByGenre();
        Map<String, Long> genreCounts = new HashMap<>();
        for (Object[] row : results) {
            genreCounts.put((String) row[0], (Long) row[1]);
        }
        return genreCounts;
    }

}

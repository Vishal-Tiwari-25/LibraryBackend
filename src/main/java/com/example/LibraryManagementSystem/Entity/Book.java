package com.example.LibraryManagementSystem.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;
    @Column(nullable = false, unique = true)
    private String bookTitle;
    @Column(nullable = false)
    private String authorName;
    @Column(nullable = false)
    private String edition;
   // @Column(nullable = false)
    private Float ratings;
    @Column(nullable = false)
    private String genre;
    @Column(nullable = false)
    private Integer quantity;

    //@Column(nullable = false)
    private Long totalRatingsCount = 0L; // Track number of ratings

    //@Column(nullable = false)
    private Float ratingsSum = 0.0f; // Sum of all ratings


    // Bidirectional mapping (optional)
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Fine> fines;
}

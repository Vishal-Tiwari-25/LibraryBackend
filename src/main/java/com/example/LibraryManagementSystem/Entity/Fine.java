package com.example.LibraryManagementSystem.Entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fineId;

    @Column(nullable = false)
    private Double amount;
    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private boolean isPaid = false;

    // Foreign Key: User (One user can have multiple fines)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Creates user_id column
    @JsonIgnore
    private User user;

    // Foreign Key: Book (One book can be fined multiple times)
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    @JsonBackReference// Creates book_id column
    @JsonIgnore
    private Book book;
}

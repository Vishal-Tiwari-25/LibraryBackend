package com.example.LibraryManagementSystem.Entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.*;
import com.example.LibraryManagementSystem.Entity.BookIssued;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class BookIssuedId implements Serializable {
    private Long userId;
    private Long bookId;
    @Override
    public String toString() {
        return "BookIssuedId(userId=" + userId + ", bookId=" + bookId + ")";
    }
}

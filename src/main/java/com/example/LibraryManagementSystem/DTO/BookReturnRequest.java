package com.example.LibraryManagementSystem.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookReturnRequest {
    private Long bookId;
    private Long userId;
    private boolean isDamaged;
    private double damageFineAmount; // Only applicable if isDamaged = true
    private String damageReason; // Optional admin-provided reason
}

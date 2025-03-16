package com.example.LibraryManagementSystem.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FineRequest {
    private Long userId;
    private Long bookId;
    private double fineAmount;
    private String reason;
}

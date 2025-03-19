package com.example.LibraryManagementSystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardIssuedBookDTO {
    private String userName;
    private String bookTitle;
    private LocalDateTime dueDate;
}

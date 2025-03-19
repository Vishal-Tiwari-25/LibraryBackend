package com.example.LibraryManagementSystem.Controller;

import com.example.LibraryManagementSystem.DTO.DashboardIssuedBookDTO;
import com.example.LibraryManagementSystem.Services.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashBoardController {
    @Autowired
    private DashBoardService dashboardService;

    @GetMapping("/upcoming-due-books")
    public ResponseEntity<List<DashboardIssuedBookDTO>> getUpcomingDueBooks() {
        return ResponseEntity.ok(dashboardService.getUpcomingDueBooks());
    }
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getDashboardStats() {
        return ResponseEntity.ok(dashboardService.getDashboardStats());
    }
    @GetMapping("/genre-counts")
    public ResponseEntity<Map<String, Long>> getGenreCounts() {
        return ResponseEntity.ok(dashboardService.getGenreCounts());
    }

}

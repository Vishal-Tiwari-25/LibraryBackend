package com.example.LibraryManagementSystem.Controller;

import com.example.LibraryManagementSystem.DTO.FineRequest;
import com.example.LibraryManagementSystem.Entity.Fine;
import com.example.LibraryManagementSystem.Services.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fines")
public class FineController {

    @Autowired
    private FineService fineService;

    @GetMapping("/unpaid/{userId}")
    public ResponseEntity<List<Fine>> getUnpaidFines(@PathVariable Long userId) {
        return ResponseEntity.ok(fineService.getUnpaidFines(userId));
    }

    @PostMapping("/mark-paid/{fineId}")
    public ResponseEntity<String> markFineAsPaid(@PathVariable Long fineId) {
        fineService.markFineAsPaid(fineId);
        return ResponseEntity.ok("Fine marked as paid successfully.");
    }
    @PostMapping("/add-damage-fine")
    public ResponseEntity<Fine> applyDamageFine(@RequestBody FineRequest fineRequest) {
        Fine fine = fineService.addDamageFine(
                fineRequest.getUserId(),
                fineRequest.getBookId(),
                fineRequest.getFineAmount(),
                fineRequest.getReason()
        );
        return ResponseEntity.ok(fine);
    }

}

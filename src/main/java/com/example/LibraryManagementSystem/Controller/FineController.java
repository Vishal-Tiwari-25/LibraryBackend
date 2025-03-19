package com.example.LibraryManagementSystem.Controller;

import com.example.LibraryManagementSystem.DTO.FineRequest;
import com.example.LibraryManagementSystem.Entity.Fine;
import com.example.LibraryManagementSystem.Services.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fines")
public class FineController {

    @Autowired
    private FineService fineService;

//    @GetMapping("/unpaid/{userId}")
//    public ResponseEntity<List<Fine>> getUnpaidFines(@PathVariable Long userId) {
//        return ResponseEntity.ok(fineService.getUnpaidFines(userId));
//    }
    @GetMapping("/unpaid/{userId}")
    public ResponseEntity<Map<String, Object>> checkUnpaidFines(@PathVariable Long userId) {
        List<Fine> unpaidFines = fineService.getUnpaidFines(userId);
        if (!unpaidFines.isEmpty()) {
            double totalFine = unpaidFines.stream().mapToDouble(Fine::getAmount).sum();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User has an unpaid fine. Cannot issue a new book.");
            response.put("amount", totalFine);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(Collections.singletonMap("amount", 0));
    }
    @PostMapping("/unpaid/pay-fine/{userId}/{bookId}")
    public ResponseEntity<String> payFine(@PathVariable Long userId,@PathVariable Long bookId){
        fineService.payFine(userId,bookId);
        return ResponseEntity.ok("Fine paid");
    }
    @PostMapping("/unpaid/pay-fine/{userId}")
    public ResponseEntity<String> payTotalFine(@PathVariable Long userId){
        fineService.payTotalFine(userId);
        return ResponseEntity.ok("Fine total paid");
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

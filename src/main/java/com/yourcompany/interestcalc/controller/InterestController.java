package com.yourcompany.interestcalc.controller;

import com.yourcompany.interestcalc.dto.DaywiseRequest;
import com.yourcompany.interestcalc.dto.InterestResponse;
import com.yourcompany.interestcalc.dto.MonthwiseRequest;
import com.yourcompany.interestcalc.dto.RecurringDepositRequest;
import com.yourcompany.interestcalc.dto.RecurringDepositResponse;
import com.yourcompany.interestcalc.service.InterestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interest")
public class InterestController {

    private final InterestService interestService;

    public InterestController(InterestService interestService) {
        this.interestService = interestService;
    }

    // ---------- Day-wise ----------

    @PostMapping("/daywise")
    public ResponseEntity<InterestResponse> calculateDaywise(@Valid @RequestBody DaywiseRequest request) {
        InterestResponse response = interestService.calculateAndSaveDaywise(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/daywise")
    public ResponseEntity<List<InterestResponse>> getDaywiseHistory() {
        return ResponseEntity.ok(interestService.getDaywiseHistory());
    }

    // ---------- Month-wise ----------

    @PostMapping("/monthwise")
    public ResponseEntity<InterestResponse> calculateMonthwise(@Valid @RequestBody MonthwiseRequest request) {
        InterestResponse response = interestService.calculateAndSaveMonthwise(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/monthwise")
    public ResponseEntity<List<InterestResponse>> getMonthwiseHistory() {
        return ResponseEntity.ok(interestService.getMonthwiseHistory());
    }

    // ---------- Recurring Deposit ----------

    @PostMapping("/rd")
    public ResponseEntity<RecurringDepositResponse> calculateRecurringDeposit(@Valid @RequestBody RecurringDepositRequest request) {
        RecurringDepositResponse response = interestService.calculateAndSaveRecurringDeposit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/rd")
    public ResponseEntity<List<RecurringDepositResponse>> getRecurringDepositHistory() {
        return ResponseEntity.ok(interestService.getRecurringDepositHistory());
    }

    // ---------- Delete (shared endpoint, type tells us which table) ----------

    @DeleteMapping("/daywise/{id}")
    public ResponseEntity<Void> deleteDaywise(@PathVariable Long id) {
        interestService.deleteDaywise(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/monthwise/{id}")
    public ResponseEntity<Void> deleteMonthwise(@PathVariable Long id) {
        interestService.deleteMonthwise(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/rd/{id}")
    public ResponseEntity<Void> deleteRecurringDeposit(@PathVariable Long id) {
        interestService.deleteRecurringDeposit(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/daywise")
    public ResponseEntity<Void> deleteAllDaywise() {
        interestService.deleteAllDaywise();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/monthwise")
    public ResponseEntity<Void> deleteAllMonthwise() {
        interestService.deleteAllMonthwise();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/rd")
    public ResponseEntity<Void> deleteAllRecurringDeposit() {
        interestService.deleteAllRecurringDeposit();
        return ResponseEntity.noContent().build();
    }
}
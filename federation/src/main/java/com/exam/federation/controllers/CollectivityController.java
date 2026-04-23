package com.exam.federation.controllers;

import com.exam.federation.Exception.BusinessException;
import com.exam.federation.dto.*;
import com.exam.federation.services.CollectivityService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/collectivities")
public class CollectivityController {

    private final CollectivityService collectivityService;

    public CollectivityController(CollectivityService collectivityService) {
        this.collectivityService = collectivityService;
    }

    @PostMapping
    public ResponseEntity<?> createCollectivities(@RequestBody List<CreateCollectivityRequest> requests) {
        try {
            List<CollectivityResponse> responses = collectivityService.saveAll(requests);
            return ResponseEntity.status(HttpStatus.CREATED).body(responses);

        } catch (BusinessException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", e.getStatusCode());
            error.put("message", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(error);
        }
    }

    @PutMapping("/{id}/identification")
    public ResponseEntity<?> assignIdentification(
            @PathVariable String id,
            @RequestBody AssignIdentificationRequest request) {
        try {
            CollectivityResponse response = collectivityService.assignIdentification(id, request);
            return ResponseEntity.ok(response);

        } catch (BusinessException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", e.getStatusCode());
            error.put("message", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(error);
        }
    }

    @PostMapping("/{id}/membershipFees")
    public ResponseEntity<?> createMembershipFees(
            @PathVariable String id,
            @RequestBody List<CreateMembershipFee> requests) {
        List<?> responses = collectivityService.createMembershipFees(id, requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<CollectivityTransaction>> getTransactions(
            @PathVariable String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(collectivityService.getTransactions(id, from, to));
    }

    @GetMapping("/{id}/financialAccounts")
    public ResponseEntity<List<FinancialAccount>> getFinancialAccounts(@PathVariable String id) {
        List<FinancialAccount> accounts = collectivityService.getFinancialAccounts(id);
        return ResponseEntity.ok(accounts);
    }
}
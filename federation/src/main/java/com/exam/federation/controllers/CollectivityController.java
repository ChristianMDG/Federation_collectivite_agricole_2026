package com.exam.federation.controllers;

import com.exam.federation.dto.CollectivityResponse;
import com.exam.federation.dto.CreateCollectivityRequest;
import com.exam.federation.entity.AssignIdentificationRequest;
import com.exam.federation.services.CollectivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> create(@RequestBody List<CreateCollectivityRequest> requests) {
        try {
            List<CollectivityResponse> created = collectivityService.saveAll(requests);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/identification")
    public ResponseEntity<?> assignIdentification(
            @PathVariable String id,
            @RequestBody AssignIdentificationRequest request) {

        CollectivityResponse response = collectivityService.assignIdentification(id, request);
        return ResponseEntity.ok(response);
    }
}
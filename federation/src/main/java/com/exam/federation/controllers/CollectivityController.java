package com.exam.federation.controllers;

import com.exam.federation.entity.Collectivity;
import com.exam.federation.entity.CreateCollectivity;
import com.exam.federation.services.CollectivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/collectivities")

public class CollectivityController {
    private CollectivityService collectivityService;

    public CollectivityController(CollectivityService collectivityService) {
        this.collectivityService = collectivityService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody List<CreateCollectivity> requests) {
        try {
            List<Collectivity> created = collectivityService.createAll(requests);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/identification")
    public ResponseEntity<CollectivityResponse> assignIdentification(
            @PathVariable String id,
            @Valid @RequestBody AssignIdentificationRequest request) {
        CollectivityResponse response = collectivityService.assignIdentification(id, request);
        return ResponseEntity.ok(response);
    }
}

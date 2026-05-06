package com.exam.federation.controllers;

import com.exam.federation.Exception.BusinessException;
import com.exam.federation.dto.CollectivityLocalStatistics;
import com.exam.federation.services.StatisticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/collectivities/{id}/statistics")
    public ResponseEntity<?> getLocalStatistics(
            @PathVariable String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        try {
            List<CollectivityLocalStatistics> statistics = statisticsService.getLocalStatistics(id, from, to);
            return ResponseEntity.ok(statistics);
        } catch (BusinessException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", e.getStatusCode());
            error.put("message", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(error);
        }
    }
}
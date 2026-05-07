package com.exam.federation.controllers;

import com.exam.federation.Exception.BusinessException;
import com.exam.federation.dto.ActivityMemberAttendance;
import com.exam.federation.dto.CollectivityActivity;
import com.exam.federation.dto.CreateActivityMemberAttendance;
import com.exam.federation.dto.CreateCollectivityActivity;
import com.exam.federation.services.ActivityService;
import com.exam.federation.services.AttendanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/collectivities")
public class ActivityController {

    private final ActivityService activityService;
    private final AttendanceService attendanceService;

    public ActivityController(ActivityService activityService, AttendanceService attendanceService) {
        this.activityService = activityService;
        this.attendanceService = attendanceService;
    }

    @PostMapping("/{id}/activities")
    public ResponseEntity<?> createActivities(
            @PathVariable String id,
            @RequestBody List<CreateCollectivityActivity> requests) {

        try {
            List<CollectivityActivity> responses = activityService.createActivities(id, requests);
            return ResponseEntity.status(HttpStatus.CREATED).body(responses);

        } catch (BusinessException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", e.getStatusCode());
            error.put("message", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(error);

        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 400);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 500);
            error.put("message", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<?> getActivities(@PathVariable String id) {
        try {
            List<CollectivityActivity> responses = activityService.getActivities(id);
            return ResponseEntity.ok(responses);

        } catch (BusinessException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", e.getStatusCode());
            error.put("message", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(error);
        }
    }
    @PostMapping("/{id}/activities/{activityId}/attendance")
    public ResponseEntity<?> createAttendances(
            @PathVariable String id,
            @PathVariable String activityId,
            @RequestBody List<CreateActivityMemberAttendance> requests) {

        try {
            List<ActivityMemberAttendance> responses = attendanceService.createAttendances(id, activityId, requests);
            return ResponseEntity.status(HttpStatus.CREATED).body(responses);

        } catch (BusinessException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", e.getStatusCode());
            error.put("message", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(error);

        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 400);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 500);
            error.put("message", "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    @GetMapping("/{id}/activities/{activityId}/attendance")
    public ResponseEntity<?> getAttendance(
            @PathVariable String id,
            @PathVariable String activityId) {

        try {
            List<ActivityMemberAttendance> responses = attendanceService.getAttendance(id, activityId);
            return ResponseEntity.ok(responses);
        } catch (BusinessException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", e.getStatusCode());
            error.put("message", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(error);
        }
    }
}
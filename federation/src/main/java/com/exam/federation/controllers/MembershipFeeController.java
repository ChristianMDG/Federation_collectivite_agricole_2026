package com.exam.federation.controllers;

import com.exam.federation.services.MembershipFeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/collectivities")
@AllArgsConstructor
public class MembershipFeeController {
    private MembershipFeeService membershipFeeService;

    @GetMapping("/{id}/membershipFees")
    public ResponseEntity<?> getMembershipFees(@PathVariable String id){
        try {
            return ResponseEntity.ok(
                    membershipFeeService.getByCollectivity(id)
            );
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}

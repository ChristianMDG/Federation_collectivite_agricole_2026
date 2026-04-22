package com.exam.federation.controllers;

import com.exam.federation.dto.CreateMemberPayment;
import com.exam.federation.dto.MemberPayment;
import com.exam.federation.services.MemberPaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@AllArgsConstructor
public class MemberPaymentController {

    private MemberPaymentService memberPaymentService;

    @PostMapping("/{id}/payments")
    public ResponseEntity<List<MemberPayment>> createPayments(
            @PathVariable String id,
            @RequestBody List<CreateMemberPayment> requests
    ) {
        List<MemberPayment> response = memberPaymentService.createPayments(id, requests);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

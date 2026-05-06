package com.exam.federation.controllers;

import com.exam.federation.Exception.BusinessException;
import com.exam.federation.dto.CreateMember;
import com.exam.federation.dto.CreateMemberPayment;
import com.exam.federation.dto.MemberPayment;
import com.exam.federation.dto.MemberResponse;
import com.exam.federation.services.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<?> createMembers(@RequestBody List<CreateMember> requests) {
        try {
            List<MemberResponse> responses = memberService.saveAll(requests);
            return ResponseEntity.status(HttpStatus.CREATED).body(responses);

        } catch (BusinessException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", e.getStatusCode());
            error.put("message", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(error);
        }
    }

    @PostMapping("/{id}/payments")
    public ResponseEntity<List<MemberPayment>> createPayments(
            @PathVariable String id,
            @RequestBody List<CreateMemberPayment> requests) {
        List<MemberPayment> responses = memberService.createPayments(id, requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }
}
package com.exam.federation.controllers;

import com.exam.federation.dto.CreateMember;
import com.exam.federation.dto.MemberResponse;
import com.exam.federation.entity.Member;
import com.exam.federation.services.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok("Hello World");
    }
    @PostMapping("/save")
    public ResponseEntity<List<MemberResponse>> createMembers(@RequestBody List<CreateMember> requests) {
        List<MemberResponse> responses = memberService.saveAll(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }
}

package com.exam.federation.controllers;

import com.exam.federation.dto.CreateMember;
import com.exam.federation.entity.Member;
import com.exam.federation.services.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok("Hello World");
    }
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody CreateMember request) {
        return ResponseEntity.ok(memberService.saveMember(request));
    }
}

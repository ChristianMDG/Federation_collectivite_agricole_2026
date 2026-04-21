package com.exam.federation.services;

import com.exam.federation.dto.CreateMember;
import com.exam.federation.entity.Member;
import com.exam.federation.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member saveMember(CreateMember request) {
       return memberRepository.save(request);
    }
}

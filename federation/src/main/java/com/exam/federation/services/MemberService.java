package com.exam.federation.services;

import com.exam.federation.dto.CreateMember;
import com.exam.federation.dto.MemberResponse;
import com.exam.federation.entity.Member;
import com.exam.federation.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public List<MemberResponse> saveAll(List<CreateMember> requests) {
        return memberRepository.saveAll(requests);
    }
}

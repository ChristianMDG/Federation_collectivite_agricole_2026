package com.exam.federation.services;

import com.exam.federation.Exception.BusinessException;
import com.exam.federation.dto.CreateMember;
import com.exam.federation.dto.MemberResponse;
import com.exam.federation.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> saveAll(List<CreateMember> requests) {
        List<MemberResponse> responses = new ArrayList<>();

        for (CreateMember request : requests) {

            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                throw BusinessException.emailRequired();
            }

            if (memberRepository.existsByEmail(request.getEmail())) {
                throw BusinessException.emailAlreadyExists(request.getEmail());
            }

            if (request.getReferees() != null) {
                for (String refereeId : request.getReferees()) {
                    if (!memberRepository.existsById(refereeId)) {
                        throw BusinessException.refereeNotFound(refereeId);
                    }
                }
            }

            responses.add(memberRepository.save(request));
        }

        return responses;
    }
}
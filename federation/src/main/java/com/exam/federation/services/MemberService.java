package com.exam.federation.services;

import com.exam.federation.Exception.BusinessException;
import com.exam.federation.Exception.*;
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
            try {
                if (Boolean.FALSE.equals(request.getRegistrationFeePaid())) {
                    throw new RegistrationFeeNotPaidException(request.getEmail());
                }

                if (Boolean.FALSE.equals(request.getMembershipDuesPaid())) {
                    throw new MembershipDuesNotPaidException(request.getEmail());
                }

                if (request.getEmail() == null || request.getEmail().isEmpty()) {
                    throw new EmailAlreadyExistsException("Email is required");
                }

                if (memberRepository.existsByEmail(request.getEmail())) {
                    throw new EmailAlreadyExistsException(request.getEmail());
                }

                if (request.getReferees() != null && !request.getReferees().isEmpty()) {
                    for (String refereeId : request.getReferees()) {
                        if (!memberRepository.existsById(refereeId)) {
                            throw new RefereeNotFoundException(refereeId);
                        }
                    }
                }

                responses.add(memberRepository.save(request));

            } catch (BusinessException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException("Erreur technique: " + e.getMessage());
            }
        }

        return responses;
    }
}
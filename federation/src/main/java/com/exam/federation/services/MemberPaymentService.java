package com.exam.federation.services;

import com.exam.federation.dto.CreateMemberPayment;
import com.exam.federation.dto.MemberPayment;
import com.exam.federation.repository.MemberPaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberPaymentService {
    private MemberPaymentRepository memberPaymentRepository;

    public MemberPaymentService(MemberPaymentRepository memberPaymentRepository) {
        this.memberPaymentRepository = memberPaymentRepository;
    }

    public List<MemberPayment> createPayments(String memberId, List<CreateMemberPayment> requests) {
        return memberPaymentRepository.createPayments(memberId, requests);
    }
}

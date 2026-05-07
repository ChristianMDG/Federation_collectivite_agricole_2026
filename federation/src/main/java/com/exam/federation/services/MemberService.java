package com.exam.federation.services;

import com.exam.federation.Exception.BusinessException;
import com.exam.federation.dto.CreateMember;
import com.exam.federation.dto.CreateMemberPayment;
import com.exam.federation.dto.MemberPayment;
import com.exam.federation.dto.MemberResponse;
import com.exam.federation.repository.MemberPaymentRepository;
import com.exam.federation.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberPaymentRepository paymentRepository;

    public List<MemberResponse> saveAll(List<CreateMember> requests) {
        List<MemberResponse> responses = new ArrayList<>();

        for (CreateMember request : requests) {

            if (request.getFirstName() == null || request.getFirstName().isEmpty()) {
                throw new BusinessException(400, "First name is required");
            }
            if (request.getLastName() == null || request.getLastName().isEmpty()) {
                throw new BusinessException(400, "Last name is required");
            }
            if (request.getBirthDate() == null) {
                throw new BusinessException(400, "Birth date is required");
            }
            if (request.getGender() == null) {
                throw new BusinessException(400, "Gender is required");
            }
            if (request.getAddress() == null || request.getAddress().isEmpty()) {
                throw new BusinessException(400, "Address is required");
            }
            if (request.getProfession() == null || request.getProfession().isEmpty()) {
                throw new BusinessException(400, "Profession is required");
            }
            if (request.getPhoneNumber() == null) {
                throw new BusinessException(400, "Phone number is required");
            }
            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                throw BusinessException.emailRequired();
            }

            if (memberRepository.existsByEmail(request.getEmail())) {
                throw BusinessException.emailAlreadyExists(request.getEmail());
            }

            if (request.getRegistrationFeePaid() == null || !request.getRegistrationFeePaid()) {
                throw new BusinessException(400, "Registration fee of 50.000 Ar must be paid");
            }

            if (request.getReferees() == null || request.getReferees().isEmpty()) {
                throw new BusinessException(400, "At least one referee is required");
            }

            LocalDate ninetyDaysAgo = LocalDate.now().minusDays(90);

            for (String refereeId : request.getReferees()) {

                if (!memberRepository.existsById(refereeId)) {
                    throw BusinessException.refereeNotFound(refereeId);
                }

                String refereeOccupation = memberRepository.getOccupation(refereeId);
                if (refereeOccupation == null || !"SENIOR".equals(refereeOccupation)) {
                    throw new BusinessException(400, "Referee must be a confirmed member (SENIOR)");
                }

                LocalDate refereeRegistrationDate = memberRepository.getRegistrationDate(refereeId);
                if (refereeRegistrationDate == null || !refereeRegistrationDate.isBefore(ninetyDaysAgo)) {
                    throw new BusinessException(400, "Referee must have seniority greater than 90 days");
                }
            }

            responses.add(memberRepository.save(request));
        }

        return responses;
    }

    public List<MemberPayment> createPayments(String memberId, List<CreateMemberPayment> requests) {

        if (!memberRepository.existsById(memberId)) {
            throw BusinessException.memberNotFound(memberId);
        }

        String collectivityId = paymentRepository.findCollectivityIdByMemberId(memberId);
        if (collectivityId == null) {
            throw new BusinessException(400, "Member is not associated with any collectivity");
        }

        String membershipFeeId = paymentRepository.getMembershipFeeIdByCollectivity(collectivityId);
        if (membershipFeeId == null) {
            throw new BusinessException(404, "No active membership fee found for this collectivity");
        }

        for (CreateMemberPayment request : requests) {
            if (request.getAmount() == null || request.getAmount() <= 0) {
                throw BusinessException.invalidAmount();
            }
            if (request.getPaymentMode() == null) {
                throw BusinessException.invalidPaymentMode();
            }
            if (!paymentRepository.membershipFeeExists(request.getMembershipFeeIdentifier())) {
                throw BusinessException.membershipFeeNotFound(request.getMembershipFeeIdentifier());
            }
        }

        return paymentRepository.saveAll(memberId, requests, collectivityId, membershipFeeId);
    }
}
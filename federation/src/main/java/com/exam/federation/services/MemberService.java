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


    public List<MemberPayment> createPayments(String memberId, List<CreateMemberPayment> requests) {
        // 1. Vérifier que le membre existe
        if (!memberRepository.existsById(memberId)) {
            throw BusinessException.memberNotFound(memberId);
        }

        // 2. Récupérer la collectivité du membre
        String collectivityId = paymentRepository.findCollectivityIdByMemberId(memberId);
        if (collectivityId == null) {
            throw new BusinessException(400, "Member is not associated with any collectivity");
        }

        // 3. Récupérer l'ID de la cotisation active
        String membershipFeeId = paymentRepository.getMembershipFeeIdByCollectivity(collectivityId);
        if (membershipFeeId == null) {
            throw new BusinessException(404, "No active membership fee found for this collectivity");
        }

        // 4. Valider chaque paiement
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

        // 5. Appeler saveAll avec 4 paramètres (memberId, requests, collectivityId, membershipFeeId)
        return paymentRepository.saveAll(memberId, requests, collectivityId, membershipFeeId);
    }
}
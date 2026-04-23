package com.exam.federation.services;

import com.exam.federation.Exception.BusinessException;
import com.exam.federation.dto.*;

import com.exam.federation.entity.CreateCollectivityStructure;
import com.exam.federation.entity.MembershipFee;
import com.exam.federation.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CollectivityService {

    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;
    private final MembershipFeeRepository membershipFeeRepository;
    private final CollectivityTransactionRepository transactionRepository;
    private final FinancialAccountRepository financialAccountRepository;


    public List<CollectivityResponse> saveAll(List<CreateCollectivityRequest> requests) {
        List<CollectivityResponse> responses = new ArrayList<>();
        for (CreateCollectivityRequest request : requests) {
            responses.add(save(request));
        }
        return responses;
    }

    public CollectivityResponse save(CreateCollectivityRequest request) {
        if (request.getFederationApproval() == null || !request.getFederationApproval()) {
            throw BusinessException.federationApprovalMissing();
        }

        if (request.getStructure() == null) {
            throw BusinessException.structureMissing();
        }

        for (String memberId : request.getMembers()) {
            if (!memberRepository.existsById(memberId)) {
                throw BusinessException.memberNotFound(memberId);
            }
        }

        CreateCollectivityStructure s = request.getStructure();
        if (!memberRepository.existsById(s.getPresident())) {
            throw BusinessException.memberNotFound(s.getPresident());
        }
        if (!memberRepository.existsById(s.getVicePresident())) {
            throw BusinessException.memberNotFound(s.getVicePresident());
        }
        if (!memberRepository.existsById(s.getTreasurer())) {
            throw BusinessException.memberNotFound(s.getTreasurer());
        }
        if (!memberRepository.existsById(s.getSecretary())) {
            throw BusinessException.memberNotFound(s.getSecretary());
        }

        return collectivityRepository.save(request);
    }

    public CollectivityResponse assignIdentification(String id, AssignIdentificationRequest request) {
        CollectivityResponse existing = collectivityRepository.findById(id);

        if (existing == null) {
            throw BusinessException.collectivityNotFound(id);
        }

        if (existing.getNumber() != null && !existing.getNumber().isEmpty()) {
            throw BusinessException.collectivityAlreadyHasNumber();
        }

        if (existing.getName() != null && !existing.getName().isEmpty()) {
            throw BusinessException.collectivityAlreadyHasName();
        }

        if (collectivityRepository.existsByNumber(request.getNumber())) {
            throw BusinessException.numberAlreadyExists(request.getNumber());
        }

        if (collectivityRepository.existsByName(request.getName())) {
            throw BusinessException.nameAlreadyExists(request.getName());
        }

        return collectivityRepository.assignIdentification(id, request);
    }

    public List<MembershipFee> createMembershipFees(String id, List<CreateMembershipFee> requests) {

        CollectivityResponse collectivity = collectivityRepository.findById(id);
        if (collectivity == null) {
            throw BusinessException.collectivityNotFound(id);
        }

        for (CreateMembershipFee request : requests) {
            if (request.getAmount() == null || request.getAmount() <= 0) {
                throw BusinessException.invalidAmount();
            }

            if (request.getFrequency() == null) {
                throw BusinessException.invalidFrequency();
            }

            if (request.getEligibleFrom() == null) {
                throw new BusinessException(400, "Eligible from date is required");
            }
        }
        return membershipFeeRepository.saveAll(id, requests);
    }

    public List<CollectivityTransaction> getTransactions(String id, LocalDate from, LocalDate to) {

        if (from == null || to == null) {
            throw BusinessException.missingDateParameter();
        }
        if (from.isAfter(to)) {
            throw BusinessException.invalidDateRange();
        }
        CollectivityResponse collectivity = collectivityRepository.findById(id);
        if (collectivity == null) {
            throw BusinessException.collectivityNotFound(id);
        }
        return transactionRepository.findByCollectivityIdAndDateRange(id, from, to);
    }

    public List<FinancialAccount> getFinancialAccounts(String collectivityId) {
        if (collectivityRepository.findById(collectivityId) == null) {
            throw BusinessException.collectivityNotFound(collectivityId);
        }

        return financialAccountRepository.findByCollectivityId(collectivityId);
    }
}
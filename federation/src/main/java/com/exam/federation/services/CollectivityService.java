package com.exam.federation.services;

import com.exam.federation.Exception.BusinessException;
import com.exam.federation.dto.AssignIdentificationRequest;
import com.exam.federation.dto.CollectivityResponse;
import com.exam.federation.dto.CreateCollectivityRequest;

import com.exam.federation.entity.CreateCollectivityStructure;
import com.exam.federation.repository.CollectivityRepository;
import com.exam.federation.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CollectivityService {

    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;

    public CollectivityService(CollectivityRepository collectivityRepository,
                               MemberRepository memberRepository) {
        this.collectivityRepository = collectivityRepository;
        this.memberRepository = memberRepository;
    }

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
}
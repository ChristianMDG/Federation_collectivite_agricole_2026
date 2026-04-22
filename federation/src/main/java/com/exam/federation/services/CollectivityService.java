package com.exam.federation.services;

import com.exam.federation.dto.*;
import com.exam.federation.entity.AssignIdentificationRequest;
import com.exam.federation.entity.CreateCollectivityStructure;
import com.exam.federation.repository.CollectivityRepository;
import com.exam.federation.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        List<CollectivityResponse> result = new ArrayList<>();
        for (CreateCollectivityRequest request : requests) {
            result.add(save(request));
        }
        return result;
    }

    public CollectivityResponse save(CreateCollectivityRequest request) {
        // Vérifier l'approbation
        if (request.getFederationApproval() == null || !request.getFederationApproval()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Collectivity without federation approval");
        }

        // Vérifier la structure
        if (request.getStructure() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Structure missing");
        }

        // Vérifier que les membres existent
        if (request.getMembers() != null) {
            for (String memberId : request.getMembers()) {
                if (!memberRepository.existsById(memberId)) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Member not found: " + memberId);
                }
            }
        }


        CreateCollectivityStructure structure = request.getStructure();
        if (!memberRepository.existsById(structure.getPresident())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "President not found: " + structure.getPresident());
        }
        if (!memberRepository.existsById(structure.getVicePresident())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Vice president not found: " + structure.getVicePresident());
        }
        if (!memberRepository.existsById(structure.getTreasurer())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Treasurer not found: " + structure.getTreasurer());
        }
        if (!memberRepository.existsById(structure.getSecretary())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Secretary not found: " + structure.getSecretary());
        }

        return collectivityRepository.save(request);
    }

    public CollectivityResponse assignIdentification(String id, AssignIdentificationRequest request) {
        // Vérifier que la collectivité existe
        CollectivityResponse existing = collectivityRepository.findById(id);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Collectivity not found");
        }

        // Vérifier qu'elle n'a pas déjà un numéro
        if (existing.getNumber() != null && !existing.getNumber().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Collectivity already has a number: " + existing.getNumber());
        }

        // Vérifier qu'elle n'a pas déjà un nom
        if (existing.getName() != null && !existing.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Collectivity already has a name: " + existing.getName());
        }

        // Vérifier que le numéro n'existe pas déjà (conflit)
        if (collectivityRepository.existsByNumber(request.getNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Number already exists: " + request.getNumber());
        }

        // Vérifier que le nom n'existe pas déjà (conflit)
        if (collectivityRepository.existsByName(request.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Name already exists: " + request.getName());
        }


        if (request.getNumber() == null || request.getNumber().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Number is required");
        }
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Name is required");
        }

        return collectivityRepository.assignIdentification(id, request);
    }
}
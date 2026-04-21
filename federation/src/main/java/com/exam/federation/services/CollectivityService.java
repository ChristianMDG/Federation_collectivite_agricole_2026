package com.exam.federation.services;

import com.exam.federation.entity.*;
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

    public List<Collectivity> createAll(List<CreateCollectivity> requests) {
        List<Collectivity> result = new ArrayList<>();

        for (CreateCollectivity request : requests) {
            result.add(create(request));
        }

        return result;
    }

    private Collectivity create(CreateCollectivity request) {

        if (request.getFederationApproval() == null) {
            throw new IllegalArgumentException("Federation approval required");
        }

        if (request.getMembers() == null) {
            throw new IllegalArgumentException("Members required");
        }

        if (request.getStructure() == null) {
            throw new IllegalArgumentException("Structure required");
        }

        List<Member> members = new ArrayList<>();
        for (String id : request.getMembers()) {
            members.add(getMember(id));
        }

        CreateCollectivityStructure s = request.getStructure();

        CollectivityStructure structure = new CollectivityStructure();
        structure.setPresident(getMember(s.getPresident()));
        structure.setVicePresident(getMember(s.getVicePresident()));
        structure.setTreasurer(getMember(s.getTreasurer()));
        structure.setSecretary(getMember(s.getSecretary()));

        return collectivityRepository.save(request, members, structure);
    }

    private Member getMember(String id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member " + id + " not found"));
    }
}
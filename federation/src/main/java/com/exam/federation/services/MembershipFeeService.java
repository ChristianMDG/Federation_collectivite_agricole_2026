package com.exam.federation.services;

import com.exam.federation.entity.MembershipFee;
import com.exam.federation.repository.CollectivityRepository;
import com.exam.federation.repository.MembershipFeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MembershipFeeService {
    private MembershipFeeRepository membershipFeeRepository;
    private CollectivityRepository collectivityRepository;

    public List<MembershipFee> getByCollectivity(String collectivityId) {
        if (collectivityRepository.findById(collectivityId) == null) {
            throw new RuntimeException("Collectivity with id " + collectivityId + " not found");
        }
        return membershipFeeRepository.findByCollectivityId(collectivityId);
    }
}

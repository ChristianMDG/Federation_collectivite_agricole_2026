package com.exam.federation.services;

import com.exam.federation.Exception.BusinessException;
import com.exam.federation.dto.CollectivityActivity;
import com.exam.federation.dto.CreateCollectivityActivity;
import com.exam.federation.repository.ActivityRepository;
import com.exam.federation.repository.CollectivityRepository;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final CollectivityRepository collectivityRepository;

    public ActivityService(ActivityRepository activityRepository,
                           CollectivityRepository collectivityRepository) {
        this.activityRepository = activityRepository;
        this.collectivityRepository = collectivityRepository;
    }


    public List<CollectivityActivity> createActivities(String collectivityId, List<CreateCollectivityActivity> requests) {

        if (collectivityRepository.findById(collectivityId) == null) {
            throw new BusinessException(404, "Collectivity not found: " + collectivityId);
        }

        for (CreateCollectivityActivity request : requests) {

            if (request.getRecurrenceRule() != null && request.getExecutiveDate() != null) {
                throw new IllegalArgumentException(
                        "Both recurrence rule and executive date cannot be provided at the same time");
            }

            if (request.getRecurrenceRule() == null && request.getExecutiveDate() == null) {
                throw new IllegalArgumentException(
                        "Either recurrence rule or executive date must be provided");
            }

            if (request.getLabel() == null || request.getLabel().isEmpty()) {
                throw new IllegalArgumentException("Label is required");
            }

            if (request.getActivityType() == null) {
                throw new IllegalArgumentException("Activity type is required");
            }
        }

        return activityRepository.saveAll(collectivityId, requests);
    }

    public List<CollectivityActivity> getActivities(String collectivityId) {
        if (collectivityRepository.findById(collectivityId) == null) {
            throw new BusinessException(404, "Collectivity not found: " + collectivityId);
        }

        return activityRepository.findByCollectivityId(collectivityId);
    }
}
package com.exam.federation.services;

import com.exam.federation.Exception.BusinessException;
import com.exam.federation.dto.ActivityMemberAttendance;
import com.exam.federation.dto.CreateActivityMemberAttendance;
import com.exam.federation.repository.ActivityRepository;
import com.exam.federation.repository.AttendanceRepository;
import com.exam.federation.repository.CollectivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceService {

    private AttendanceRepository attendanceRepository;
    private ActivityRepository activityRepository;
    private CollectivityRepository collectivityRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             ActivityRepository activityRepository,
                             CollectivityRepository collectivityRepository) {
        this.attendanceRepository = attendanceRepository;
        this.activityRepository = activityRepository;
        this.collectivityRepository = collectivityRepository;
    }

    public List<ActivityMemberAttendance> createAttendances(String collectivityId, String activityId,
                                                            List<CreateActivityMemberAttendance> requests) {
        if (collectivityRepository.findById(collectivityId) == null) {
            throw new BusinessException(404, "Collectivity not found: " + collectivityId);
        }

        if (activityRepository.existsByIdAndCollectivityId(activityId, collectivityId) == false) {
            throw new BusinessException(404, "Activity not found: " + activityId);
        }

        for (CreateActivityMemberAttendance request : requests) {
            if (request.getAttendanceStatus() == null) {
                throw new IllegalArgumentException("Attendance status is required");
            }
        }

        return attendanceRepository.saveAll(activityId, requests);
    }

    public List<ActivityMemberAttendance> getAttendance(String collectivityId, String activityId) {

        if (collectivityRepository.findById(collectivityId) == null) {
            throw new BusinessException(404, "Collectivity not found: " + collectivityId);
        }

        if (!attendanceRepository.existsById(activityId, collectivityId)) {
            throw new BusinessException(404, "Activity not found: " + activityId);
        }

        return attendanceRepository.findByActivityId(activityId);
    }
}
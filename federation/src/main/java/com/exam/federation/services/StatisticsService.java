package com.exam.federation.services;

import com.exam.federation.Exception.BusinessException;
import com.exam.federation.dto.CollectivityLocalStatistics;
import com.exam.federation.repository.CollectivityRepository;
import com.exam.federation.repository.StatisticsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;
    private final CollectivityRepository collectivityRepository;

    public StatisticsService(StatisticsRepository statisticsRepository,
                             CollectivityRepository collectivityRepository) {
        this.statisticsRepository = statisticsRepository;
        this.collectivityRepository = collectivityRepository;
    }

    public List<CollectivityLocalStatistics> getLocalStatistics(String collectivityId, LocalDate from, LocalDate to) {

        if (collectivityRepository.findById(collectivityId) == null) {
            throw BusinessException.collectivityNotFound(collectivityId);
        }

        if (from == null || to == null) {
            throw new IllegalArgumentException("Les paramètres 'from' et 'to' sont obligatoires");
        }

        if (from.isAfter(to)) {
            throw new IllegalArgumentException("La date 'from' doit être antérieure à la date 'to'");
        }

        return statisticsRepository.getLocalStatistics(collectivityId, from, to);
    }
}
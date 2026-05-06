package com.exam.federation.services;

import com.exam.federation.Exception.BusinessException;
import com.exam.federation.dto.CollectivityOverallStatistics;
import com.exam.federation.repository.CollectivityStatisticsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class CollectivityStatisticsService {

    private CollectivityStatisticsRepository statisticsRepository;

    public List<CollectivityOverallStatistics> getOverallStatistics(LocalDate from, LocalDate to) {
        if (from == null || to == null) throw BusinessException.missingDateParameter();
        if (from.isAfter(to)) throw BusinessException.invalidDateRange();
        return statisticsRepository.getOverallStatistics(from, to);
    }
}

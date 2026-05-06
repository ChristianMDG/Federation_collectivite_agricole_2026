package com.exam.federation.dto;

import com.exam.federation.entity.Enums.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyRecurrenceRule {
    private Integer weekOrdinal;
    private DayOfWeek dayOfWeek;
}
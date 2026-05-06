package com.exam.federation.dto;

import com.exam.federation.entity.Enums.Frequency;
import com.exam.federation.entity.Enums.ActivityStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipFee {
    private String id;
    private LocalDate eligibleFrom;
    private Frequency frequency;
    private Double amount;
    private String label;
    private ActivityStatus status;
}
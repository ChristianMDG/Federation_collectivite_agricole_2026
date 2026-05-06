package com.exam.federation.dto;

import com.exam.federation.entity.Enums.Frequency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMembershipFee {
    private LocalDate eligibleFrom;
    private Frequency frequency;
    private Double amount;
    private String label;
}
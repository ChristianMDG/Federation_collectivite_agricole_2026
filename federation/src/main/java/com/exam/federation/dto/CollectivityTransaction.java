package com.exam.federation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectivityTransaction {
    private String id;
    private LocalDate transactionDate;
    private Double amount;
    private String paymentMode;
    private MemberResponse memberDebited;
}
package com.exam.federation.dto;

import com.exam.federation.entity.Enums.PaymentMode;
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
    private PaymentMode paymentMode;
    private MemberResponse memberDebited;
}
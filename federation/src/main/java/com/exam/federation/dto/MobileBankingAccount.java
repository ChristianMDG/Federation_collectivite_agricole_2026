package com.exam.federation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MobileBankingAccount extends FinancialAccount {
    private String holderName;
    private String mobileBankingService;
    private Integer mobileNumber;
}
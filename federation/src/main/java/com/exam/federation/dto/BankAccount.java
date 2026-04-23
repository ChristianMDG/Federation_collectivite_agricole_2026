package com.exam.federation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BankAccount extends FinancialAccount {
    private String holderName;
    private String bankName;
    private Integer bankCode;
    private Integer bankBranchCode;
    private Integer bankAccountNumber;
    private Integer bankAccountKey;
}
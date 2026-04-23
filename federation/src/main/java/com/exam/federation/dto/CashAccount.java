package com.exam.federation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CashAccount extends FinancialAccount {
    // Pas de propriétés supplémentaires selon l'OpenAPI
}
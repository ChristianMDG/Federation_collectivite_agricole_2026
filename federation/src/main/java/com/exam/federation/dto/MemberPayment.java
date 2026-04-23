package com.exam.federation.dto;

import com.exam.federation.entity.Enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberPayment {
    private String id;
    private Integer amount;
    private PaymentMode paymentMode;
    private FinancialAccount accountCredited;
    private LocalDate creationDate;
}
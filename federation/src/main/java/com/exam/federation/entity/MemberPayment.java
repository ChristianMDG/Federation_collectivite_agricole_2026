package com.exam.federation.entity;

import com.exam.federation.entity.Enums.PaymentMode;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MemberPayment {
    private String id;
    private String memberId;
    private BigDecimal amount;
    private PaymentMode paymentMode;
    private String membershipFeeId;
    private String accountCreditedId;
    private LocalDate creationDate;

    public MemberPayment() {}
    public MemberPayment(String id, String memberId, BigDecimal amount, PaymentMode paymentMode, String membershipFeeId, String accountCreditedId, LocalDate creationDate) {
        this.id = id;
        this.memberId = memberId;
        this.amount = amount;
        this.paymentMode = paymentMode;
        this.membershipFeeId = membershipFeeId;
        this.accountCreditedId = accountCreditedId;
        this.creationDate = creationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getMembershipFeeId() {
        return membershipFeeId;
    }

    public void setMembershipFeeId(String membershipFeeId) {
        this.membershipFeeId = membershipFeeId;
    }

    public String getAccountCreditedId() {
        return accountCreditedId;
    }

    public void setAccountCreditedId(String accountCreditedId) {
        this.accountCreditedId = accountCreditedId;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}

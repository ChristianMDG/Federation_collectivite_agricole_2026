package com.exam.federation.Exception;

public class MembershipDuesNotPaidException extends BusinessException {
    public MembershipDuesNotPaidException(String email) {
        super(400, "MEMBERSHIP_DUES_NOT_PAID", "Membership dues not paid for member: " + email);
    }
}
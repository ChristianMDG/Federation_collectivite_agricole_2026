package com.exam.federation.Exception;

public class RegistrationFeeNotPaidException extends BusinessException {
    public RegistrationFeeNotPaidException(String email) {
        super(400, "REGISTRATION_FEE_NOT_PAID", "Registration fee not paid for member: " + email);
    }
}
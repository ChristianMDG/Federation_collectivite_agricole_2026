package com.exam.federation.Exception;

public class EmailAlreadyExistsException extends BusinessException {
    public EmailAlreadyExistsException(String email) {
        super(400, "EMAIL_ALREADY_EXISTS", "Email already exists: " + email);
    }
}
package com.exam.federation.Exception;

public class NumberAlreadyExistsException extends BusinessException {
    public NumberAlreadyExistsException(String number) {
        super(409, "NUMBER_ALREADY_EXISTS", "Number already exists: " + number);
    }
}
package com.exam.federation.Exception;

public class CollectivityAlreadyHasNumberException extends BusinessException {
    public CollectivityAlreadyHasNumberException(String number) {
        super(400, "COLLECTIVITY_ALREADY_HAS_NUMBER", "Collectivity already has a number: " + number);
    }
}
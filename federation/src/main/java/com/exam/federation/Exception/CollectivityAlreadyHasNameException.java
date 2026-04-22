package com.exam.federation.Exception;

public class CollectivityAlreadyHasNameException extends BusinessException {
    public CollectivityAlreadyHasNameException(String name) {
        super(400, "COLLECTIVITY_ALREADY_HAS_NAME", "Collectivity already has a name: " + name);
    }
}
package com.exam.federation.Exception;

public class CollectivityNotFoundException extends BusinessException {
    public CollectivityNotFoundException(String id) {
        super(404, "COLLECTIVITY_NOT_FOUND", "Collectivity not found: " + id);
    }
}
package com.exam.federation.Exception;

public class BusinessException extends RuntimeException {
    private final int statusCode;

    public BusinessException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }



    public static BusinessException registrationFeeNotPaid() {
        return new BusinessException(400, "Registration fee not paid");
    }

    public static BusinessException membershipDuesNotPaid() {
        return new BusinessException(400, "Membership dues not paid");
    }

    public static BusinessException emailAlreadyExists(String email) {
        return new BusinessException(400, "Email already exists: " + email);
    }

    public static BusinessException emailRequired() {
        return new BusinessException(400, "Email is required");
    }

    public static BusinessException refereeNotFound(String refereeId) {
        return new BusinessException(400, "Referee not found: " + refereeId);
    }

    public static BusinessException memberNotFound(String id) {
        return new BusinessException(404, "Member not found: " + id);
    }

    public static BusinessException federationApprovalMissing() {
        return new BusinessException(400, "Collectivity without federation approval");
    }

    public static BusinessException structureMissing() {
        return new BusinessException(400, "Structure missing");
    }

    public static BusinessException collectivityNotFound(String id) {
        return new BusinessException(404, "Collectivity not found: " + id);
    }

    public static BusinessException collectivityAlreadyHasNumber() {
        return new BusinessException(400, "Collectivity already has a number");
    }

    public static BusinessException collectivityAlreadyHasNumber(String number) {
        return new BusinessException(400, "Collectivity already has a number: " + number);
    }

    public static BusinessException collectivityAlreadyHasName() {
        return new BusinessException(400, "Collectivity already has a name");
    }

    public static BusinessException collectivityAlreadyHasName(String name) {
        return new BusinessException(400, "Collectivity already has a name: " + name);
    }

    public static BusinessException numberAlreadyExists(String number) {
        return new BusinessException(409, "Number already exists: " + number);
    }

    public static BusinessException nameAlreadyExists(String name) {
        return new BusinessException(409, "Name already exists: " + name);
    }
}
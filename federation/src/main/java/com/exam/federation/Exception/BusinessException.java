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

    public static BusinessException registrationFeeNotPaid(String email) {
        return new BusinessException(400, "Registration fee not paid for member: " + email);
    }

    public static BusinessException membershipDuesNotPaid(String email) {
        return new BusinessException(400, "Membership dues not paid for member: " + email);
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

    public static BusinessException collectivityNotFound(String id) {
        return new BusinessException(404, "Collectivity not found: " + id);
    }

    public static BusinessException presidentNotFound(String id) {
        return new BusinessException(404, "President not found: " + id);
    }

    public static BusinessException vicePresidentNotFound(String id) {
        return new BusinessException(404, "Vice president not found: " + id);
    }

    public static BusinessException treasurerNotFound(String id) {
        return new BusinessException(404, "Treasurer not found: " + id);
    }

    public static BusinessException secretaryNotFound(String id) {
        return new BusinessException(404, "Secretary not found: " + id);
    }

    public static BusinessException federationApprovalMissing() {
        return new BusinessException(400, "Collectivity without federation approval");
    }

    public static BusinessException structureMissing() {
        return new BusinessException(400, "Structure missing");
    }

    public static BusinessException collectivityAlreadyHasNumber() {
        return new BusinessException(400, "Collectivity already has a number");
    }

    public static BusinessException collectivityAlreadyHasName() {
        return new BusinessException(400, "Collectivity already has a name");
    }


    public static BusinessException numberAlreadyExists(String number) {
        return new BusinessException(409, "Number already exists: " + number);
    }

    public static BusinessException nameAlreadyExists(String name) {
        return new BusinessException(409, "Name already exists: " + name);
    }

    public static BusinessException invalidAmount() {
        return new BusinessException(400, "Amount must be greater than 0");
    }

    public static BusinessException invalidFrequency() {
        return new BusinessException(400, "Invalid frequency. Allowed: WEEKLY, MONTHLY, ANNUALLY, PUNCTUALLY");
    }

    public static BusinessException invalidDateRange() {
        return new BusinessException(400, "Invalid date range. 'from' must be before 'to'");
    }

    public static BusinessException missingDateParameter() {
        return new BusinessException(400, "Both 'from' and 'to' date parameters are required");
    }
}
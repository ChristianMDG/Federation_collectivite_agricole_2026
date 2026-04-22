package com.exam.federation.Exception;

public class MemberNotFoundException extends BusinessException {
    public MemberNotFoundException(String id) {
        super(404, "MEMBER_NOT_FOUND", "Member not found: " + id);
    }
}
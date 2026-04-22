package com.exam.federation.Exception;

public class RefereeNotFoundException extends BusinessException {
    public RefereeNotFoundException(String refereeId) {
        super(400, "REFEREE_NOT_FOUND", "Member with bad referees: " + refereeId + " not found");
    }
}
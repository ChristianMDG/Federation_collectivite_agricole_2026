package com.exam.federation.Exception;

public class FederationApprovalMissingException extends BusinessException {
    public FederationApprovalMissingException() {
        super(400, "FEDERATION_APPROVAL_MISSING", "Collectivity without federation approval");
    }
}
package com.exam.federation.Exception;

public class StructureMissingException extends BusinessException {
    public StructureMissingException() {
        super(400, "STRUCTURE_MISSING", "Structure missing");
    }
}
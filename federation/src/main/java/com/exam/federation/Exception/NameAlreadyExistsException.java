package com.exam.federation.Exception;


public class NameAlreadyExistsException extends BusinessException {
    public NameAlreadyExistsException(String name) {
        super(409, "NAME_ALREADY_EXISTS", "Name already exists: " + name);
    }
}
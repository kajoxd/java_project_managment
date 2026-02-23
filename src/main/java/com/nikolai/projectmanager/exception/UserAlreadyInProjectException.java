package com.nikolai.projectmanager.exception;

public class UserAlreadyInProjectException extends RuntimeException {
    public UserAlreadyInProjectException(String message) {
        super(message);
    }
}
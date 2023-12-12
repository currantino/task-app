package com.currantino.taskapp.exception;

public class UserAlreadyExistsException extends ConflictException {
    public UserAlreadyExistsException(String msg) {
        super(msg);
    }
}

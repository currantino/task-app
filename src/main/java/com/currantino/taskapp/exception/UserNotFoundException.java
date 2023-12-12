package com.currantino.taskapp.exception;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(String msg) {
        super(msg);
    }
}

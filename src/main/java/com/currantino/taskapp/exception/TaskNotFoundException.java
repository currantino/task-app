package com.currantino.taskapp.exception;

public class TaskNotFoundException extends ResourceNotFoundException {
    public TaskNotFoundException(String s) {
        super(s);
    }
}

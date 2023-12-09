package com.currantino.taskapp.exception;

public class CommentNotFoundException extends ResourceNotFoundException {
    public CommentNotFoundException(String s) {
        super(s);
    }
}

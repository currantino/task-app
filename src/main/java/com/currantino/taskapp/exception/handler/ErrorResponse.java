package com.currantino.taskapp.exception.handler;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResponse {
    private String message;

    @Builder.Default
    private List<String> fieldErrors = new ArrayList<>();

    public ErrorResponse(String message) {
        this.message = message;
    }

    @Builder
    public ErrorResponse(String message, List<String> fieldErrors) {
        this.message = message;
        this.fieldErrors = fieldErrors;
    }
}

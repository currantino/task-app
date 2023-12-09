package com.currantino.taskapp.exception.handler;

import com.currantino.taskapp.exception.AccessDeniedException;
import com.currantino.taskapp.exception.BadRequestException;
import com.currantino.taskapp.exception.ConflictException;
import com.currantino.taskapp.exception.InvalidPasswordException;
import com.currantino.taskapp.exception.ResourceNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse validationError(final MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<String> fieldErrors = result.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return ErrorResponse.builder()
                .fieldErrors(fieldErrors)
                .message("Validation error")
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleBadRequest(final BadRequestException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ErrorResponse handleDatabaseException(final DataIntegrityViolationException e) {
        return new ErrorResponse("Invalid value");
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleDataNotFoundException(final ResourceNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(FORBIDDEN)
    public ErrorResponse handleInvalidPasswordException(final InvalidPasswordException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(final AccessDeniedException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ErrorResponse handleConflictException(final ConflictException e) {
        return new ErrorResponse(e.getMessage());
    }

}

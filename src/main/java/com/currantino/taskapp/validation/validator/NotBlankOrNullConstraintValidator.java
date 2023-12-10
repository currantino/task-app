package com.currantino.taskapp.validation.validator;

import com.currantino.taskapp.validation.annotation.NotBlankOrNull;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotBlankOrNullConstraintValidator implements ConstraintValidator<NotBlankOrNull, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || !value.isBlank();
    }
}

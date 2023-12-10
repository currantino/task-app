package com.currantino.taskapp.validation.annotation;

import com.currantino.taskapp.validation.validator.NotBlankOrNullConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotBlankOrNullConstraintValidator.class)
public @interface NotBlankOrNull {
    String message() default "Value must be null or not blank.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

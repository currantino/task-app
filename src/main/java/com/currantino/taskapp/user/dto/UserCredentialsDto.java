package com.currantino.taskapp.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record UserCredentialsDto(
        @Email
        @Length(max = 127)
        @NotNull
        String email,
        @NotBlank
        @Length(min = 8, max = 127)
        String password
) {
}

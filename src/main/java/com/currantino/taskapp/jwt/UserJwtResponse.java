package com.currantino.taskapp.jwt;

import com.currantino.taskapp.user.dto.UserFullDto;

public record UserJwtResponse(
        UserFullDto user,
        JwtResponse jwt
) {
}

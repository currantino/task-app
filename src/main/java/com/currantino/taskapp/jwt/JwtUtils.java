package com.currantino.taskapp.jwt;


import com.currantino.taskapp.user.entity.Role;
import com.currantino.taskapp.user.entity.User;
import io.jsonwebtoken.Claims;
import lombok.experimental.UtilityClass;


@UtilityClass
public final class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setUser(User.builder()
                .role(getRole(claims))
                .id(getUserId(claims))
                .email(claims.getSubject())
                .build());
        return jwtInfoToken;
    }

    private Long getUserId(Claims claims) {
        return claims.get("id", Long.class);
    }

    private Role getRole(Claims claims) {
        String role = claims.get("role", String.class);
        return Role.valueOf(role);
    }

}
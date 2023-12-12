package com.currantino.taskapp.auth.controller;

import com.currantino.taskapp.auth.service.AuthService;
import com.currantino.taskapp.jwt.JwtResponse;
import com.currantino.taskapp.jwt.RefreshJwtRequest;
import com.currantino.taskapp.jwt.UserJwtResponse;
import com.currantino.taskapp.user.dto.UserCredentialsDto;
import com.currantino.taskapp.user.dto.UserFullDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/signup")
    public ResponseEntity<UserJwtResponse> signup(
            @Valid
            @RequestBody
            UserCredentialsDto userCredentialsDto
    ) {
        UserFullDto user = authService.signup(userCredentialsDto);
        JwtResponse jwt = authService.getTokens(user);
        UserJwtResponse response = new UserJwtResponse(user, jwt);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public UserJwtResponse login(@Valid
                                 @RequestBody
                                 UserCredentialsDto userDto) {
        return authService.login(userDto);
    }

    @PostMapping("/token")
    public JwtResponse getNewAccessToken(
            @RequestBody
            RefreshJwtRequest request
    ) {
        return authService.getAccessToken(request.getRefreshToken());
    }

    @PostMapping("/refresh")
    public JwtResponse getNewRefreshToken(
            @RequestBody
            RefreshJwtRequest request
    ) {
        return authService.refresh(request.getRefreshToken());
    }
}

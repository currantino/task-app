package com.currantino.taskapp.auth.controller;

import com.currantino.taskapp.auth.service.AuthService;
import com.currantino.taskapp.jwt.JwtResponse;
import com.currantino.taskapp.jwt.RefreshJwtRequest;
import com.currantino.taskapp.jwt.UserJwtResponse;
import com.currantino.taskapp.user.dto.UserCredentialsDto;
import com.currantino.taskapp.user.dto.UserFullDto;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Registration of a new user")
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

    @Operation(summary = "Login existing user")
    @PostMapping("/login")
    public ResponseEntity<UserJwtResponse> login(@Valid
                                                 @RequestBody
                                                 UserCredentialsDto userDto) {
        UserJwtResponse response = authService.login(userDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get new access token by providing your refresh token.")
    @PostMapping("/token")
    public ResponseEntity<JwtResponse> getNewAccessToken(
            @RequestBody
            RefreshJwtRequest request
    ) {
        JwtResponse response = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get new refresh and access token by providing old refresh token.")
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(
            @RequestBody
            RefreshJwtRequest request
    ) {
        JwtResponse response = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }
}

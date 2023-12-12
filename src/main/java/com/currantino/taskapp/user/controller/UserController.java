package com.currantino.taskapp.user.controller;

import com.currantino.taskapp.user.dto.UserFullDto;
import com.currantino.taskapp.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@SecurityRequirement(name = "jwtAuth")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get user by id.")
    @GetMapping("/{userId}")
    public ResponseEntity<UserFullDto> getUserById(@PathVariable
                                                   Long userId) {
        UserFullDto user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }


}

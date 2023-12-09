package com.currantino.taskapp.jwt;

import lombok.Value;

@Value
public class JwtResponse {

    String type = "Bearer";
    String accessToken;
    String refreshToken;

}
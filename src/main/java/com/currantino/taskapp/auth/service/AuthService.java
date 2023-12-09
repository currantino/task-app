package com.currantino.taskapp.auth.service;

import com.currantino.taskapp.exception.AccessDeniedException;
import com.currantino.taskapp.exception.InvalidRefreshTokenException;
import com.currantino.taskapp.exception.UserNotFoundException;
import com.currantino.taskapp.jwt.JwtAuthentication;
import com.currantino.taskapp.jwt.JwtProvider;
import com.currantino.taskapp.jwt.JwtResponse;
import com.currantino.taskapp.jwt.UserJwtResponse;
import com.currantino.taskapp.user.dto.UserCredentialsDto;
import com.currantino.taskapp.user.dto.UserFullDto;
import com.currantino.taskapp.user.entity.Role;
import com.currantino.taskapp.user.entity.User;
import com.currantino.taskapp.user.mapper.UserMapper;
import com.currantino.taskapp.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final Map<String, String> refreshStorage = new ConcurrentHashMap<>();

    @Autowired
    public AuthService(PasswordEncoder passwordEncoder, UserMapper userMapper, UserRepository userRepository, JwtProvider jwtProvider) {
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    public UserFullDto signup(UserCredentialsDto userDto) {
        User user = userMapper.toEntity(userDto);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);
        return userMapper.toFullDto(savedUser);
    }

    public UserJwtResponse login(@NonNull UserCredentialsDto userDto) {
        final User user = userRepository.findByEmail(userDto.email())
                .orElseThrow(() -> new UserNotFoundException("Cannot find user %s.".formatted(userDto.email())));
        boolean passwordMatches = passwordEncoder.matches(userDto.password(), user.getPassword());
        if (!passwordMatches) {
            throw new AccessDeniedException("Invalid password!");
        }
        JwtResponse jwtResponse = generateTokens(user);
        return new UserJwtResponse(userMapper.toFullDto(user), jwtResponse);
    }

    public JwtResponse getTokens(UserFullDto userDto) {
        User user = userMapper.toEntity(userDto);
        return generateTokens(user);
    }

    private JwtResponse generateTokens(User user) {
        final String accessToken = jwtProvider.generateAccessToken(user);
        final String refreshToken = jwtProvider.generateRefreshToken(user);
        //TODO: maybe put in redis
        refreshStorage.put(user.getEmail(), refreshToken);
        return new JwtResponse(accessToken, refreshToken);
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            return new JwtResponse(null, null);
        }
        final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        final String email = claims.getSubject();
        final String savedRefreshToken = refreshStorage.get(email);
        if (savedRefreshToken == null || !savedRefreshToken.equals(refreshToken)) {
            throw new InvalidRefreshTokenException("Provided refresh token is invalid.");
        }
        final User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Cannot find user %s.".formatted(email)));
        final String accessToken = jwtProvider.generateAccessToken(user);
        return new JwtResponse(accessToken, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String email = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(email);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new UserNotFoundException("Cannot find user %s.".formatted(email)));
                return generateTokens(user);
            }
        }
        throw new JwtException("Invalid refresh token");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

    public Long getLoggedInUserId() {
        return ((JwtAuthentication) SecurityContextHolder.getContext().getAuthentication()).getUser().getId();
    }
}

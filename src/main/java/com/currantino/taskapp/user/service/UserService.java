package com.currantino.taskapp.user.service;

import com.currantino.taskapp.exception.UserNotFoundException;
import com.currantino.taskapp.user.dto.UserFullDto;
import com.currantino.taskapp.user.entity.User;
import com.currantino.taskapp.user.mapper.UserMapper;
import com.currantino.taskapp.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserFullDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Could not find user with id: " + userId));
        return userMapper.toFullDto(user);
    }
}

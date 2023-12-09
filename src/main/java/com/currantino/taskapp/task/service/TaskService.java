package com.currantino.taskapp.task.service;

import com.currantino.taskapp.exception.UserNotFoundException;
import com.currantino.taskapp.task.controller.TaskFullDto;
import com.currantino.taskapp.task.mapper.TaskMapper;
import com.currantino.taskapp.task.repository.TaskRepository;
import com.currantino.taskapp.user.entity.User;
import com.currantino.taskapp.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository,
                       TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
    }


    @Transactional
    public Page<TaskFullDto> getTasksByCreator(Long creatorId, Pageable pageable) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new UserNotFoundException("Could not find user with id: " + creatorId));
        return taskRepository
                .findAllByCreator(creatorId, pageable)
                .map(taskMapper::toFullDto);
    }
}

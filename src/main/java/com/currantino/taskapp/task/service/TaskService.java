package com.currantino.taskapp.task.service;

import com.currantino.taskapp.auth.service.AuthService;
import com.currantino.taskapp.exception.AccessDeniedException;
import com.currantino.taskapp.exception.TaskNotFoundException;
import com.currantino.taskapp.exception.UserNotFoundException;
import com.currantino.taskapp.task.controller.TaskFullDto;
import com.currantino.taskapp.task.dto.CreateTaskDto;
import com.currantino.taskapp.task.dto.UpdateTaskDto;
import com.currantino.taskapp.task.entity.Task;
import com.currantino.taskapp.task.entity.TaskStatus;
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
    private final AuthService authService;

    @Autowired
    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository,
                       TaskMapper taskMapper,
                       AuthService authService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
        this.authService = authService;
    }


    @Transactional
    public TaskFullDto addTask(CreateTaskDto createTaskDto) {
        Long realUserId = authService.getLoggedInUserId();
        if (!realUserId.equals(createTaskDto.creatorId())) {
            throw new AccessDeniedException("Invalid task creator id.");
        }
        if (createTaskDto.assigneeId() != null) {
            User assignee = userRepository.findById(createTaskDto.assigneeId())
                    .orElseThrow(() -> new UserNotFoundException("Could not find user with id: " + createTaskDto.assigneeId()));
        }
        Task task = taskMapper.toEntity(createTaskDto);
        Task saved = taskRepository.save(task);
        return taskMapper.toFullDto(saved);
    }

    @Transactional
    public Page<TaskFullDto> getTasksByCreator(Long creatorId, Pageable pageable) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new UserNotFoundException("Could not find user with id: " + creatorId));
        return taskRepository
                .findAllByCreator(creatorId, pageable)
                .map(taskMapper::toFullDto);
    }

    @Transactional
    public Page<TaskFullDto> getTasksByAssignee(Long assigneeId, Pageable pageable) {
        User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new UserNotFoundException("Could not find user with id: " + assigneeId));
        return taskRepository
                .findAllByAssignee(assigneeId, pageable)
                .map(taskMapper::toFullDto);
    }

    @Transactional
    public void deleteTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new UserNotFoundException("Could not find task with id: " + taskId));
        Long realRequesterId = authService.getLoggedInUserId();
        if (!task.getCreator().getId().equals(realRequesterId)) {
            throw new AccessDeniedException("You can not delete tasks of other users.");
        }
        taskRepository.deleteById(taskId);
    }

    @Transactional
    public TaskFullDto updateTaskById(Long taskId, UpdateTaskDto taskDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Could not find task with id: " + taskId));
        Long realUserId = authService.getLoggedInUserId();
        if (!realUserId.equals(task.getCreator().getId())) {
            throw new AccessDeniedException("You can not update tasks of other users.");
        }
        User assignee = userRepository.findById(taskDto.assigneeId())
                .orElseThrow(() -> new UserNotFoundException("Could not find user with id: " + taskDto.assigneeId()));
        Task mapped = taskMapper.partialUpdate(taskDto, task);
        Task updated = taskRepository.save(mapped);
        return taskMapper.toFullDto(updated);
    }

    @Transactional
    public TaskFullDto updateTaskStatusById(Long taskId, TaskStatus status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Could not find task with id: " + taskId));
        Long realUserId = authService.getLoggedInUserId();
        if (!realUserId.equals(task.getCreator().getId()) && !realUserId.equals(task.getAssignee().getId())) {
            throw new AccessDeniedException("You can not update tasks of other users.");
        }
        task.setStatus(status);
        Task updated = taskRepository.save(task);
        return taskMapper.toFullDto(updated);
    }
}

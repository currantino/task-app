package com.currantino.taskapp.task.controller;

import com.currantino.taskapp.task.entity.TaskPriority;
import com.currantino.taskapp.task.entity.TaskStatus;
import com.currantino.taskapp.user.dto.UserFullDto;

import java.io.Serializable;

/**
 * DTO for {@link com.currantino.taskapp.task.entity.Task}
 */
public record TaskFullDto(
        Long id, String name, String description, TaskStatus status, TaskPriority priority, UserFullDto creator,
        UserFullDto assignee
) implements Serializable {
}
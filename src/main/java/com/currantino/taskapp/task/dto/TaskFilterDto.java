package com.currantino.taskapp.task.dto;

import com.currantino.taskapp.task.entity.TaskPriority;
import com.currantino.taskapp.task.entity.TaskStatus;

public record TaskFilterDto(
        String name,
        String description,
        TaskStatus status,
        TaskPriority priority,
        Long assigneeId,
        Long creatorId
) {
}

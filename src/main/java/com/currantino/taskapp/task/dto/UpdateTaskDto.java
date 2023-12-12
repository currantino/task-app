package com.currantino.taskapp.task.dto;

import com.currantino.taskapp.task.entity.TaskPriority;
import com.currantino.taskapp.task.entity.TaskStatus;
import com.currantino.taskapp.validation.annotation.NotBlankOrNull;
import lombok.Builder;

@Builder
public record UpdateTaskDto(
        @NotBlankOrNull(message = "Task name must be null or not blank.")
        String name,
        @NotBlankOrNull(message = "Task description must be null or not blank.")
        String description,
        TaskStatus status,
        TaskPriority priority,
        Long assigneeId
) {
}
